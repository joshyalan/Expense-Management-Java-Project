package view;

import model.Category;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CategoryPickerDialog extends JDialog {
    private List<Category> allCategories;
    private List<Category> filteredCategories;
    private Category selectedCategory = null;
    
    private JTextField txtSearch;
    private JPanel gridPanel;
    private JScrollPane scrollPane;

    public CategoryPickerDialog(JDialog parent, List<Category> categories, String type) {
        super(parent, "Select " + type + " Category", true);
        this.allCategories = new ArrayList<>(categories);
        this.filteredCategories = new ArrayList<>(categories);
        
        initUI();
    }

    private void initUI() {
        setSize(450, 600);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(Theme.getBgApp());
        
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, Theme.getBgApp());
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Theme.getTextPrimary());

        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBackground(Theme.getBgApp());
        mainPanel.setBorder(new EmptyBorder(16, 24, 24, 24));

        // Search Bar
        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search categories...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 16; margin: 8,16,8,16;");
        txtSearch.setFont(Theme.fontBody());
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(txtSearch, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Grid Panel
        gridPanel = new JPanel(new GridLayout(0, 2, 12, 12));
        gridPanel.setBackground(Theme.getBgApp());
        
        scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(Theme.getBgApp());
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Custom Category Button
        JButton btnCustom = new JButton("+ Create Custom Category");
        btnCustom.setFont(Theme.fontBodyBold());
        btnCustom.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + Theme.hex(Theme.getBgPanel()) + "; foreground: " + Theme.hex(Theme.PRIMARY) + 
            "; arc: 12; padding: 12,24,12,24; borderWidth: 1; borderColor: " + Theme.hex(Theme.getBorder()));
        btnCustom.addActionListener(e -> {
            String customName = JOptionPane.showInputDialog(this, "Enter new category name:", "Custom Category", JOptionPane.PLAIN_MESSAGE);
            if (customName != null && !customName.trim().isEmpty()) {
                selectedCategory = new Category(-1, -1, customName.trim(), "", "", "", null); // Dummy representing custom
                dispose();
            }
        });
        mainPanel.add(btnCustom, BorderLayout.SOUTH);

        add(mainPanel);
        renderGrid();
    }

    private void filter() {
        String q = txtSearch.getText().toLowerCase().trim();
        filteredCategories.clear();
        for (Category c : allCategories) {
            if (c.getName().toLowerCase().contains(q)) {
                filteredCategories.add(c);
            }
        }
        renderGrid();
    }

    private void renderGrid() {
        gridPanel.removeAll();
        for (Category cat : filteredCategories) {
            gridPanel.add(createCategoryCard(cat));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCategoryCard(Category cat) {
        RoundedPanel card = new RoundedPanel(Theme.RADIUS_MD, Theme.getBgPanel());
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        card.setBorder(BorderFactory.createLineBorder(Theme.getBorder(), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Color Dot
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                try {
                    g2.setColor(Color.decode(cat.getColor()));
                } catch (Exception e) {
                    g2.setColor(Theme.PRIMARY);
                }
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(16, 16));
        dot.setOpaque(false);
        
        JLabel lblName = new JLabel(cat.getName());
        lblName.setFont(Theme.fontBody());
        lblName.setForeground(Theme.getTextPrimary());
        
        card.add(dot);
        card.add(lblName);
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(Theme.getBgApp()); // slight hover effect
                card.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Theme.getBgPanel());
                card.setBorder(BorderFactory.createLineBorder(Theme.getBorder(), 1));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedCategory = cat;
                dispose();
            }
        });
        
        return card;
    }
    
    public Category getSelectedCategory() {
        return selectedCategory;
    }
}
