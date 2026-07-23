package view;

import dao.AccountDAOImpl;
import dao.CategoryDAOImpl;
import dao.ExpenseDAOImpl;
import model.Account;
import model.Category;
import model.Expense;
import utils.SessionManager;
import com.formdev.flatlaf.FlatClientProperties;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.util.List;

public class ExpensesPanel extends JPanel {
    private ExpenseDAOImpl expenseDAO = new ExpenseDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    private AccountDAOImpl accountDAO = new AccountDAOImpl();
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtSearch;

    public ExpensesPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(244, 247, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        initComponents();
        loadExpenses();
    }

    private void initComponents() {
        // --- Header Panel (Title + Actions) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 47));
        headerPanel.add(title, BorderLayout.WEST);

        // Action Buttons (Search + Delete + Add)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);

        // Search Field
        txtSearch = new JTextField(15);
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 15; padding: 5,10,5,10");
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        actionPanel.add(txtSearch);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #e74c3c; foreground: #fff; padding: 5,15,5,15");
        btnDelete.addActionListener(e -> deleteSelectedExpenses());
        actionPanel.add(btnDelete);

        JButton btnAdd = new JButton("+ Add Expense");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #4361ee; foreground: #fff; padding: 5,15,5,15");
        btnAdd.addActionListener(e -> showAddExpenseDialog());
        actionPanel.add(btnAdd);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Area (Glassmorphism Card) ---
        RoundedPanel cardPanel = new RoundedPanel(20, Color.WHITE);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Account", "Category", "Amount", "Date", "Description", "Receipt"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 246, 250));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        add(cardPanel, BorderLayout.CENTER);
    }

    private void filter() {
        String text = txtSearch.getText();
        if (text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void loadExpenses() {
        tableModel.setRowCount(0);
        if (SessionManager.isLoggedIn()) {
            List<Expense> expenses = expenseDAO.getAllExpensesByUser(SessionManager.getCurrentUser().getId());
            for (Expense ex : expenses) {
                // Formatting receipt indicator
                String receipt = (ex.getReceiptPath() != null && !ex.getReceiptPath().isEmpty()) ? "📎 Attached" : "";
                tableModel.addRow(new Object[]{
                    ex.getId(), 
                    "Account " + ex.getAccountId(), // We would ideally fetch Account Name here via JOIN or map
                    "Category " + ex.getCategoryId(), // We would ideally fetch Category Name here
                    "$" + String.format("%.2f", ex.getAmount()), 
                    ex.getDate(), 
                    ex.getDescription(),
                    receipt
                });
            }
        }
    }

    private void deleteSelectedExpenses() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Please select at least one expense to delete.", ToastNotification.WARNING);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected transactions?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                int id = (int) tableModel.getValueAt(modelRow, 0);
                if (expenseDAO.deleteExpense(id)) {
                    successCount++;
                }
            }
            if (successCount > 0) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), successCount + " transactions deleted.", ToastNotification.SUCCESS);
                loadExpenses();
            }
        }
    }

    private void showAddExpenseDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Transaction", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form Fields
        JComboBox<Account> cbAccount = new JComboBox<>();
        List<Account> accounts = accountDAO.getAccountsByUser(SessionManager.getCurrentUser().getId());
        accounts.forEach(cbAccount::addItem);

        JComboBox<Category> cbCategory = new JComboBox<>();
        List<Category> categories = categoryDAO.getCategoriesByUserAndType(SessionManager.getCurrentUser().getId(), "EXPENSE");
        categories.forEach(cbCategory::addItem);

        JTextField txtAmount = new JTextField();
        JTextField txtDate = new JTextField(new java.sql.Date(System.currentTimeMillis()).toString()); // Default to today
        JTextField txtDesc = new JTextField();
        
        // Receipt Upload
        JPanel receiptPanel = new JPanel(new BorderLayout(5, 0));
        JTextField txtReceipt = new JTextField();
        txtReceipt.setEditable(false);
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Images & PDFs", "jpg", "png", "pdf"));
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                txtReceipt.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        receiptPanel.add(txtReceipt, BorderLayout.CENTER);
        receiptPanel.add(btnBrowse, BorderLayout.EAST);

        form.add(new JLabel("Account:")); form.add(cbAccount);
        form.add(new JLabel("Category:")); form.add(cbCategory);
        form.add(new JLabel("Amount ($):")); form.add(txtAmount);
        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(txtDate);
        form.add(new JLabel("Description:")); form.add(txtDesc);
        form.add(new JLabel("Receipt:")); form.add(receiptPanel);

        JButton btnSave = new JButton("Save Transaction");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #4361ee; foreground: #fff");
        btnSave.addActionListener(e -> {
            try {
                Account acc = (Account) cbAccount.getSelectedItem();
                Category cat = (Category) cbCategory.getSelectedItem();
                double amt = Double.parseDouble(txtAmount.getText());
                Date dt = Date.valueOf(txtDate.getText());
                String path = txtReceipt.getText().isEmpty() ? null : txtReceipt.getText();

                Expense ex = new Expense(SessionManager.getCurrentUser().getId(), acc.getId(), cat.getId(), amt, dt, txtDesc.getText(), path);
                if (expenseDAO.addExpense(ex)) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Expense added successfully!", ToastNotification.SUCCESS);
                    loadExpenses();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Please check your inputs (Date format: YYYY-MM-DD, Amount must be a number).", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnSave);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
