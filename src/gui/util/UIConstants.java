package gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class UIConstants {
    
    public static final Color PRIMARY_DARK = new Color(25, 25, 55);
    public static final Color PRIMARY = new Color(45, 45, 80);
    public static final Color PRIMARY_LIGHT = new Color(65, 65, 110);
    public static final Color ACCENT = new Color(0, 150, 136);
    public static final Color ACCENT_LIGHT = new Color(0, 170, 156);
    public static final Color ACCENT_DARK = new Color(0, 130, 116);
    
    public static final Color BACKGROUND_COLOR = PRIMARY_DARK;
    public static final Color PANEL_BACKGROUND = PRIMARY;
    public static final Color HEADER_COLOR = PRIMARY_DARK;
    public static final Color MENU_BACKGROUND = new Color(20, 20, 40);
    public static final Color WELCOME_PANEL_BG = new Color(0, 110, 100);
    public static final Color ROW_ODD = new Color(35, 35, 70);
    public static final Color ROW_EVEN = new Color(45, 45, 80);
    public static final Color ROW_SELECTED = new Color(60, 60, 100);
    
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color TEXT_LIGHT = new Color(220, 220, 240);
    public static final Color TEXT_SECONDARY = new Color(180, 180, 210);
    public static final Color HEADER_TEXT_COLOR = Color.BLACK;
    public static final Color HIGHLIGHT_TEXT_COLOR = new Color(240, 240, 250);
    public static final Color MENU_HEADER_TEXT = new Color(150, 150, 200);
    
    public static final Color HEADER_BORDER_COLOR = new Color(70, 70, 120);
    public static final Color TABLE_GRID_COLOR = new Color(60, 60, 100);
    public static final Color PANEL_BORDER_COLOR = new Color(60, 60, 100);
    
    public static final Color PRIMARY_BUTTON_COLOR = ACCENT;
    public static final Color PRIMARY_BUTTON_HOVER = ACCENT_LIGHT;
    public static final Color SECONDARY_BUTTON_COLOR = new Color(70, 80, 170);
    public static final Color SECONDARY_BUTTON_HOVER = new Color(80, 90, 190);
    public static final Color DANGER_BUTTON_COLOR = new Color(180, 50, 50);
    public static final Color DANGER_BUTTON_HOVER = new Color(200, 60, 60);
    public static final Color WARNING_BUTTON_COLOR = new Color(230, 140, 0);
    public static final Color WARNING_BUTTON_HOVER = new Color(250, 160, 0);
    public static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font MENU_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font MENU_ITEM_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    public static final int PADDING = 15;
    public static final EmptyBorder PANEL_PADDING = new EmptyBorder(PADDING, PADDING, PADDING, PADDING);
    public static final EmptyBorder CONTENT_PADDING = new EmptyBorder(10, 10, 10, 10);
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 1),
        new EmptyBorder(10, 10, 10, 10)
    );
    
    public static final Dimension BUTTON_DIMENSION = new Dimension(120, 32);
    public static final Dimension ICON_BUTTON_DIMENSION = new Dimension(32, 32);
    public static final Dimension MENU_WIDTH = new Dimension(220, 0);
    public static final int TABLE_ROW_HEIGHT = 40;
    
    public static final Color TABLE_HEADER_BG = PRIMARY_DARK;
    public static final Color TABLE_HEADER_FG = Color.BLACK;
    
    public static void setupPanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(PANEL_PADDING);
    }
    
    public static void setupContentPanel(JPanel panel) {
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(PANEL_BORDER);
    }
    
    public static void setupPrimaryButton(JButton button, String text) {
        setupButton(button, text, PRIMARY_BUTTON_COLOR, PRIMARY_BUTTON_HOVER);
    }
    
    public static void setupSecondaryButton(JButton button, String text) {
        setupButton(button, text, SECONDARY_BUTTON_COLOR, SECONDARY_BUTTON_HOVER);
    }
    
    public static void setupDangerButton(JButton button, String text) {
        setupButton(button, text, DANGER_BUTTON_COLOR, DANGER_BUTTON_HOVER);
    }
    
    public static void setupWarningButton(JButton button, String text) {
        setupButton(button, text, WARNING_BUTTON_COLOR, WARNING_BUTTON_HOVER);
    }
    
    private static void setupButton(JButton button, String text, Color bgColor, Color hoverColor) {
        button.setText(text);
        button.setBackground(bgColor);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setPreferredSize(BUTTON_DIMENSION);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    public static void setupIconButton(JButton button, String tooltip) {
        button.setToolTipText(tooltip);
        button.setBackground(PANEL_BACKGROUND);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(ICON_BUTTON_DIMENSION);
    }
    
    public static void setupTitleLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    public static void setupSubtitleLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    public static void setupLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    public static void setupSecondaryLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_SECONDARY);
    }
    
    public static void setupRadioButton(JRadioButton radioButton, String text) {
        radioButton.setText(text);
        radioButton.setFont(LABEL_FONT);
        radioButton.setForeground(TEXT_COLOR);
        radioButton.setBackground(BACKGROUND_COLOR);
    }
    
    public static void setupTable(JTable table) {
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setFont(TABLE_FONT);
        table.setBackground(PANEL_BACKGROUND);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFillsViewportHeight(true);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setFont(TABLE_HEADER_FONT);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createLineBorder(HEADER_BORDER_COLOR, 1));
    }
}