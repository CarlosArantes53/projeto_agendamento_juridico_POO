package gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import gui.ClientesListPanel.TableActionListener;
import util.IconManager;
import util.UIConstants;

public class TableActionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = 1L;
    
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private int row;
    
    public TableActionCellEditor(TableActionListener listener) {
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
        
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onEdit(row);
                fireEditingStopped();
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onDelete(row);
                fireEditingStopped();
            }
        });
        
        panel.add(btnEdit);
        panel.add(btnDelete);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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