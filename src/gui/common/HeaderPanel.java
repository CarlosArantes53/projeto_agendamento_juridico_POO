package gui.common;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.util.IconManager;
import gui.util.UIConstants;

/**
 * Painel reutilizável para cabeçalhos das telas
 */
public class HeaderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JLabel lblTitle;
    private JPanel btnPanel;
    
    public HeaderPanel(String title) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Title section
        lblTitle = new JLabel(title);
        lblTitle.setForeground(UIConstants.TEXT_COLOR);
        lblTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // Button section
        btnPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        add(lblTitle, BorderLayout.WEST);
        add(btnPanel, BorderLayout.EAST);
    }
    
    /**
     * Adiciona um botão com ícone ao painel de botões
     */
    public JButton addButton(String text, String iconName, ActionListener listener) {
        JButton button = new JButton(text);
        UIConstants.setupPrimaryButton(button, text);
        
        // Add icon if available
        ImageIcon icon = IconManager.loadIcon(iconName, 16, 16);
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addActionListener(listener);
        btnPanel.add(button);
        
        return button;
    }
    
    /**
     * Altera o título do painel
     */
    public void setTitle(String title) {
        lblTitle.setText(title);
    }
}