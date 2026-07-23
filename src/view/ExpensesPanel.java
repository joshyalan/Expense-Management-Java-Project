package view;

import dao.ExpenseDAOImpl;
import model.Expense;
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
import java.util.List;

public class ExpensesPanel extends JPanel {
    private ExpenseDAOImpl expenseDAO = new ExpenseDAOImpl();
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtSearch;

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

        JLabel title = new JLabel("Expense History");
        title.setFont(Theme.fontHeading2());
        title.setForeground(Theme.getTextPrimary());
        headerPanel.add(title, BorderLayout.WEST);

        // Action Buttons (Search + Delete)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actionPanel.setOpaque(false);

        // Search Field
        txtSearch = new JTextField(20);
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search expenses...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
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
        RoundedPanel cardPanel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] columns = {"ID", "Account", "Category", "Amount", "Date", "Time", "Description", "Receipt"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
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
        
        // Remove focus border
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                setBorder(new EmptyBorder(0, 16, 0, 16));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Theme.getBgPanel());
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

    public void loadExpenses() {
        tableModel.setRowCount(0);
        if (SessionManager.isLoggedIn()) {
            List<Expense> expenses = expenseDAO.getAllExpensesByUser(SessionManager.getCurrentUser().getId());
            for (Expense ex : expenses) {
                String receipt = (ex.getReceiptPath() != null && !ex.getReceiptPath().isEmpty()) ? "📎 Attached" : "";
                tableModel.addRow(new Object[]{
                    ex.getId(), 
                    "Acct " + ex.getAccountId(), 
                    "Cat " + ex.getCategoryId(), 
                    "$" + String.format("%.2f", ex.getAmount()), 
                    ex.getDate(), 
                    ex.getTime() != null ? ex.getTime().toString() : "--",
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
                // Call Dashboard refresh instead of local if possible, but for now local is fine
                loadExpenses();
            }
        }
    }
    
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
