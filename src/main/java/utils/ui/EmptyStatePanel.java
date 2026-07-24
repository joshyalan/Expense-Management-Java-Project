package utils.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmptyStatePanel extends JPanel {

    public EmptyStatePanel(String svgIconName, String titleText, String subtitleText, String buttonText, Runnable buttonAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.getBgPanel());
        setOpaque(true);
        setBorder(new EmptyBorder(40, 24, 40, 24));

        add(Box.createVerticalGlue());

        if (svgIconName != null && !svgIconName.isEmpty()) {
            FlatSVGIcon icon = new FlatSVGIcon("resources/icons/" + svgIconName, 80, 80);
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Theme.getTextMuted()));
            JLabel lblIcon = new JLabel(icon);
            lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(lblIcon);
            add(Box.createRigidArea(new Dimension(0, 16)));
        }

        JLabel lblTitle = new JLabel(titleText);
        lblTitle.setFont(Theme.fontHeading2());
        lblTitle.setForeground(Theme.getTextPrimary());
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblTitle);

        add(Box.createRigidArea(new Dimension(0, 8)));

        JTextArea txtDesc = new JTextArea(subtitleText);
        txtDesc.setFont(Theme.fontBody());
        txtDesc.setForeground(Theme.getTextMuted());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setOpaque(false);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtDesc.setMaximumSize(new Dimension(400, 100));
        add(txtDesc);

        if (buttonText != null && buttonAction != null) {
            add(Box.createRigidArea(new Dimension(0, 24)));
            JButton btnAction = new JButton(buttonText);
            btnAction.setFont(Theme.fontBodyBold());
            btnAction.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #" + Integer.toHexString(Theme.PRIMARY.getRGB()).substring(2) + "; foreground: #ffffff; padding: 8,24,8,24; borderWidth: 0");
            btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAction.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnAction.addActionListener(e -> buttonAction.run());
            add(btnAction);
        }

        add(Box.createVerticalGlue());
    }
}
