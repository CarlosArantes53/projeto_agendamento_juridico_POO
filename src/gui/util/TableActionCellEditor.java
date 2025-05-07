package gui.util;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import gui.common.TableActionListener;

public class TableActionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = 1L;
    
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnView;
    private int row;

    public TableActionCellEditor(TableActionListener listener) {
        this(listener, true, true, false);
    }
    
    public TableActionCellEditor(TableActionListener listener, 
            boolean showEdit, boolean showDelete, boolean showView) {
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        panel.setOpaque(true);
        
        if (showEdit) {
            btnEdit = createButton("edit.png", "Editar", UIConstants.SECONDARY_BUTTON_COLOR);
            btnEdit.addActionListener(e -> {
                listener.onEdit(row);
                fireEditingStopped();
            });
            panel.add(btnEdit);
        }
        
        if (showView) {
            btnView = createButton("view.png", "Visualizar", UIConstants.PRIMARY_BUTTON_COLOR);
            btnView.addActionListener(e -> {
                listener.onView(row);
                fireEditingStopped();
            });
            panel.add(btnView);
        }
        
        if (showDelete) {
            btnDelete = createButton("delete.png", "Excluir", UIConstants.DANGER_BUTTON_COLOR);
            btnDelete.addActionListener(e -> {
                listener.onDelete(row);
                fireEditingStopped();
            });
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
        
        return button;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, 
            boolean isSelected, int row, int column) {
        
        this.row = row;
        
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
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
    
    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
}