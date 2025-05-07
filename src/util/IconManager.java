package util;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Dimension;

/**
 * Classe utilitária para gerenciar ícones na aplicação
 */
public class IconManager {
    
    private static final String ICON_PATH = "src/gui/icons/";
    
    /**
     * Carrega um ícone a partir do caminho
     * @param iconName Nome do arquivo do ícone
     * @param width Largura desejada
     * @param height Altura desejada
     * @return ImageIcon redimensionado ou null se o ícone não existir
     */
    public static ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(ICON_PATH + iconName);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone: " + iconName + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Configura um botão apenas com ícone
     * @param button Botão a ser configurado
     * @param iconName Nome do arquivo do ícone
     * @param tooltip Texto de tooltip
     * @param size Tamanho do botão
     */
    public static void setupIconButton(JButton button, String iconName, String tooltip, int size) {
        ImageIcon icon = loadIcon(iconName, size - 10, size - 10);
        
        if (icon != null) {
            button.setIcon(icon);
        } else {
            // Fallback para texto se o ícone não estiver disponível
            button.setText(tooltip.substring(0, 1));
        }
        
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(size, size));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }
    
    /**
     * Lista de nomes de ícones padrão
     */
    public static final String ICON_MENU = "menu.png";
    public static final String ICON_PROFILE = "profile.png";
    public static final String ICON_LOGOUT = "logout.png";
    public static final String ICON_CUSTOMER = "customer.png";
    public static final String ICON_ADD = "add.png";
    public static final String ICON_LIST = "list.png";
    public static final String ICON_CALENDAR = "calendar.png";
    public static final String ICON_DASHBOARD = "dashboard.png";
}