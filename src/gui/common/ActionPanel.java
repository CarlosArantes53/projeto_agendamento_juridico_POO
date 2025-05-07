package gui.common;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.util.UIConstants;

public class ActionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final List<JButton> buttons = new ArrayList<>();
    
    public ActionPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        setBackground(UIConstants.PANEL_BACKGROUND);
    }
    
    public JButton addPrimaryButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupPrimaryButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    public JButton addSecondaryButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupSecondaryButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    public JButton addDangerButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupDangerButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    public JButton addWarningButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupWarningButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    public JButton getButtonAt(int index) {
        if (index >= 0 && index < buttons.size()) {
            return buttons.get(index);
        }
        return null;
    }
    
    public List<JButton> getButtons() {
        return new ArrayList<>(buttons);
    }
}