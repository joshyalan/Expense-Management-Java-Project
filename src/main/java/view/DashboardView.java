package view;

import utils.SessionManager;
import utils.ui.RoundedPanel;
import utils.ui.Theme;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
 * Premium Dashboard GUI.
 */
public class DashboardView extends JFrame {

    private JPanel sidebarPanel;
    private JPanel mainContainer;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String currentCard;

    public DashboardView() {
        this("homeCard");
    }

    public DashboardView(String initialCard) {
        this.currentCard = initialCard;
        initFrame();
        initComponents();
        cardLayout.show(contentPanel, currentCard);
        
        // Only show welcome on home load for the first time
        if (initialCard.equals("homeCard") && !Theme.isDarkMode) {
            SwingUtilities.invokeLater(() -> {
                String username = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getUsername() : "User";
                // utils.ui.ToastNotification.show(this, "Welcome back, " + username + "!", utils.ui.ToastNotification.SUCCESS);
            });
        }
    }

    private void initFrame() {
        setTitle("ExpenseTracker Pro");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.getBgApp());
        
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, Theme.getBgApp());
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Theme.getTextPrimary());
        // For Mac-style unified title bar
        getRootPane().putClientProperty("JRootPane.titleBarShowIcon", false);
    }

    private void initComponents() {
        // --- 1. Sidebar (West) ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBackground(Theme.getSidebarBg());
        sidebarPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.getBorder()),
            new EmptyBorder(24, 16, 24, 16)
        ));

        // Logo
        JLabel lblLogo = new JLabel("ExpenseTracker", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(Theme.fontHeading2());
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(10, 0, 40, 0));
        sidebarPanel.add(lblLogo);

        // Sidebar Buttons
        sidebarPanel.add(createSidebarButton("Dashboard", "homeCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Accounts", "accountsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Transactions", "transactionsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Budgets", "budgetsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Savings Goals", "goalsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Reports", "reportsCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Categories", "categoriesCard"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(createSidebarButton("Settings", "settingsCard"));
        
        sidebarPanel.add(Box.createVerticalGlue());

        // User Info & Logout
        String username = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getUsername() : "User";
        JLabel lblUser = new JLabel("👤 " + username);
        lblUser.setForeground(new Color(200, 200, 200));
        lblUser.setFont(Theme.fontBodyBold());
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUser.setBorder(new EmptyBorder(0, 0, 16, 0));
        sidebarPanel.add(lblUser);

        JButton btnLogout = createSidebarButton("Logout", null);
        btnLogout.addActionListener(e -> handleLogout());
        sidebarPanel.add(btnLogout);

        add(sidebarPanel, BorderLayout.WEST);

        // --- Main Container (Center) ---
        mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Theme.getBgApp());
        
        // Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.getBgApp());
        headerPanel.setBorder(new EmptyBorder(32, 40, 16, 40));
        
        JLabel lblHeaderTitle = new JLabel("Dashboard");
        lblHeaderTitle.setFont(Theme.fontHeading1());
        lblHeaderTitle.setForeground(Theme.getTextPrimary());
        headerPanel.add(lblHeaderTitle, BorderLayout.WEST);
        
        // Right side of header (Theme Toggle + Add Transaction)
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        headerRight.setOpaque(false);
        
        JButton btnThemeToggle = new JButton(Theme.isDarkMode ? "☀️ Light Mode" : "🌙 Dark Mode");
        btnThemeToggle.setFont(Theme.fontBodyBold());
        btnThemeToggle.putClientProperty(FlatClientProperties.STYLE, 
            "background: $Panel.background; foreground: $Label.foreground; arc: 15; padding: 8,16,8,16; borderWidth: 1");
        btnThemeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemeToggle.addActionListener(e -> {
            Theme.toggleTheme(this);
            new DashboardView(currentCard).setVisible(true);
            this.dispose();
        });
        
        JButton btnAdd = new JButton("+ Add Transaction");
        btnAdd.setFont(Theme.fontBodyBold());
        btnAdd.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 15; padding: 8,16,8,16; borderWidth: 0");
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {
            new TransactionDialog(this, () -> {
                // Refresh dashboard to show new data
                new DashboardView(currentCard).setVisible(true);
                this.dispose();
            }).setVisible(true);
        });
        
        headerRight.add(btnThemeToggle);
        headerRight.add(btnAdd);
        headerPanel.add(headerRight, BorderLayout.EAST);
        
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.getBgApp());
        contentPanel.setBorder(new EmptyBorder(0, 40, 40, 40));

        contentPanel.add(createHomePanel(), "homeCard");
        contentPanel.add(new AccountsPanel(), "accountsCard");
        contentPanel.add(new ExpensesPanel(), "transactionsCard");
        contentPanel.add(new BudgetsPanel(), "budgetsCard");
        contentPanel.add(new GoalsPanel(), "goalsCard");
        contentPanel.add(new ReportsPanel(), "reportsCard");
        contentPanel.add(new CategoriesPanel(), "categoriesCard");
        contentPanel.add(new SettingsPanel(), "settingsCard");

        mainContainer.add(contentPanel, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBackground(Theme.getSidebarBg());
        btn.setForeground(new Color(220, 220, 230));
        btn.setFont(Theme.fontBody());
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 12; hoverBackground: " + hex(Theme.getSidebarBtnHover()) + "; borderWidth: 0");
        
        if (cardName != null) {
            btn.addActionListener(e -> {
                currentCard = cardName;
                cardLayout.show(contentPanel, cardName);
                // Update header title based on card
                Component[] comps = headerPanel.getComponents();
                for (Component c : comps) {
                    if (c instanceof JLabel) {
                        ((JLabel)c).setText(text);
                    }
                }
            });
        }
        return btn;
    }

    private JPanel createHomePanel() {
        JPanel home = new JPanel(new BorderLayout());
        home.setBackground(Theme.getBgApp());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // 1. Summary Cards (Grid)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 24, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        double totalBalance = 0;
        double monthlyIncome = 0;
        double monthlyExpenses = 0;
        int healthScore = 0;
        
        if (SessionManager.isLoggedIn()) {
            int userId = SessionManager.getCurrentUser().getId();
            
            // Total Balance
            java.util.List<model.Account> accounts = new dao.AccountDAOImpl().getAccountsByUser(userId);
            totalBalance = accounts.stream().mapToDouble(model.Account::getBalance).sum();
            
            java.time.LocalDate now = java.time.LocalDate.now();
            
            // Monthly Income
            java.util.List<model.Income> incomes = new dao.IncomeDAOImpl().getAllIncomeByUser(userId);
            monthlyIncome = incomes.stream()
                .filter(i -> {
                    java.time.LocalDate d = i.getDate().toLocalDate();
                    return d.getMonthValue() == now.getMonthValue() && d.getYear() == now.getYear();
                })
                .mapToDouble(model.Income::getAmount)
                .sum();
                
            // Monthly Expenses
            java.util.List<model.Expense> expenses = new dao.ExpenseDAOImpl().getAllExpensesByUser(userId);
            monthlyExpenses = expenses.stream()
                .filter(e -> {
                    java.time.LocalDate d = e.getDate().toLocalDate();
                    return d.getMonthValue() == now.getMonthValue() && d.getYear() == now.getYear();
                })
                .mapToDouble(model.Expense::getAmount)
                .sum();
                
            // Health Score
            if (monthlyIncome > 0) {
                double ratio = monthlyExpenses / monthlyIncome;
                if (ratio > 1.0) ratio = 1.0;
                healthScore = (int) ((1.0 - ratio) * 100);
            } else if (monthlyExpenses == 0) {
                healthScore = 100;
            } else {
                healthScore = 0;
            }
        }
        
        String balanceStr = "$" + String.format("%,.2f", totalBalance);
        String incomeStr = "$" + String.format("%,.2f", monthlyIncome);
        String expenseStr = "$" + String.format("%,.2f", monthlyExpenses);
        String healthStr = healthScore + " / 100";
        String healthStatus = healthScore >= 80 ? "Excellent" : (healthScore >= 50 ? "Good" : "Needs Work");

        cardsPanel.add(createWidgetCard("Total Balance", balanceStr, Theme.PRIMARY, ""));
        cardsPanel.add(createWidgetCard("Monthly Income", incomeStr, Theme.SUCCESS, ""));
        cardsPanel.add(createWidgetCard("Monthly Expenses", expenseStr, Theme.DANGER, ""));
        cardsPanel.add(createWidgetCard("Health Score", healthStr, Theme.WARNING, healthStatus));

        centerPanel.add(cardsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // 2. Charts, Recent Activity, and AI Insights
        JPanel lowerPanel = new JPanel(new GridLayout(1, 3, 24, 0));
        lowerPanel.setOpaque(false);

        lowerPanel.add(createChartPanel());
        lowerPanel.add(createActivityPanel());
        lowerPanel.add(createInsightsPanel());

        centerPanel.add(lowerPanel);
        home.add(centerPanel, BorderLayout.CENTER);
        return home;
    }
    
    private JPanel createInsightsPanel() {
        RoundedPanel panel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        
        JLabel title = new JLabel("✨ AI Insights");
        title.setFont(Theme.fontHeading3());
        title.setForeground(Theme.PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.getBgPanel());

        List<String> insights = new java.util.ArrayList<>();
        if (SessionManager.isLoggedIn()) {
            service.AnalyticsService as = new service.AnalyticsService();
            insights = as.generateInsights(SessionManager.getCurrentUser().getId());
        }
        
        for (String text : insights) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setBackground(Theme.getBgPanel());
            row.setBorder(new EmptyBorder(8, 0, 8, 0));
            
            JLabel icon = new JLabel("💡");
            JLabel lbl = new JLabel(text);
            lbl.setFont(Theme.fontBody());
            lbl.setForeground(Theme.getTextPrimary());
            
            row.add(icon, BorderLayout.WEST);
            row.add(lbl, BorderLayout.CENTER);
            content.add(row);
            
            JPanel sep = new JPanel();
            sep.setBackground(Theme.getBorder());
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            content.add(sep);
        }

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createWidgetCard(String title, String amount, Color accentColor, String badgeText) {
        RoundedPanel card = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        
        // Add subtle hover effect (Micro-interaction)
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 1),
                    new EmptyBorder(23, 23, 23, 23)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(24, 24, 24, 24));
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Theme.getTextMuted());
        lblTitle.setFont(Theme.fontBodyBold());
        topPanel.add(lblTitle, BorderLayout.WEST);

        if (!badgeText.isEmpty()) {
            JLabel badge = new JLabel(badgeText);
            badge.setForeground(accentColor);
            badge.setFont(Theme.fontSmall());
            topPanel.add(badge, BorderLayout.EAST);
        }
        card.add(topPanel, BorderLayout.NORTH);

        JLabel lblAmount = new JLabel(amount);
        lblAmount.setForeground(Theme.getTextPrimary());
        lblAmount.setFont(Theme.fontHero());
        card.add(lblAmount, BorderLayout.CENTER);

        return card;
    }

    private void handleLogout() {
        SessionManager.logout();
        new LoginView().setVisible(true);
        this.dispose();
    }
    
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private JPanel createChartPanel() {
        RoundedPanel panel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        
        JLabel title = new JLabel("Expenses by Category");
        title.setFont(Theme.fontHeading3());
        title.setForeground(Theme.getTextPrimary());
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(title, BorderLayout.NORTH);

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (SessionManager.isLoggedIn()) {
            int userId = SessionManager.getCurrentUser().getId();
            List<Expense> expenses = new dao.ExpenseDAOImpl().getAllExpensesByUser(userId);
            
            Map<Integer, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategoryId, Collectors.summingDouble(Expense::getAmount)));
                
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

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        chart.setBackgroundPaint(Theme.getBgPanel());
        
        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Theme.getBgPanel());
        plot.setOutlineVisible(false);
        plot.setShadowXOffset(0);
        plot.setShadowYOffset(0);
        plot.setLabelBackgroundPaint(Theme.getBgPanel());
        plot.setLabelPaint(Theme.getTextPrimary());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 250));
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createActivityPanel() {
        RoundedPanel panel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        
        JLabel title = new JLabel("Recent Activity");
        title.setFont(Theme.fontHeading3());
        title.setForeground(Theme.getTextPrimary());
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.getBgPanel());

        if (SessionManager.isLoggedIn()) {
            TransactionService ts = new TransactionService();
            List<Transaction> recent = ts.getRecentTransactionsByUser(SessionManager.getCurrentUser().getId(), 5);
            
            if (recent.isEmpty()) {
                JLabel empty = new JLabel("No recent activity.");
                empty.setForeground(Theme.getTextMuted());
                listPanel.add(empty);
            } else {
                for (Transaction t : recent) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setBackground(Theme.getBgPanel());
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
                    row.setBorder(new EmptyBorder(8, 0, 8, 0));
                    
                    String type = t instanceof Income ? "+" : "-";
                    Color amtColor = t instanceof Income ? Theme.SUCCESS : Theme.DANGER;
                    
                    JLabel lblDesc = new JLabel(t.getDescription() != null && !t.getDescription().isEmpty() ? t.getDescription() : "Transaction");
                    lblDesc.setFont(Theme.fontBodyBold());
                    lblDesc.setForeground(Theme.getTextPrimary());
                    
                    JLabel lblDate = new JLabel(t.getDate().toString() + (t.getTime() != null ? " " + t.getTime().toString() : ""));
                    lblDate.setFont(Theme.fontSmall());
                    lblDate.setForeground(Theme.getTextMuted());
                    
                    JPanel leftSide = new JPanel(new GridLayout(2, 1));
                    leftSide.setBackground(Theme.getBgPanel());
                    leftSide.add(lblDesc);
                    leftSide.add(lblDate);
                    
                    JLabel lblAmt = new JLabel(type + "$" + String.format("%.2f", t.getAmount()));
                    lblAmt.setFont(Theme.fontBodyBold());
                    lblAmt.setForeground(amtColor);
                    
                    row.add(leftSide, BorderLayout.WEST);
                    row.add(lblAmt, BorderLayout.EAST);
                    
                    listPanel.add(row);
                    
                    // Add separator
                    JPanel sep = new JPanel();
                    sep.setBackground(Theme.getBorder());
                    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    listPanel.add(sep);
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
