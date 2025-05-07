package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import modelo.Usuario;
import util.UIConstants;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    public HomePanel(MainFrame mainFrame, Usuario usuario) {
        this.usuarioLogado = usuario;
        
        // Set background color
        setBackground(UIConstants.BACKGROUND_COLOR);
        
        setLayout(new BorderLayout());
        setBorder(UIConstants.PANEL_PADDING);
        
        // Header panel with welcome message
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR));
        
        JLabel lblBoasVindas = new JLabel();
        UIConstants.setupTitleLabel(lblBoasVindas, "Bem-vindo(a), " + usuarioLogado.getNome() + "!");
        lblBoasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblBoasVindas, BorderLayout.CENTER);
        
        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblTipoUsuario = new JLabel();
        UIConstants.setupLabel(lblTipoUsuario, "Tipo de acesso: " + usuarioLogado.getTipoUsuario());
        contentPanel.add(lblTipoUsuario, gbc);
        
        // Dashboard area (future enhancement)
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JLabel lblDashboard = new JLabel("DASHBOARD", SwingConstants.CENTER);
        lblDashboard.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblDashboard.setFont(UIConstants.TITLE_FONT);
        dashboardPanel.add(lblDashboard, new GridBagConstraints());
        
        contentPanel.add(dashboardPanel, gbc);
        
        // Footer panel with buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, UIConstants.HEADER_BORDER_COLOR));
        
        // Botão para cadastrar cliente (NOVO)
        JButton btnCadastrarCliente = new JButton();
        UIConstants.setupSecondaryButton(btnCadastrarCliente, "Cadastrar Cliente");
        
        JButton btnEditarPerfil = new JButton();
        UIConstants.setupSecondaryButton(btnEditarPerfil, "Editar Perfil");
        
        JButton btnLogout = new JButton();
        UIConstants.setupDangerButton(btnLogout, "Sair");
        
        // Action listener para o botão de cadastrar cliente (NOVO)
        btnCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainelCadastroCliente();
            }
        });
        
        btnEditarPerfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainelEditarPerfil();
            }
        });
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.fazerLogout();
            }
        });
        
        // Adicionar o botão de cadastrar cliente ao painel (NOVO)
        footerPanel.add(btnCadastrarCliente);
        footerPanel.add(btnEditarPerfil);
        footerPanel.add(btnLogout);
        
        // Add all panels to the main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
}