package view;

import dao.BudgetDAOImpl;
import dao.CategoryDAOImpl;
import model.Budget;
import model.Category;
import utils.SessionManager;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class BudgetsPanel extends JPanel {

    private BudgetDAOImpl budgetDAO = new BudgetDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    private JTable table;
    private DefaultTableModel tableModel;
    private RoundedPanel cardPanel;
    private CardLayout cardLayout;

    public BudgetsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(20, 30, 20, 30));

        initComponents();
        loadBudgets();
    }

    private void initComponents() {
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Budgets & Goals");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 47));
        headerPanel.add(title, BorderLayout.WEST);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);

        JButton btnDelete = new JButton("Delete");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #e74c3c; foreground: #fff; padding: 5,15,5,15");
        btnDelete.addActionListener(e -> deleteSelectedBudget());
        actionPanel.add(btnDelete);

        JButton btnEdit = new JButton("Edit");
        btnEdit.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #f39c12; foreground: #fff; padding: 5,15,5,15");
        btnEdit.addActionListener(e -> showEditBudgetDialog());
        actionPanel.add(btnEdit);

        JButton btnAdd = new JButton("+ Add Budget");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #4361ee; foreground: #fff; padding: 5,15,5,15");
        btnAdd.addActionListener(e -> showAddBudgetDialog());
        actionPanel.add(btnAdd);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Area ---
        cardLayout = new CardLayout();
        cardPanel = new RoundedPanel(20, Theme.getBgPanel());
        cardPanel.setLayout(cardLayout);
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Category", "Amount", "Month", "Year"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 246, 250));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Theme.getBgPanel());
        cardPanel.add(scrollPane, "TABLE");
        
        utils.ui.EmptyStatePanel emptyState = new utils.ui.EmptyStatePanel(
            "empty_box.svg",
            "No Budgets Found",
            "You haven't set any budgets yet.",
            "Add Budget",
            () -> showAddBudgetDialog()
        );
        cardPanel.add(emptyState, "EMPTY");

        add(cardPanel, BorderLayout.CENTER);
    }

    private void loadBudgets() {
        tableModel.setRowCount(0);
        if (SessionManager.isLoggedIn()) {
            int userId = SessionManager.getCurrentUser().getId();
            List<Budget> budgets = budgetDAO.getAllBudgetsByUser(userId);
            
            if (budgets.isEmpty()) {
                cardLayout.show(cardPanel, "EMPTY");
            } else {
                cardLayout.show(cardPanel, "TABLE");
                List<Category> categories = categoryDAO.getCategoriesByUserAndType(userId, "EXPENSE");

                for (Budget budget : budgets) {
                    String catName = "Unknown";
                    for (Category c : categories) {
                        if (c.getId() == budget.getCategoryId()) {
                            catName = c.getName();
                            break;
                        }
                    }

                    tableModel.addRow(new Object[]{
                        budget.getId(), 
                        catName, 
                        String.format("%.2f", budget.getAmount()), 
                        budget.getMonth(),
                        budget.getYear()
                    });
                }
            }
        }
    }

    private void showAddBudgetDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Budget", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<CategoryItem> cbCategory = new JComboBox<>();
        if (SessionManager.isLoggedIn()) {
            List<Category> categories = categoryDAO.getCategoriesByUserAndType(SessionManager.getCurrentUser().getId(), "EXPENSE");
            for (Category c : categories) {
                cbCategory.addItem(new CategoryItem(c.getId(), c.getName()));
            }
        }

        JTextField txtAmount = new JTextField("0.0");
        JComboBox<Integer> cbMonth = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        JTextField txtYear = new JTextField(String.valueOf(java.time.Year.now().getValue()));

        form.add(new JLabel("Category:")); form.add(cbCategory);
        form.add(new JLabel("Amount:")); form.add(txtAmount);
        form.add(new JLabel("Month:")); form.add(cbMonth);
        form.add(new JLabel("Year:")); form.add(txtYear);

        JButton btnSave = new JButton("Save Budget");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #4361ee; foreground: #fff");
        btnSave.addActionListener(e -> {
            try {
                CategoryItem selectedCat = (CategoryItem) cbCategory.getSelectedItem();
                if (selectedCat == null) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Please select a category.", ToastNotification.WARNING);
                    return;
                }
                
                double amount = Double.parseDouble(txtAmount.getText());
                if (amount < 0) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Amount cannot be negative.", ToastNotification.WARNING);
                    return;
                }
                int month = (Integer) cbMonth.getSelectedItem();
                int year = Integer.parseInt(txtYear.getText());

                Budget budget = new Budget(SessionManager.getCurrentUser().getId(), selectedCat.id, amount, month, year);
                
                // Check if exists
                Budget existing = budgetDAO.getBudgetByCategoryAndMonth(budget.getUserId(), budget.getCategoryId(), budget.getMonth(), budget.getYear());
                if (existing != null) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Budget already exists for this category/month.", ToastNotification.WARNING);
                    return;
                }

                if (budgetDAO.addBudget(budget)) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Budget created!", ToastNotification.SUCCESS);
                    loadBudgets();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Check inputs. Amount and Year must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnSave);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showEditBudgetDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Select a budget to edit.", ToastNotification.WARNING);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Budget budget = budgetDAO.getBudgetById(id);
        if (budget == null) return;

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Budget", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtAmount = new JTextField(String.valueOf(budget.getAmount()));
        form.add(new JLabel("New Amount:")); form.add(txtAmount);

        JButton btnSave = new JButton("Update Budget");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #f39c12; foreground: #fff");
        btnSave.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText());
                if (amount < 0) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Amount cannot be negative.", ToastNotification.WARNING);
                    return;
                }
                budget.setAmount(amount);

                if (budgetDAO.updateBudget(budget)) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Budget updated!", ToastNotification.SUCCESS);
                    loadBudgets();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Check inputs. Amount must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnSave);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteSelectedBudget() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Select a budget to delete.", ToastNotification.WARNING);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this budget?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (budgetDAO.deleteBudget(id)) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Budget deleted.", ToastNotification.SUCCESS);
                loadBudgets();
            }
        }
    }

    private static class CategoryItem {
        int id;
        String name;

        public CategoryItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
