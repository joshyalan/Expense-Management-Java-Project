package view;

import dao.AccountDAOImpl;
import dao.CategoryDAOImpl;
import dao.ExpenseDAOImpl;
import dao.IncomeDAOImpl;
import model.Account;
import model.Category;
import model.Expense;
import model.Income;
import utils.SessionManager;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionDialog extends JDialog {
    private JComboBox<String> cbType;
    private JComboBox<Account> cbAccount;
    private JComboBox<Category> cbCategory;
    private JTextField txtAmount;
    private JTextField txtDate;
    private JTextField txtTime;
    private JTextField txtDesc;
    private JTextField txtReceipt;
    
    private AccountDAOImpl accountDAO = new AccountDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    private Runnable onSuccess;

    public TransactionDialog(JFrame parent, Runnable onSuccess) {
        super(parent, "Add Transaction", true);
        this.onSuccess = onSuccess;
        initUI();
    }

    private void initUI() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(Theme.getBgApp());
        
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, Theme.getBgApp());
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Theme.getTextPrimary());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.getBgApp());
        mainPanel.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Header
        JLabel lblHeader = new JLabel("New Transaction");
        lblHeader.setFont(Theme.fontHeading2());
        lblHeader.setForeground(Theme.getTextPrimary());
        lblHeader.setBorder(new EmptyBorder(0, 0, 24, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Form inside a Glass Card
        RoundedPanel formCard = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(24, 24, 24, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Type
        cbType = new JComboBox<>(new String[]{"Expense", "Income"});
        styleComponent(cbType);
        formCard.add(createLabeledField("Transaction Type", cbType), gbc);

        // Account
        gbc.gridy++;
        cbAccount = new JComboBox<>();
        List<Account> accounts = accountDAO.getAccountsByUser(SessionManager.getCurrentUser().getId());
        accounts.forEach(cbAccount::addItem);
        styleComponent(cbAccount);
        formCard.add(createLabeledField("Account", cbAccount), gbc);

        // Category
        gbc.gridy++;
        cbCategory = new JComboBox<>();
        styleComponent(cbCategory);
        formCard.add(createLabeledField("Category", cbCategory), gbc);
        
        cbType.addActionListener(e -> updateCategoryList());
        updateCategoryList(); // Init

        // Amount
        gbc.gridy++;
        txtAmount = new JTextField();
        txtAmount.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "0.00");
        styleComponent(txtAmount);
        formCard.add(createLabeledField("Amount ($)", txtAmount), gbc);

        // Date & Time Row
        gbc.gridy++;
        JPanel dateTimePanel = new JPanel(new GridLayout(1, 2, 16, 0));
        dateTimePanel.setOpaque(false);
        
        txtDate = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        styleComponent(txtDate);
        dateTimePanel.add(createLabeledField("Date (YYYY-MM-DD)", txtDate));
        
        txtTime = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        styleComponent(txtTime);
        dateTimePanel.add(createLabeledField("Time (HH:MM:SS)", txtTime));
        
        formCard.add(dateTimePanel, gbc);

        // Description
        gbc.gridy++;
        txtDesc = new JTextField();
        txtDesc.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "What was this for?");
        styleComponent(txtDesc);
        formCard.add(createLabeledField("Description", txtDesc), gbc);

        // Receipt
        gbc.gridy++;
        JPanel receiptPanel = new JPanel(new BorderLayout(8, 0));
        receiptPanel.setOpaque(false);
        txtReceipt = new JTextField();
        txtReceipt.setEditable(false);
        styleComponent(txtReceipt);
        
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.setFont(Theme.fontBodyBold());
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Images & PDFs", "jpg", "png", "pdf"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtReceipt.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        receiptPanel.add(txtReceipt, BorderLayout.CENTER);
        receiptPanel.add(btnBrowse, BorderLayout.EAST);
        
        formCard.add(createLabeledField("Receipt (Optional)", receiptPanel), gbc);

        mainPanel.add(formCard, BorderLayout.CENTER);

        // Footer / Buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        footerPanel.setBackground(Theme.getBgApp());
        footerPanel.setBorder(new EmptyBorder(24, 0, 0, 0));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(Theme.fontBodyBold());
        btnCancel.putClientProperty(FlatClientProperties.STYLE, "buttonType: borderless; foreground: " + hex(Theme.getTextMuted()));
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnSave = new JButton("Save Transaction");
        btnSave.setFont(Theme.fontBodyBold());
        btnSave.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 12; padding: 8,24,8,24; borderWidth: 0");
        btnSave.addActionListener(e -> saveTransaction());
        
        footerPanel.add(btnCancel);
        footerPanel.add(btnSave);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateCategoryList() {
        cbCategory.removeAllItems();
        String type = cbType.getSelectedItem().toString().toUpperCase();
        List<Category> categories = categoryDAO.getCategoriesByUserAndType(SessionManager.getCurrentUser().getId(), type);
        categories.forEach(cbCategory::addItem);
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.fontBodyBold());
        label.setForeground(Theme.getTextPrimary());
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(Theme.fontBody());
        if (comp instanceof JTextField || comp instanceof JComboBox) {
            comp.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        }
    }

    private void saveTransaction() {
        try {
            Account acc = (Account) cbAccount.getSelectedItem();
            Category cat = (Category) cbCategory.getSelectedItem();
            
            if (acc == null || cat == null) {
                ToastNotification.show((JFrame) getParent(), "Account and Category are required.", ToastNotification.WARNING);
                return;
            }
            
            double amt = Double.parseDouble(txtAmount.getText());
            Date dt = Date.valueOf(txtDate.getText());
            Time tm = Time.valueOf(txtTime.getText());
            String desc = txtDesc.getText().trim();
            String path = txtReceipt.getText().isEmpty() ? null : txtReceipt.getText();
            int userId = SessionManager.getCurrentUser().getId();

            boolean success = false;
            if (cbType.getSelectedItem().toString().equals("Expense")) {
                Expense ex = new Expense(userId, acc.getId(), cat.getId(), amt, dt, tm, desc, path);
                success = new ExpenseDAOImpl().addExpense(ex);
            } else {
                Income inc = new Income(userId, acc.getId(), cat.getId(), amt, dt, tm, desc, path);
                success = new IncomeDAOImpl().addIncome(inc);
            }

            if (success) {
                ToastNotification.show((JFrame) getParent(), "Transaction saved successfully!", ToastNotification.SUCCESS);
                if (onSuccess != null) onSuccess.run();
                dispose();
            } else {
                ToastNotification.show((JFrame) getParent(), "Database error occurred.", ToastNotification.ERROR);
            }
        } catch (IllegalArgumentException ex) {
            ToastNotification.show((JFrame) getParent(), "Please check Date (YYYY-MM-DD) and Time (HH:MM:SS) formats.", ToastNotification.ERROR);
        } catch (Exception ex) {
            ToastNotification.show((JFrame) getParent(), "Invalid amount.", ToastNotification.ERROR);
        }
    }

    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
