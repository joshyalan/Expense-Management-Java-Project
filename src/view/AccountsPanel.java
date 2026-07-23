package view;

import dao.AccountDAOImpl;
import model.Account;
import utils.SessionManager;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccountsPanel extends JPanel {

    private AccountDAOImpl accountDAO = new AccountDAOImpl();
    private JTable table;
    private DefaultTableModel tableModel;

    public AccountsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(244, 247, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        initComponents();
        loadAccounts();
    }

    private void initComponents() {
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("My Wallets & Accounts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 47));
        headerPanel.add(title, BorderLayout.WEST);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);

        JButton btnDelete = new JButton("Delete Account");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #e74c3c; foreground: #fff; padding: 5,15,5,15");
        btnDelete.addActionListener(e -> deleteSelectedAccount());
        actionPanel.add(btnDelete);

        JButton btnAdd = new JButton("+ Add Account");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #4361ee; foreground: #fff; padding: 5,15,5,15");
        btnAdd.addActionListener(e -> showAddAccountDialog());
        actionPanel.add(btnAdd);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Area ---
        RoundedPanel cardPanel = new RoundedPanel(20, Color.WHITE);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Account Name", "Balance", "Currency", "Created At"};
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
        scrollPane.getViewport().setBackground(Color.WHITE);
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        add(cardPanel, BorderLayout.CENTER);
    }

    private void loadAccounts() {
        tableModel.setRowCount(0);
        if (SessionManager.isLoggedIn()) {
            List<Account> accounts = accountDAO.getAccountsByUser(SessionManager.getCurrentUser().getId());
            for (Account acc : accounts) {
                tableModel.addRow(new Object[]{
                    acc.getId(), 
                    acc.getName(), 
                    String.format("%.2f", acc.getBalance()), 
                    acc.getCurrency(),
                    acc.getCreatedAt()
                });
            }
        }
    }

    private void showAddAccountDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Account", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtName = new JTextField();
        JTextField txtBalance = new JTextField("0.0");
        JComboBox<String> cbCurrency = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "INR"});

        form.add(new JLabel("Account Name:")); form.add(txtName);
        form.add(new JLabel("Initial Balance:")); form.add(txtBalance);
        form.add(new JLabel("Currency:")); form.add(cbCurrency);

        JButton btnSave = new JButton("Save Account");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #4361ee; foreground: #fff");
        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText();
                double balance = Double.parseDouble(txtBalance.getText());
                String curr = cbCurrency.getSelectedItem().toString();

                Account acc = new Account(SessionManager.getCurrentUser().getId(), name, balance, curr);
                if (accountDAO.addAccount(acc)) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Account created!", ToastNotification.SUCCESS);
                    loadAccounts();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Check inputs. Balance must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnSave);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteSelectedAccount() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Select an account to delete.", ToastNotification.WARNING);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this account? (Will also delete associated transactions)", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (accountDAO.deleteAccount(id)) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Account deleted.", ToastNotification.SUCCESS);
                loadAccounts();
            }
        }
    }
}
