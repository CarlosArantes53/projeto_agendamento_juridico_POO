package gui.common;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.util.UIConstants;

/**
 * Painel reutilizável para botões de ação
 */
public class ActionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Lista para armazenar os botões adicionados
    private final List<JButton> buttons = new ArrayList<>();
    
    public ActionPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        setBackground(UIConstants.PANEL_BACKGROUND);
    }
    
    /**
     * Adiciona um botão primário ao painel
     */
    public JButton addPrimaryButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupPrimaryButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    /**
     * Adiciona um botão secundário ao painel
     */
    public JButton addSecondaryButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupSecondaryButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    /**
     * Adiciona um botão de perigo (normalmente para cancelar ou excluir) ao painel
     */
    public JButton addDangerButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupDangerButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    /**
     * Adiciona um botão de aviso ao painel
     */
    public JButton addWarningButton(String text, ActionListener listener) {
        JButton button = new JButton();
        UIConstants.setupWarningButton(button, text);
        button.addActionListener(listener);
        add(button);
        buttons.add(button);
        return button;
    }
    
    /**
     * Retorna o botão no índice especificado
     * 
     * @param index Índice do botão (0 para o primeiro botão, etc.)
     * @return O botão no índice especificado ou null se o índice for inválido
     */
    public JButton getButtonAt(int index) {
        if (index >= 0 && index < buttons.size()) {
            return buttons.get(index);
        }
        return null;
    }
    
    /**
     * Retorna todos os botões do painel
     * 
     * @return Lista de botões
     */
    public List<JButton> getButtons() {
        return new ArrayList<>(buttons);
    }
}