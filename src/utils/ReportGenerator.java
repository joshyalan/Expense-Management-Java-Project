package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Expense;

import java.io.FileOutputStream;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating Financial Reports (PDF, Excel).
 */
public class ReportGenerator {

    /**
     * Generates a PDF Report of the provided expenses.
     * @param expenses List of expenses to include in the report.
     * @param destPath The file path to save the PDF.
     * @return true if successful, false otherwise.
     */
    public static boolean generateExpenseReportPDF(List<Expense> expenses, String destPath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(destPath));
            document.open();

            // Add Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("Expense Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add Date
            Paragraph dateText = new Paragraph("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            dateText.setSpacingAfter(20);
            document.add(dateText);

            // Create Table (4 columns)
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Table Headers
            String[] headers = {"Date", "Category ID", "Description", "Amount ($)"};
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new BaseColor(67, 97, 238)); // Modern Blue Accent
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Table Data
            double total = 0.0;
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
            for (Expense e : expenses) {
                table.addCell(new PdfPCell(new Phrase(e.getDate().toString(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(e.getCategoryId()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(e.getDescription(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", e.getAmount()), dataFont)));
                total += e.getAmount();
            }

            document.add(table);
            
            // Total Summary
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.RED);
            Paragraph totalParagraph = new Paragraph("Total Expenses: $" + String.format("%.2f", total), totalFont);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean generateExpenseReportCSV(List<Expense> expenses, String destPath) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(destPath))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Date,Category ID,Description,Amount\n");
            
            for (Expense e : expenses) {
                sb.append(e.getDate().toString()).append(",");
                sb.append(e.getCategoryId()).append(",");
                
                String desc = e.getDescription() != null ? e.getDescription().replace(",", ";") : "";
                sb.append(desc).append(",");
                sb.append(String.format("%.2f", e.getAmount())).append("\n");
            }
            
            writer.write(sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
