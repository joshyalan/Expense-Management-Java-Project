package view;

import dao.GoalDAOImpl;
import model.Goal;
import utils.SessionManager;
import utils.ui.RoundedPanel;
import utils.ui.Theme;
import utils.ui.ToastNotification;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class GoalsPanel extends JPanel {

    private GoalDAOImpl goalDAO = new GoalDAOImpl();
    private JPanel listPanel;

    public GoalsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        initComponents();
        loadGoals();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel("Savings Goals");
        title.setFont(Theme.fontHeading2());
        title.setForeground(Theme.getTextPrimary());
        headerPanel.add(title, BorderLayout.WEST);

        JButton btnAdd = new JButton("+ New Goal");
        btnAdd.setFont(Theme.fontBodyBold());
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; padding: 8,16,8,16; borderWidth: 0");
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showAddGoalDialog());
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(btnAdd);
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.getBgApp());
        
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Theme.getBgApp());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadGoals() {
        listPanel.removeAll();
        if (SessionManager.isLoggedIn()) {
            List<Goal> goals = goalDAO.getGoalsByUser(SessionManager.getCurrentUser().getId());
            if (goals.isEmpty()) {
                JLabel lblEmpty = new JLabel("No goals created yet. Start saving!");
                lblEmpty.setFont(Theme.fontBody());
                lblEmpty.setForeground(Theme.getTextMuted());
                lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(Box.createRigidArea(new Dimension(0, 40)));
                listPanel.add(lblEmpty);
            } else {
                for (Goal g : goals) {
                    listPanel.add(createGoalCard(g));
                    listPanel.add(Box.createRigidArea(new Dimension(0, 16)));
                }
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createGoalCard(Goal g) {
        RoundedPanel card = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        JLabel lblName = new JLabel(g.getName());
        lblName.setFont(Theme.fontHeading3());
        lblName.setForeground(Theme.getTextPrimary());
        top.add(lblName, BorderLayout.WEST);
        
        JLabel lblTargetDate = new JLabel("Target: " + (g.getTargetDate() != null ? g.getTargetDate().toString() : "N/A"));
        lblTargetDate.setFont(Theme.fontSmall());
        lblTargetDate.setForeground(Theme.getTextMuted());
        top.add(lblTargetDate, BorderLayout.EAST);
        
        card.add(top, BorderLayout.NORTH);
        
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(16, 0, 16, 0));
        
        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue((int) g.getProgressPercentage());
        progress.setStringPainted(false);
        progress.putClientProperty(FlatClientProperties.STYLE, "arc: 12; foreground: " + hex(Theme.SUCCESS));
        progress.setPreferredSize(new Dimension(0, 12));
        center.add(progress, BorderLayout.CENTER);
        
        JPanel progressLabels = new JPanel(new BorderLayout());
        progressLabels.setOpaque(false);
        progressLabels.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        JLabel lblCurrent = new JLabel("$" + String.format("%.2f", g.getCurrentAmount()));
        lblCurrent.setFont(Theme.fontBodyBold());
        lblCurrent.setForeground(Theme.SUCCESS);
        progressLabels.add(lblCurrent, BorderLayout.WEST);
        
        JLabel lblTarget = new JLabel("of $" + String.format("%.2f", g.getTargetAmount()));
        lblTarget.setFont(Theme.fontBody());
        lblTarget.setForeground(Theme.getTextMuted());
        progressLabels.add(lblTarget, BorderLayout.EAST);
        
        center.add(progressLabels, BorderLayout.SOUTH);
        card.add(center, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bottom.setOpaque(false);
        
        JButton btnUpdate = new JButton("Update Amount");
        btnUpdate.setFont(Theme.fontSmall());
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, "buttonType: borderless; foreground: " + hex(Theme.PRIMARY));
        btnUpdate.addActionListener(e -> updateGoal(g));
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(Theme.fontSmall());
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "buttonType: borderless; foreground: " + hex(Theme.DANGER));
        btnDelete.addActionListener(e -> deleteGoal(g));
        
        bottom.add(btnUpdate);
        bottom.add(btnDelete);
        
        card.add(bottom, BorderLayout.SOUTH);
        
        return card;
    }

    private void updateGoal(Goal g) {
        String input = JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor(this), "Enter new current amount:", g.getCurrentAmount());
        if (input != null) {
            try {
                double newAmt = Double.parseDouble(input);
                if (goalDAO.updateGoalCurrentAmount(g.getId(), newAmt)) {
                    loadGoals();
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Goal updated successfully!", ToastNotification.SUCCESS);
                }
            } catch (NumberFormatException e) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Invalid amount.", ToastNotification.ERROR);
            }
        }
    }
    
    private void deleteGoal(Goal g) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this goal?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (goalDAO.deleteGoal(g.getId())) {
                loadGoals();
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Goal deleted.", ToastNotification.SUCCESS);
            }
        }
    }

    private void showAddGoalDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "New Goal", true);
        dialog.setSize(400, 350);
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
        txtName.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        formPanel.add(createLabeledField("Goal Name", txtName), gbc);
        
        gbc.gridy++;
        JTextField txtTarget = new JTextField();
        txtTarget.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        formPanel.add(createLabeledField("Target Amount ($)", txtTarget), gbc);
        
        gbc.gridy++;
        JTextField txtDate = new JTextField(LocalDate.now().plusMonths(6).toString());
        txtDate.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        formPanel.add(createLabeledField("Target Date (YYYY-MM-DD)", txtDate), gbc);

        gbc.gridy++;
        JButton btnSave = new JButton("Save Goal");
        btnSave.setFont(Theme.fontBodyBold());
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; arc: 12; padding: 8,16,8,16; borderWidth: 0");
        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText();
                double target = Double.parseDouble(txtTarget.getText());
                Date date = Date.valueOf(txtDate.getText());
                
                Goal g = new Goal(SessionManager.getCurrentUser().getId(), name, target, 0.0, date);
                if (goalDAO.addGoal(g)) {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Goal added!", ToastNotification.SUCCESS);
                    loadGoals();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Please check your inputs.", ToastNotification.ERROR);
            }
        });
        formPanel.add(btnSave, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);
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
    
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
