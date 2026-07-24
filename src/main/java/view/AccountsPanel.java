package view;

import dao.AccountDAOImpl;
import model.Account;
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

public class AccountsPanel extends JPanel {
    private AccountDAOImpl accountDAO = new AccountDAOImpl();
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtSearch;
    private RoundedPanel cardPanel;
    private CardLayout cardLayout;

    public AccountsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        initComponents();
        loadAccounts();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel("Your Wallets");
        title.setFont(Theme.fontHeading2());
        title.setForeground(Theme.getTextPrimary());
        headerPanel.add(title, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actionPanel.setOpaque(false);

        txtSearch = new JTextField(20);
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search accounts...");
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
        
        JButton btnDelete = new JButton("Delete Wallet");
        btnDelete.setFont(Theme.fontBodyBold());
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: " + hex(Theme.DANGER) + "; foreground: #ffffff; padding: 6,16,6,16; borderWidth: 0");
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> deleteSelectedAccounts());
        actionPanel.add(btnDelete);

        JButton btnAdd = new JButton("+ Add Wallet");
        btnAdd.setFont(Theme.fontBodyBold());
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; padding: 6,16,6,16; borderWidth: 0");
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showAddAccountDialog());
        actionPanel.add(btnAdd);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        cardPanel.setLayout(cardLayout);
        cardPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] columns = {"ID", "Account Name", "Balance", "Currency"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
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
        cardPanel.add(scrollPane, "TABLE");

        utils.ui.EmptyStatePanel emptyState = new utils.ui.EmptyStatePanel(
            "empty_box.svg",
            "No Wallets Found",
            "You haven't added any wallets or accounts yet.",
            "Add Wallet",
            () -> showAddAccountDialog()
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

    private void loadAccounts() {
        tableModel.setRowCount(0);
        if (SessionManager.isLoggedIn()) {
            List<Account> accounts = accountDAO.getAccountsByUser(SessionManager.getCurrentUser().getId());
            if (accounts.isEmpty()) {
                cardLayout.show(cardPanel, "EMPTY");
            } else {
                cardLayout.show(cardPanel, "TABLE");
                for (Account acc : accounts) {
                    tableModel.addRow(new Object[]{
                        acc.getId(), 
                        acc.getName(), 
                        String.format("%.2f", acc.getBalance()), 
                        acc.getCurrency()
                    });
                }
            }
        }
    }

    private void deleteSelectedAccounts() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Please select an account to delete.", ToastNotification.WARNING);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure? This will delete the wallet.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                int id = (int) tableModel.getValueAt(modelRow, 0);
                if (accountDAO.deleteAccount(id)) {
                    successCount++;
                }
            }
            if (successCount > 0) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), successCount + " accounts deleted.", ToastNotification.SUCCESS);
                loadAccounts();
            }
        }
    }

    private void showAddAccountDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Wallet", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Theme.getBgApp());
        
        RoundedPanel formPanel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JTextField txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. Chase Checkings");
        txtName.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        txtName.setFont(Theme.fontBody());
        
        JPanel pName = new JPanel(new BorderLayout(0,6));
        pName.setOpaque(false);
        JLabel l1 = new JLabel("Wallet Name");
        l1.setFont(Theme.fontBodyBold());
        l1.setForeground(Theme.getTextPrimary());
        pName.add(l1, BorderLayout.NORTH);
        pName.add(txtName, BorderLayout.CENTER);
        formPanel.add(pName, gbc);
        
        gbc.gridy++;
        JTextField txtBalance = new JTextField("0.00");
        txtBalance.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        txtBalance.setFont(Theme.fontBody());
        
        JPanel pBal = new JPanel(new BorderLayout(0,6));
        pBal.setOpaque(false);
        JLabel l2 = new JLabel("Initial Balance");
        l2.setFont(Theme.fontBodyBold());
        l2.setForeground(Theme.getTextPrimary());
        pBal.add(l2, BorderLayout.NORTH);
        pBal.add(txtBalance, BorderLayout.CENTER);
        formPanel.add(pBal, gbc);

        gbc.gridy++;
        JButton btnSave = new JButton("Save Wallet");
        btnSave.setFont(Theme.fontBodyBold());
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 12; padding: 8,16,8,16; borderWidth: 0");
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a wallet name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double bal;
            try {
                bal = Double.parseDouble(txtBalance.getText().replace(",", ""));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid numeric balance.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Account acc = new Account(SessionManager.getCurrentUser().getId(), name, bal, "USD");
                if (accountDAO.addAccount(acc)) {
                    dialog.dispose();
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Wallet added!", ToastNotification.SUCCESS);
                    loadAccounts();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to save wallet to database (No rows inserted).", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Database Error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(btnSave, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
