package gui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class IconManager {
    
    private static final String ICON_PATH = "src/gui/images/icons/";
    
    // Constantes para os ícones
    public static final String ICON_MENU = "menu.png";
    public static final String ICON_PROFILE = "profile.png";
    public static final String ICON_LOGOUT = "logout.png";
    public static final String ICON_CUSTOMER = "customer.png";
    public static final String ICON_ADD = "add.png";
    public static final String ICON_ADD_W = "add_w.png";
    public static final String ICON_LIST = "list.png";
    public static final String ICON_CALENDAR = "calendar.png";
    public static final String ICON_CALENDAR_B = "b_calendario.png";
    public static final String ICON_DASHBOARD = "dashboard.png";
    public static final String ICON_EDIT = "edit.png";
    public static final String ICON_DELETE = "delete.png";
    public static final String ICON_SAVE = "save.png";
    public static final String ICON_CANCEL = "cancel.png";
    public static final String ICON_SEARCH = "search.png";
    public static final String ICON_REFRESH = "refresh.png";
    
    
    /**
     * Carrega um ícone com melhor qualidade redimensionado para o tamanho especificado.
     * 
     * @param iconName Nome do arquivo do ícone
     * @param width Largura desejada
     * @param height Altura desejada
     * @return ImageIcon redimensionado com alta qualidade
     */
    public static ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            // Tenta carregar a imagem como BufferedImage para melhor controle de qualidade
            File imageFile = new File(ICON_PATH + iconName);
            if (imageFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imageFile);
                
                // Se a imagem já tem o tamanho desejado, não redimensione
                if (originalImage.getWidth() == width && originalImage.getHeight() == height) {
                    return new ImageIcon(originalImage);
                }
                
                // Criar uma nova imagem com melhor qualidade
                BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                
                // Obter o objeto Graphics2D da nova imagem e configurar para melhor qualidade
                java.awt.Graphics2D g2d = resizedImage.createGraphics();
                
                // Configurações para melhor qualidade de redimensionamento
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, 
                        java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, 
                        java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Desenhar a imagem redimensionada com alta qualidade
                g2d.drawImage(originalImage, 0, 0, width, height, null);
                g2d.dispose();
                
                return new ImageIcon(resizedImage);
            } else {
                System.err.println("Arquivo de imagem não encontrado: " + ICON_PATH + iconName);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone: " + iconName + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Carrega um ícone com tamanho padrão de 16x16.
     */
    public static ImageIcon loadIcon(String iconName) {
        return loadIcon(iconName, 16, 16);
    }
    
    /**
     * Configura um botão com um ícone.
     */
    public static void setupIconButton(JButton button, String iconName, String tooltip, int size) {
        ImageIcon icon = loadIcon(iconName, size - 10, size - 10);
        
        if (icon != null) {
            button.setIcon(icon);
        } else {
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
     * Define o ícone para um componente JComponent.
     */
    public static boolean setComponentIcon(JComponent component, String iconName, int width, int height) {
        ImageIcon icon = loadIcon(iconName, width, height);
        
        if (icon != null) {
            if (component instanceof JButton) {
                ((JButton) component).setIcon(icon);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Define o ícone para um botão.
     */
    public static boolean setButtonIcon(Component button, String iconName, int width, int height) {
        ImageIcon icon = loadIcon(iconName, width, height);
        
        if (icon != null && button instanceof AbstractButton) {
            AbstractButton abstractButton = (AbstractButton) button;
            abstractButton.setIcon(icon);
            return true;
        }
        
        return false;
    }
    
    /**
     * Define o ícone para um botão com tamanho padrão.
     */
    public static boolean setButtonIcon(JButton button, String iconName) {
        return setButtonIcon(button, iconName, 16, 16);
    }
    
    /**
     * Carrega uma imagem maior, como o logo, com alta qualidade.
     */
    public static ImageIcon loadLogo(String imagePath, int width, int height) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imageFile);
                
                // Criar uma nova imagem com melhor qualidade
                BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                
                // Obter o objeto Graphics2D da nova imagem e configurar para melhor qualidade
                java.awt.Graphics2D g2d = resizedImage.createGraphics();
                
                // Configurações para melhor qualidade de redimensionamento
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, 
                        java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, 
                        java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Desenhar a imagem redimensionada com alta qualidade
                g2d.drawImage(originalImage, 0, 0, width, height, null);
                g2d.dispose();
                
                return new ImageIcon(resizedImage);
            } else {
                System.err.println("Arquivo de imagem não encontrado: " + imagePath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem: " + imagePath + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}