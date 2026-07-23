package view;

import dao.ExpenseDAOImpl;
import dao.IncomeDAOImpl;
import model.Expense;
import model.Income;
import utils.ReportGenerator;
import utils.SessionManager;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;
import javax.swing.JFileChooser;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ReportsPanel extends JPanel {

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(40, 50, 40, 50));

        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel("Financial Reports");
        title.setFont(Theme.fontHeading2());
        title.setForeground(Theme.getTextPrimary());
        title.setBorder(new EmptyBorder(0, 0, 24, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setOpaque(false);

        RoundedPanel pdfCard = createExportCard("PDF Report", "Export your transaction history into a beautifully formatted PDF document for printing.", "Export to PDF", Theme.DANGER);
        centerPanel.add(pdfCard);

        RoundedPanel excelCard = createExportCard("Excel / CSV", "Download your data in a spreadsheet format for advanced manual analysis.", "Export to Excel", Theme.SUCCESS);
        centerPanel.add(excelCard);

        add(centerPanel, BorderLayout.CENTER);
    }

    private RoundedPanel createExportCard(String title, String desc, String btnText, Color accentColor) {
        RoundedPanel card = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 32, 32, 32));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Theme.fontHeading3());
        lblTitle.setForeground(Theme.getTextPrimary());
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setFont(Theme.fontBody());
        txtDesc.setForeground(Theme.getTextMuted());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setOpaque(false);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(txtDesc);
        
        card.add(Box.createVerticalGlue());
        
        JButton btnExport = new JButton(btnText);
        btnExport.setFont(Theme.fontBodyBold());
        btnExport.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #" + Integer.toHexString(accentColor.getRGB()).substring(2) + "; foreground: #ffffff; padding: 10,20,10,20; borderWidth: 0");
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnExport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Report");
            
            String extension = title.contains("PDF") ? ".pdf" : ".xlsx";
            chooser.setSelectedFile(new File("Expense_Report" + extension));
            
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(extension)) {
                    path += extension;
                }
                
                int userId = SessionManager.getCurrentUser().getId();
                List<Expense> expenses = new ExpenseDAOImpl().getAllExpensesByUser(userId);
                List<Income> incomes = new IncomeDAOImpl().getAllIncomesByUser(userId);
                
                boolean success;
                if (title.contains("PDF")) {
                    success = ReportGenerator.generatePDFReport(path, incomes, expenses);
                } else {
                    success = ReportGenerator.generateExcelReport(path, incomes, expenses);
                }
                
                if (success) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Report saved successfully!", ToastNotification.SUCCESS);
                } else {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Failed to save report.", ToastNotification.ERROR);
                }
            }
        });
        
        card.add(btnExport);
        return card;
    }
}
