package view;

import utils.SessionManager;
import utils.ui.RoundedPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import service.TransactionService;
import model.Transaction;
import model.Expense;
import model.Income;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Premium Dashboard GUI with Glassmorphism and Modern Card Layouts.
 */
public class DashboardView extends JFrame {

    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Premium Color Palette
    private final Color SIDEBAR_BG = new Color(30, 30, 47);
    private final Color SIDEBAR_BTN_HOVER = new Color(50, 50, 75);
    private final Color CONTENT_BG = new Color(244, 247, 252);

    public DashboardView() {
        setTitle("ExpenseTracker Pro - Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Remove standard window borders if FlatLaf supports it (optional)
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, SIDEBAR_BG);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Color.WHITE);

        initComponents();
        
        // Show Welcome Toast Notification
        SwingUtilities.invokeLater(() -> {
            String username = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getUsername() : "User";
            utils.ui.ToastNotification.show(this, "Welcome back, " + username + "!", utils.ui.ToastNotification.SUCCESS);
        });
    }

    private void initComponents() {
        // --- 1. Sidebar (West) ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(240, 0));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Logo / Title area
        JLabel lblLogo = new JLabel("ExpenseTracker Pro");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(10, 0, 40, 0));
        sidebarPanel.add(lblLogo);

        // Sidebar Navigation Buttons
        sidebarPanel.add(createSidebarButton("Dashboard", "homeCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(createSidebarButton("Accounts", "accountsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(createSidebarButton("Transactions", "transactionsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(createSidebarButton("Budgets & Goals", "budgetsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(createSidebarButton("Reports", "reportsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(createSidebarButton("Settings", "settingsCard"));
        
        sidebarPanel.add(Box.createVerticalGlue()); // Push logout to bottom

        // User Info & Logout
        String username = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getUsername() : "User";
        JLabel lblUser = new JLabel("👤 " + username);
        lblUser.setForeground(new Color(200, 200, 200));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUser.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebarPanel.add(lblUser);

        JButton btnLogout = createSidebarButton("Logout", null);
        btnLogout.addActionListener(e -> handleLogout());
        sidebarPanel.add(btnLogout);

        add(sidebarPanel, BorderLayout.WEST);

        // --- 2. Content Area (Center) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);

        // Add screens
        contentPanel.add(createHomePanel(), "homeCard");
        contentPanel.add(new AccountsPanel(), "accountsCard");
        contentPanel.add(new ExpensesPanel(), "transactionsCard");
        contentPanel.add(new BudgetsPanel(), "budgetsCard");
        contentPanel.add(new ReportsPanel(), "reportsCard");
        contentPanel.add(new SettingsPanel(), "settingsCard");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(new Color(220, 220, 230));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect for FlatLaf
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 15; hoverBackground: " + hex(SIDEBAR_BTN_HOVER));
        
        if (cardName != null) {
            btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        }
        return btn;
    }

    private JPanel createHomePanel() {
        JPanel home = new JPanel(new BorderLayout());
        home.setBackground(CONTENT_BG);
        home.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header: Welcome & Date
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 30, 47));
        
        JLabel subtitle = new JLabel("Here's your financial summary for today.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 120, 140));
        
        JPanel titleWrapper = new JPanel(new GridLayout(2, 1));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        headerPanel.add(titleWrapper, BorderLayout.WEST);

        // Add Quick Action Button
        JButton btnAddTransaction = new JButton("+ Add Transaction");
        btnAddTransaction.setBackground(new Color(67, 97, 238));
        btnAddTransaction.setForeground(Color.WHITE);
        btnAddTransaction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddTransaction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddTransaction.putClientProperty(FlatClientProperties.STYLE, "arc: 20; padding: 8,15,8,15");
        headerPanel.add(btnAddTransaction, BorderLayout.EAST);

        home.add(headerPanel, BorderLayout.NORTH);

        // Center Content Area: Widgets
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        // 1. Top Summary Cards (Grid)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        cardsPanel.add(createWidgetCard("Total Balance", "$12,450.00", new Color(67, 97, 238), "↗ +5.2%"));
        cardsPanel.add(createWidgetCard("Monthly Income", "$5,200.00", new Color(46, 204, 113), ""));
        cardsPanel.add(createWidgetCard("Monthly Expenses", "$1,850.50", new Color(231, 76, 60), ""));
        cardsPanel.add(createWidgetCard("Health Score", "85 / 100", new Color(155, 89, 182), "Excellent"));

        centerPanel.add(cardsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 2. Charts and Recent Activity Area
        JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        lowerPanel.setOpaque(false);

        // Actual JFreeChart Implementation (Phase 2)
        lowerPanel.add(createChartPanel());

        // Actual Recent Activity Implementation (Phase 3)
        lowerPanel.add(createActivityPanel());

        centerPanel.add(lowerPanel);

        home.add(centerPanel, BorderLayout.CENTER);
        return home;
    }

    private JPanel createWidgetCard(String title, String amount, Color accentColor, String badgeText) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title and Badge
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(100, 100, 120));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topPanel.add(lblTitle, BorderLayout.WEST);

        if (!badgeText.isEmpty()) {
            JLabel badge = new JLabel(badgeText);
            badge.setForeground(accentColor);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
            topPanel.add(badge, BorderLayout.EAST);
        }
        card.add(topPanel, BorderLayout.NORTH);

        // Amount
        JLabel lblAmount = new JLabel(amount);
        lblAmount.setForeground(new Color(30, 30, 47));
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(lblAmount, BorderLayout.CENTER);

        // Bottom Accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));
        card.add(accentLine, BorderLayout.SOUTH);

        return card;
    }

    private void handleLogout() {
        SessionManager.logout();
        new LoginView().setVisible(true);
        this.dispose();
    }
    
    // Helper to convert Color to Hex for FlatLaf
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private JPanel createChartPanel() {
        RoundedPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Expenses by Category");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (SessionManager.isLoggedIn()) {
            int userId = SessionManager.getCurrentUser().getId();
            List<Expense> expenses = new dao.ExpenseDAOImpl().getAllExpensesByUser(userId);
            
            // Group by categoryId and sum amounts
            Map<Integer, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategoryId, Collectors.summingDouble(Expense::getAmount)));
                
            // For simplicity in display without full CategoryDAO joins, we will just use Category ID or fetch name if needed
            dao.CategoryDAOImpl catDao = new dao.CategoryDAOImpl();
            List<model.Category> categories = catDao.getCategoriesByUserAndType(userId, "EXPENSE");
            
            for (Map.Entry<Integer, Double> entry : categoryTotals.entrySet()) {
                String catName = "Unknown";
                for (model.Category c : categories) {
                    if (c.getId() == entry.getKey()) {
                        catName = c.getName();
                        break;
                    }
                }
                dataset.setValue(catName, entry.getValue());
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "", 
                dataset, 
                true, 
                true, 
                false);
                
        // Style the chart
        chart.setBackgroundPaint(Color.WHITE);
        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowXOffset(0);
        plot.setShadowYOffset(0);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 200));
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createActivityPanel() {
        RoundedPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Recent Activity");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        if (SessionManager.isLoggedIn()) {
            TransactionService ts = new TransactionService();
            List<Transaction> recent = ts.getRecentTransactionsByUser(SessionManager.getCurrentUser().getId(), 5);
            
            if (recent.isEmpty()) {
                JLabel empty = new JLabel("No recent activity.");
                empty.setForeground(Color.GRAY);
                listPanel.add(empty);
            } else {
                for (Transaction t : recent) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setBackground(Color.WHITE);
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                    row.setBorder(new EmptyBorder(5, 5, 5, 5));
                    
                    String type = t instanceof Income ? "+" : "-";
                    Color amtColor = t instanceof Income ? new Color(46, 204, 113) : new Color(231, 76, 60);
                    
                    JLabel lblDesc = new JLabel(t.getDescription() != null && !t.getDescription().isEmpty() ? t.getDescription() : "Transaction");
                    lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    
                    JLabel lblDate = new JLabel(t.getDate().toString());
                    lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    lblDate.setForeground(Color.GRAY);
                    
                    JPanel leftSide = new JPanel(new GridLayout(2, 1));
                    leftSide.setBackground(Color.WHITE);
                    leftSide.add(lblDesc);
                    leftSide.add(lblDate);
                    
                    JLabel lblAmt = new JLabel(type + "$" + String.format("%.2f", t.getAmount()));
                    lblAmt.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    lblAmt.setForeground(amtColor);
                    
                    row.add(leftSide, BorderLayout.WEST);
                    row.add(lblAmt, BorderLayout.EAST);
                    
                    listPanel.add(row);
                    listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}
