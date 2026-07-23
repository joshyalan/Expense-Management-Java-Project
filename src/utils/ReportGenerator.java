package utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Expense;
import model.Income;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    public static boolean generatePDFReport(String filePath, List<Income> incomes, List<Expense> expenses) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            document.add(new Paragraph("Expense Tracker Pro - Financial Report", titleFont));
            document.add(new Paragraph(" "));
            
            double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
            double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
            
            document.add(new Paragraph("Summary:", headerFont));
            document.add(new Paragraph("Total Income: $" + String.format("%.2f", totalIncome)));
            document.add(new Paragraph("Total Expenses: $" + String.format("%.2f", totalExpense)));
            document.add(new Paragraph("Net Balance: $" + String.format("%.2f", (totalIncome - totalExpense))));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Income Transactions", headerFont));
            document.add(new Paragraph(" "));
            PdfPTable incomeTable = new PdfPTable(4);
            incomeTable.addCell(new PdfPCell(new Phrase("Date", headerFont)));
            incomeTable.addCell(new PdfPCell(new Phrase("Category ID", headerFont)));
            incomeTable.addCell(new PdfPCell(new Phrase("Description", headerFont)));
            incomeTable.addCell(new PdfPCell(new Phrase("Amount", headerFont)));
            for (Income inc : incomes) {
                incomeTable.addCell(inc.getDate().toString());
                incomeTable.addCell(String.valueOf(inc.getCategoryId()));
                incomeTable.addCell(inc.getDescription());
                incomeTable.addCell("$" + String.format("%.2f", inc.getAmount()));
            }
            document.add(incomeTable);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Expense Transactions", headerFont));
            document.add(new Paragraph(" "));
            PdfPTable expenseTable = new PdfPTable(4);
            expenseTable.addCell(new PdfPCell(new Phrase("Date", headerFont)));
            expenseTable.addCell(new PdfPCell(new Phrase("Category ID", headerFont)));
            expenseTable.addCell(new PdfPCell(new Phrase("Description", headerFont)));
            expenseTable.addCell(new PdfPCell(new Phrase("Amount", headerFont)));
            for (Expense exp : expenses) {
                expenseTable.addCell(exp.getDate().toString());
                expenseTable.addCell(String.valueOf(exp.getCategoryId()));
                expenseTable.addCell(exp.getDescription());
                expenseTable.addCell("$" + String.format("%.2f", exp.getAmount()));
            }
            document.add(expenseTable);

            document.close();
            return true;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean generateExcelReport(String filePath, List<Income> incomes, List<Expense> expenses) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Financial Report");
            
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Type");
            headerRow.createCell(1).setCellValue("Date");
            headerRow.createCell(2).setCellValue("Category ID");
            headerRow.createCell(3).setCellValue("Description");
            headerRow.createCell(4).setCellValue("Amount");
            
            int rowNum = 1;
            for (Income inc : incomes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("Income");
                row.createCell(1).setCellValue(inc.getDate().toString());
                row.createCell(2).setCellValue(inc.getCategoryId());
                row.createCell(3).setCellValue(inc.getDescription());
                row.createCell(4).setCellValue(inc.getAmount());
            }
            
            for (Expense exp : expenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("Expense");
                row.createCell(1).setCellValue(exp.getDate().toString());
                row.createCell(2).setCellValue(exp.getCategoryId());
                row.createCell(3).setCellValue(exp.getDescription());
                row.createCell(4).setCellValue(exp.getAmount());
            }
            
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
            
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
