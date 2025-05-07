package gui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class IconManager {
    
    private static final String ICON_PATH = "src/gui/images/icons/";
    
    public static final String ICON_MENU = "menu.png";
    public static final String ICON_PROFILE = "profile.png";
    public static final String ICON_LOGOUT = "logout.png";
    public static final String ICON_CUSTOMER = "customer.png";
    public static final String ICON_ADD = "add.png";
    public static final String ICON_LIST = "list.png";
    public static final String ICON_CALENDAR = "calendar.png";
    public static final String ICON_DASHBOARD = "dashboard.png";
    public static final String ICON_EDIT = "edit.png";
    public static final String ICON_DELETE = "delete.png";
    public static final String ICON_SAVE = "save.png";
    public static final String ICON_CANCEL = "cancel.png";
    public static final String ICON_SEARCH = "search.png";
    public static final String ICON_REFRESH = "refresh.png";
    
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
    
    public static ImageIcon loadIcon(String iconName) {
        return loadIcon(iconName, 16, 16);
    }
    
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
    
    public static boolean setButtonIcon(Component button, String iconName, int width, int height) {
        ImageIcon icon = loadIcon(iconName, width, height);
        
        if (icon != null && button instanceof AbstractButton) {
            AbstractButton abstractButton = (AbstractButton) button;
            abstractButton.setIcon(icon);
            return true;
        }
        
        return false;
    }
    
    public static boolean setButtonIcon(JButton button, String iconName) {
        return setButtonIcon(button, iconName, 16, 16);
    }
}