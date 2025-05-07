package gui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import util.IconManager;
import util.UIConstants;

public class TableActionCellRenderer implements TableCellRenderer {
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    
    public TableActionCellRenderer() {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        panel.setOpaque(true);
        
        btnEdit = new JButton();
        btnDelete = new JButton();
        
        IconManager.setupIconButton(btnEdit, "edit.png", "Editar", 28);
        IconManager.setupIconButton(btnDelete, "delete.png", "Excluir", 28);
        
        if (btnEdit.getIcon() == null) {
            UIConstants.setupIconButton(btnEdit, "Editar");
            btnEdit.setText("✎");
            btnEdit.setForeground(UIConstants.SECONDARY_BUTTON_COLOR);
        }
        
        if (btnDelete.getIcon() == null) {
            UIConstants.setupIconButton(btnDelete, "Excluir");
            btnDelete.setText("×");
            btnDelete.setForeground(UIConstants.DANGER_BUTTON_COLOR);
        }
        
        btnEdit.setFocusable(false);
        btnDelete.setFocusable(false);
        
        panel.add(btnEdit);
        panel.add(btnDelete);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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