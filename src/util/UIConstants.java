package util;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

/**
 * Classe que centraliza todas as constantes de UI do sistema
 * para garantir consistência visual e facilitar a manutenção.
 */
public class UIConstants {
    // Cores
    public static final Color BACKGROUND_COLOR = new Color(25,25,50); // Azul escuro do sistema - RGB(4,8,94)
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color HEADER_BORDER_COLOR = new Color(100, 100, 255);
    public static final Color HIGHLIGHT_TEXT_COLOR = new Color(200, 200, 255);
    
    // Cores dos botões
    public static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 136); // Verde
    public static final Color SECONDARY_BUTTON_COLOR = new Color(63, 81, 181); // Azul
    public static final Color DANGER_BUTTON_COLOR = new Color(211, 47, 47); // Vermelho
    public static final Color WARNING_BUTTON_COLOR = new Color(255, 152, 0); // Laranja
    public static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    
    // Fontes
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    
    // Padding e margens
    public static final int PADDING = 20;
    public static final EmptyBorder PANEL_PADDING = new EmptyBorder(PADDING, PADDING, PADDING, PADDING);
    
    /**
     * Configura as propriedades padrão de um painel
     * @param panel Painel a ser configurado
     */
    public static void setupPanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(PANEL_PADDING);
    }
    
    /**
     * Configura as propriedades de um botão primário (verde)
     * @param button Botão a ser configurado
     * @param text Texto do botão
     */
    public static void setupPrimaryButton(JButton button, String text) {
        button.setText(text);
        button.setBackground(PRIMARY_BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
    }
    
    /**
     * Configura as propriedades de um botão secundário (azul)
     * @param button Botão a ser configurado
     * @param text Texto do botão
     */
    public static void setupSecondaryButton(JButton button, String text) {
        button.setText(text);
        button.setBackground(SECONDARY_BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
    }
    
    /**
     * Configura as propriedades de um botão de perigo/cancelamento (vermelho)
     * @param button Botão a ser configurado
     * @param text Texto do botão
     */
    public static void setupDangerButton(JButton button, String text) {
        button.setText(text);
        button.setBackground(DANGER_BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
    }
    
    /**
     * Configura as propriedades de um botão de alerta (laranja)
     * @param button Botão a ser configurado
     * @param text Texto do botão
     */
    public static void setupWarningButton(JButton button, String text) {
        button.setText(text);
        button.setBackground(WARNING_BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
    }
    
    /**
     * Configura as propriedades de um rótulo de título
     * @param label Rótulo a ser configurado
     * @param text Texto do rótulo
     */
    public static void setupTitleLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    /**
     * Configura as propriedades de um rótulo de subtítulo
     * @param label Rótulo a ser configurado
     * @param text Texto do rótulo
     */
    public static void setupSubtitleLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    /**
     * Configura as propriedades de um rótulo normal
     * @param label Rótulo a ser configurado
     * @param text Texto do rótulo
     */
    public static void setupLabel(JLabel label, String text) {
        label.setText(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    /**
     * Configura as propriedades de um botão de rádio
     * @param radioButton Botão de rádio a ser configurado
     * @param text Texto do botão de rádio
     */
    public static void setupRadioButton(JRadioButton radioButton, String text) {
        radioButton.setText(text);
        radioButton.setFont(LABEL_FONT);
        radioButton.setForeground(TEXT_COLOR);
        radioButton.setBackground(BACKGROUND_COLOR);
    }
}