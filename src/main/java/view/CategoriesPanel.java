package view;

import dao.CategoryDAOImpl;
import model.Category;
import utils.SessionManager;
import utils.ui.EmptyStatePanel;
import utils.ui.Theme;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CategoriesPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    private List<Category> categoriesList;
    private JPanel contentPanel;
    private EmptyStatePanel emptyState;
    private JScrollPane scrollPane;

    public CategoriesPanel() {
        setLayout(new BorderLayout(0, 24));
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(32, 40, 32, 40));
        
        initHeader();
        initContent();
        loadCategories();
    }

    private void initHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Category Management");
        titleLabel.setFont(Theme.fontHeading1());
        titleLabel.setForeground(Theme.getTextPrimary());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton btnLoadDefaults = new JButton("Load Defaults");
        btnLoadDefaults.setFont(Theme.fontBodyBold());
        btnLoadDefaults.putClientProperty(FlatClientProperties.STYLE, 
            "background: #747D8C; foreground: #ffffff; arc: 12; padding: 8,16,8,16; borderWidth: 0");
        btnLoadDefaults.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoadDefaults.addActionListener(e -> {
            if (categoryDAO.seedDefaultCategories(SessionManager.getCurrentUser().getId())) {
                loadCategories();
                JOptionPane.showMessageDialog(this, "Default categories loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load default categories. Please check the IntelliJ console for errors.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(btnLoadDefaults);

        JButton btnAdd = new JButton("+ New Category");
        btnAdd.setFont(Theme.fontBodyBold());
        btnAdd.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + Theme.hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 12; padding: 8,16,8,16; borderWidth: 0");
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showAddCategoryDialog());
        buttonsPanel.add(btnAdd);
        
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void initContent() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        emptyState = new EmptyStatePanel(
            "empty_box.svg",
            "No Categories Found",
            "You haven't created any custom categories yet.",
            "Create Category",
            () -> showAddCategoryDialog()
        );

        String[] columns = {"ID", "Color", "Name", "Type", "Icon"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(48);
        table.setFont(Theme.fontBody());
        table.getTableHeader().setFont(Theme.fontBodyBold());
        table.getTableHeader().setBackground(Theme.getBgPanel());
        table.getTableHeader().setForeground(Theme.getTextMuted());
        table.setShowVerticalLines(false);
        table.setGridColor(Theme.getBorder());
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(Theme.getBgApp());
        table.setSelectionForeground(Theme.getTextPrimary());
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        
        // Color Dot Renderer
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
                p.setBackground(isSelected ? table.getSelectionBackground() : Theme.getBgPanel());
                if (value != null) {
                    JPanel dot = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            try { g2.setColor(Color.decode(value.toString())); } 
                            catch (Exception ex) { g2.setColor(Theme.PRIMARY); }
                            g2.fillOval(0, 0, getWidth(), getHeight());
                            g2.dispose();
                        }
                    };
                    dot.setPreferredSize(new Dimension(16, 16));
                    dot.setOpaque(false);
                    p.add(dot);
                }
                return p;
            }
        });
        
        // Default Renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.getBorder(), 1));
        scrollPane.getViewport().setBackground(Theme.getBgPanel());
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
                    int id = (int) tableModel.getValueAt(modelRow, 0);
                    showCategoryDetailsDialog(id);
                }
            }
        });

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        int userId = SessionManager.getCurrentUser().getId();
        
        List<Category> expenses = categoryDAO.getCategoriesByUserAndType(userId, "EXPENSE");
        List<Category> incomes = categoryDAO.getCategoriesByUserAndType(userId, "INCOME");
        
        categoriesList = new java.util.ArrayList<>();
        categoriesList.addAll(expenses);
        categoriesList.addAll(incomes);

        if (categoriesList.isEmpty()) {
            contentPanel.removeAll();
            contentPanel.add(emptyState, BorderLayout.CENTER);
        } else {
            for (Category c : categoriesList) {
                tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getColor(),
                    c.getName(),
                    c.getType(),
                    c.getIcon()
                });
            }
            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAddCategoryDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create Category", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Theme.getBgApp());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.getBgApp());
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.gridx = 0; gbc.gridy = 0;

        JComboBox<String> cbType = new JComboBox<>(new String[]{"EXPENSE", "INCOME"});
        cbType.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        panel.add(createLabeledField("Type", cbType), gbc);
        
        gbc.gridy++;
        JTextField txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        panel.add(createLabeledField("Name", txtName), gbc);
        
        gbc.gridy++;
        JTextField txtColor = new JTextField("#A55EEA"); // Default color
        txtColor.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        panel.add(createLabeledField("Color (Hex Code)", txtColor), gbc);

        gbc.gridy++;
        JButton btnSave = new JButton("Save Category");
        btnSave.setFont(Theme.fontBodyBold());
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background: " + Theme.hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 12; padding: 8,24,8,24; borderWidth: 0");
        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category cat = new Category(SessionManager.getCurrentUser().getId(), txtName.getText().trim(), cbType.getSelectedItem().toString());
            cat.setColor(txtColor.getText().trim());
            cat.setIcon("box"); // default icon
            
            if (categoryDAO.addCategory(cat)) {
                dialog.dispose();
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnSave, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showCategoryDetailsDialog(int categoryId) {
        Category cat = categoriesList.stream().filter(c -> c.getId() == categoryId).findFirst().orElse(null);
        if (cat == null) return;
        
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Category", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Theme.getBgApp());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.getBgApp());
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.gridx = 0; gbc.gridy = 0;

        JTextField txtName = new JTextField(cat.getName());
        txtName.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        panel.add(createLabeledField("Name", txtName), gbc);
        
        gbc.gridy++;
        JTextField txtColor = new JTextField(cat.getColor());
        txtColor.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        panel.add(createLabeledField("Color (Hex Code)", txtColor), gbc);

        gbc.gridy++;
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setOpaque(false);
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(Theme.fontBodyBold());
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "background: " + Theme.hex(Theme.DANGER) + "; foreground: #ffffff; arc: 12; padding: 8,0,8,0; borderWidth: 0");
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "Delete category: " + cat.getName() + "?\nTransactions using this category may be affected.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (categoryDAO.deleteCategory(cat.getId())) {
                    dialog.dispose();
                    loadCategories();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cannot delete category. It might be used in existing transactions.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnSave = new JButton("Save");
        btnSave.setFont(Theme.fontBodyBold());
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background: " + Theme.hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 12; padding: 8,0,8,0; borderWidth: 0");
        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cat.setName(txtName.getText().trim());
            cat.setColor(txtColor.getText().trim());
            
            if (categoryDAO.updateCategory(cat)) {
                dialog.dispose();
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnPanel.add(btnDelete);
        btnPanel.add(btnSave);
        panel.add(btnPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
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
}
