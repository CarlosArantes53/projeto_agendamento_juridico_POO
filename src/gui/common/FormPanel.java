package gui.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gui.util.UIConstants;

public class FormPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private GridBagConstraints gbc;
    private int currentRow = 0;
    
    public FormPanel() {
        setLayout(new GridBagLayout());
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
    }
    
    public JTextField addTextField(String labelText, String initialValue) {
        JLabel label = new JLabel();
        UIConstants.setupLabel(label, labelText);
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        add(label, gbc);
        
        JTextField textField = new JTextField(20);
        if (initialValue != null) {
            textField.setText(initialValue);
        }
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        add(textField, gbc);
        
        currentRow++;
        return textField;
    }
    
    public JPasswordField addPasswordField(String labelText) {
        JLabel label = new JLabel();
        UIConstants.setupLabel(label, labelText);
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        add(label, gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        add(passwordField, gbc);
        
        currentRow++;
        return passwordField;
    }
    
    public JTextField addReadOnlyField(String labelText, String value) {
        JLabel label = new JLabel();
        UIConstants.setupLabel(label, labelText);
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        add(label, gbc);
        
        JTextField textField = new JTextField(20);
        textField.setText(value);
        textField.setEditable(false);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        add(textField, gbc);
        
        currentRow++;
        return textField;
    }

    public JTextArea addTextArea(String labelText, String initialValue, int rows) {
        JLabel label = new JLabel();
        UIConstants.setupLabel(label, labelText);
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(15, 5, 8, 5);
        add(label, gbc);
        
        JTextArea textArea = new JTextArea(rows, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(UIConstants.LABEL_FONT);
        textArea.setBackground(UIConstants.ROW_EVEN);
        textArea.setForeground(UIConstants.TEXT_COLOR);
        
        if (initialValue != null) {
            textArea.setText(initialValue);
        }
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR));
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        currentRow++;
        return textArea;
    }
    
    public void addComponent(String labelText, JComponent component) {
        JLabel label = new JLabel();
        UIConstants.setupLabel(label, labelText);
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        add(component, gbc);
        
        currentRow++;
    }
    

    public void addTitle(String titleText) {
        JLabel title = new JLabel();
        UIConstants.setupSubtitleLabel(title, titleText);
        title.setHorizontalAlignment(JLabel.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        currentRow++;
    }
    
    public void addSpacer(int height) {
        javax.swing.Box.Filler filler = new javax.swing.Box.Filler(
            new java.awt.Dimension(0, height),
            new java.awt.Dimension(0, height),
            new java.awt.Dimension(0, height)
        );
        
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = 2;
        add(filler, gbc);
        
        gbc.gridwidth = 1;
        currentRow++;
    }
}