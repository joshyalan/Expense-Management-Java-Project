package view;

import dao.AccountDAOImpl;
import dao.CategoryDAOImpl;
import dao.ExpenseDAOImpl;
import dao.IncomeDAOImpl;
import model.Account;
import model.Category;
import model.Expense;
import model.Income;
import model.Transaction;
import utils.SessionManager;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExpensesPanel extends JPanel {
    private ExpenseDAOImpl expenseDAO = new ExpenseDAOImpl();
    private IncomeDAOImpl incomeDAO = new IncomeDAOImpl();
    private AccountDAOImpl accountDAO = new AccountDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtSearch;
    private RoundedPanel cardPanel;
    private CardLayout cardLayout;

    public ExpensesPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        initComponents();
        loadExpenses();
    }

    private void initComponents() {
        // --- Header Panel (Title + Actions) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel("Transaction History");
        title.setFont(Theme.fontHeading2());
        title.setForeground(Theme.getTextPrimary());
        headerPanel.add(title, BorderLayout.WEST);

        // Action Buttons (Search + Delete)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actionPanel.setOpaque(false);

        // Search Field
        txtSearch = new JTextField(20);
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search transactions...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new com.formdev.flatlaf.extras.FlatSVGIcon("resources/icons/search.svg", 16, 16));
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 12; padding: 6,12,6,12");
        txtSearch.setFont(Theme.fontBody());
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        actionPanel.add(txtSearch);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setFont(Theme.fontBodyBold());
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: " + hex(Theme.DANGER) + "; foreground: #ffffff; padding: 6,16,6,16; borderWidth: 0");
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> deleteSelectedExpenses());
        actionPanel.add(btnDelete);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Area (Glassmorphism Card) ---
        cardLayout = new CardLayout();
        cardPanel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        cardPanel.setLayout(cardLayout);
        cardPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] columns = {"ID", "Type", "Account", "Category", "Amount", "Date", "Description", "RawType"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
        // Hide RawType and ID
        table.removeColumn(table.getColumnModel().getColumn(7)); // RawType
        table.removeColumn(table.getColumnModel().getColumn(0)); // ID (now at index 0 again)
        
        // Premium Table Styling
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(Theme.getBorder());
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(Theme.fontBodyBold());
        table.getTableHeader().setBackground(Theme.getBgApp());
        table.getTableHeader().setForeground(Theme.getTextMuted());
        table.getTableHeader().setBorder(new EmptyBorder(0, 0, 0, 0));
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        
        table.setFont(Theme.fontBody());
        table.setForeground(Theme.getTextPrimary());
        table.setBackground(Theme.getBgPanel());
        table.setSelectionBackground(new Color(Theme.PRIMARY.getRed(), Theme.PRIMARY.getGreen(), Theme.PRIMARY.getBlue(), 30));
        table.setSelectionForeground(Theme.getTextPrimary());
        
        // Custom renderer for amounts and padding
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                setBorder(new EmptyBorder(0, 16, 0, 16));
                
                int modelRow = table.convertRowIndexToModel(row);
                String type = (String) tableModel.getValueAt(modelRow, 7); // RawType
                
                if (column == table.getColumnModel().getColumnIndex("Amount")) {
                    if (type.equals("INCOME")) {
                        setForeground(Theme.SUCCESS);
                    } else {
                        setForeground(Theme.DANGER);
                    }
                } else if (column == table.getColumnModel().getColumnIndex("Type")) {
                    if (type.equals("INCOME")) {
                        setForeground(Theme.SUCCESS);
                    } else {
                        setForeground(Theme.DANGER);
                    }
                } else {
                    if (isSelected) setForeground(Theme.getTextPrimary());
                    else setForeground(Theme.getTextPrimary());
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Theme.getBgPanel());
        
        cardPanel.add(scrollPane, "TABLE");
        
        utils.ui.EmptyStatePanel emptyState = new utils.ui.EmptyStatePanel(
            "empty_box.svg",
            "No Transactions Found",
            "You haven't logged any transactions yet. Start tracking!",
            "Add Transaction",
            () -> {
                new TransactionDialog((JFrame) SwingUtilities.getWindowAncestor(this), this::loadExpenses).setVisible(true);
            }
        );
        cardPanel.add(emptyState, "EMPTY");

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

    public void loadExpenses() {
        tableModel.setRowCount(0);
        if (SessionManager.isLoggedIn()) {
            int userId = SessionManager.getCurrentUser().getId();
            
            List<Expense> expenses = expenseDAO.getAllExpensesByUser(userId);
            List<Income> incomes = incomeDAO.getAllIncomeByUser(userId);
            
            List<Account> accounts = accountDAO.getAccountsByUser(userId);
            List<Category> allCategories = new ArrayList<>();
            allCategories.addAll(categoryDAO.getCategoriesByUserAndType(userId, "EXPENSE"));
            allCategories.addAll(categoryDAO.getCategoriesByUserAndType(userId, "INCOME"));
            
            List<Transaction> allTransactions = new ArrayList<>();
            allTransactions.addAll(expenses);
            allTransactions.addAll(incomes);
            
            // Sort descending by date
            allTransactions.sort((t1, t2) -> {
                int dateCmp = t2.getDate().compareTo(t1.getDate());
                if (dateCmp != 0) return dateCmp;
                if (t1.getTime() != null && t2.getTime() != null) {
                    return t2.getTime().compareTo(t1.getTime());
                }
                return 0;
            });
            
            if (allTransactions.isEmpty()) {
                cardLayout.show(cardPanel, "EMPTY");
            } else {
                cardLayout.show(cardPanel, "TABLE");
                for (Transaction t : allTransactions) {
                    
                    String accName = "Unknown";
                    for (Account a : accounts) if (a.getId() == t.getAccountId()) accName = a.getName();
                    
                    String catName = "Unknown";
                    for (Category c : allCategories) if (c.getId() == t.getCategoryId()) catName = c.getName();
                    
                    boolean isIncome = t instanceof Income;
                    String typeStr = isIncome ? "Income" : "Expense";
                    String rawType = isIncome ? "INCOME" : "EXPENSE";
                    String amtStr = (isIncome ? "+$" : "-$") + String.format("%.2f", t.getAmount());
                    
                    tableModel.addRow(new Object[]{
                        t.getId(), 
                        typeStr,
                        accName, 
                        catName, 
                        amtStr, 
                        t.getDate().toString() + (t.getTime() != null ? " " + t.getTime().toString() : ""), 
                        t.getDescription() == null ? "" : t.getDescription(),
                        rawType
                    });
                }
            }
        }
    }

    private void deleteSelectedExpenses() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Please select at least one transaction to delete.", ToastNotification.WARNING);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected transactions?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                int id = (int) tableModel.getValueAt(modelRow, 0);
                String rawType = (String) tableModel.getValueAt(modelRow, 7);
                
                boolean deleted = false;
                if (rawType.equals("INCOME")) {
                    deleted = incomeDAO.deleteIncome(id);
                } else {
                    deleted = expenseDAO.deleteExpense(id);
                }
                
                if (deleted) successCount++;
            }
            if (successCount > 0) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), successCount + " transactions deleted.", ToastNotification.SUCCESS);
                loadExpenses();
            }
        }
    }
    
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
