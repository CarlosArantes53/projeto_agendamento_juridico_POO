package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import modelo.Usuario;

public class HomeForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Color BACKGROUND_COLOR = new Color(0,0,50); 
    private static final Color TEXT_COLOR = Color.WHITE;
    
    private Usuario usuarioLogado;
    
    public HomeForm(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        setTitle("HERMES - Home");
        setSize(480, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelPrincipal.setBackground(BACKGROUND_COLOR);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(100, 100, 255)));
        
        JLabel lblBoasVindas = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
        lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 18));
        lblBoasVindas.setForeground(TEXT_COLOR);
        lblBoasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblBoasVindas, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblTipoUsuario = new JLabel("Tipo de acesso: " + usuarioLogado.getTipoUsuario());
        lblTipoUsuario.setForeground(TEXT_COLOR);
        lblTipoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(lblTipoUsuario, gbc);
        
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JLabel lblDashboard = new JLabel("DASHBOARD", SwingConstants.CENTER);
        lblDashboard.setForeground(new Color(200, 200, 255));
        lblDashboard.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardPanel.add(lblDashboard, new GridBagConstraints());
        
        contentPanel.add(dashboardPanel, gbc);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(100, 100, 255)));
        
        JButton btnLogout = new JButton("Sair");
        btnLogout.setBackground(new Color(211, 47, 47));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogout();
            }
        });
        
        footerPanel.add(btnLogout);
        
        painelPrincipal.add(headerPanel, BorderLayout.NORTH);
        painelPrincipal.add(contentPanel, BorderLayout.CENTER);
        painelPrincipal.add(footerPanel, BorderLayout.SOUTH);
        
        getContentPane().add(painelPrincipal);
    }
    
    private void realizarLogout() {
        dispose();
        new LoginForm().setVisible(true);
    }
}