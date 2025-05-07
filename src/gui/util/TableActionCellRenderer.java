package gui.util;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableActionCellRenderer implements TableCellRenderer {
    
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnView;
    
    public TableActionCellRenderer() {
        this(true, true, false);
    }
    
    public TableActionCellRenderer(boolean showEdit, boolean showDelete, boolean showView) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        panel.setOpaque(true);
        
        if (showEdit) {
            btnEdit = createButton("edit.png", "Editar", UIConstants.SECONDARY_BUTTON_COLOR);
            panel.add(btnEdit);
        }
        
        if (showView) {
            btnView = createButton("view.png", "Visualizar", UIConstants.PRIMARY_BUTTON_COLOR);
            panel.add(btnView);
        }
        
        if (showDelete) {
            btnDelete = createButton("delete.png", "Excluir", UIConstants.DANGER_BUTTON_COLOR);
            panel.add(btnDelete);
        }
    }
    
    private JButton createButton(String iconName, String tooltip, java.awt.Color fallbackColor) {
        JButton button = new JButton();
        IconManager.setupIconButton(button, iconName, tooltip, 28);
        
        if (button.getIcon() == null) {
            UIConstants.setupIconButton(button, tooltip);
            
            if (tooltip.equals("Editar")) {
                button.setText("✎");
            } else if (tooltip.equals("Excluir")) {
                button.setText("×");
            } else if (tooltip.equals("Visualizar")) {
                button.setText("👁");
            }
            
            button.setForeground(fallbackColor);
        }
        
        button.setFocusable(false);
        
        return button;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (isSelected) {
            panel.setBackground(UIConstants.ROW_SELECTED);
        } else {
            if (row % 2 == 0) {
                panel.setBackground(UIConstants.ROW_EVEN);
            } else {
                panel.setBackground(UIConstants.ROW_ODD);
            }
        }
        
        return panel;
    }
}