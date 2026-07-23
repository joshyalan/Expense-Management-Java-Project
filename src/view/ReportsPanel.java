package view;

import dao.ExpenseDAOImpl;
import model.Expense;
import utils.ReportGenerator;
import utils.SessionManager;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ReportsPanel extends JPanel {

    private ExpenseDAOImpl expenseDAO = new ExpenseDAOImpl();

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(244, 247, 252));
        setBorder(new EmptyBorder(40, 50, 40, 50));

        initComponents();
    }

    private void initComponents() {
        // Title
        JLabel title = new JLabel("Financial Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 30, 47));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Center Area (Grid of Export Cards)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setOpaque(false);

        // PDF Export Card
        RoundedPanel pdfCard = createExportCard("PDF Report", "Export your transaction history into a beautifully formatted PDF document for printing.", "Export to PDF", new Color(231, 76, 60));
        JButton btnExportPDF = (JButton) pdfCard.getClientProperty("exportButton");
        btnExportPDF.addActionListener(e -> handleExportPDF());
        centerPanel.add(pdfCard);

        // Excel Export Card (Placeholder for now)
        RoundedPanel excelCard = createExportCard("Excel / CSV", "Download your data in a spreadsheet format for advanced manual analysis.", "Export to Excel", new Color(46, 204, 113));
        JButton btnExportExcel = (JButton) excelCard.getClientProperty("exportButton");
        btnExportExcel.addActionListener(e -> handleExportCSV());
        centerPanel.add(excelCard);

        add(centerPanel, BorderLayout.CENTER);
    }

    private RoundedPanel createExportCard(String title, String desc, String btnText, Color accentColor) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setForeground(Color.GRAY);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setOpaque(false);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(txtDesc);
        
        card.add(Box.createVerticalGlue());
        
        JButton btnExport = new JButton(btnText);
        btnExport.putClientProperty(FlatClientProperties.STYLE, "arc: 15; padding: 10,20,10,20");
        btnExport.setBackground(accentColor);
        btnExport.setForeground(Color.WHITE);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(btnExport);

        card.putClientProperty("exportButton", btnExport);
        return card;
    }

    private void handleExportPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setSelectedFile(new File("Expense_Report.pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Get data
            if (SessionManager.isLoggedIn()) {
                List<Expense> expenses = expenseDAO.getAllExpensesByUser(SessionManager.getCurrentUser().getId());
                boolean success = ReportGenerator.generateExpenseReportPDF(expenses, fileToSave.getAbsolutePath());
                if (success) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "PDF Exported Successfully!", ToastNotification.SUCCESS);
                } else {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Failed to generate PDF.", ToastNotification.ERROR);
                }
            }
        }
    }

    private void handleExportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV Report");
        fileChooser.setSelectedFile(new File("Expense_Report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Get data
            if (SessionManager.isLoggedIn()) {
                List<Expense> expenses = expenseDAO.getAllExpensesByUser(SessionManager.getCurrentUser().getId());
                boolean success = ReportGenerator.generateExpenseReportCSV(expenses, fileToSave.getAbsolutePath());
                if (success) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "CSV Exported Successfully!", ToastNotification.SUCCESS);
                } else {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Failed to generate CSV.", ToastNotification.ERROR);
                }
            }
        }
    }
}
