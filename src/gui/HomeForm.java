package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import modelo.Usuario;

public class HomeForm extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    
    public HomeForm(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        setTitle("HERMES - Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblBoasVindas = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
        lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 18));
        lblBoasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTipoUsuario = new JLabel("Tipo de acesso: " + usuarioLogado.getTipoUsuario());
        painelInfo.add(lblTipoUsuario);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Sair");
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.BLACK);
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogout();
            }
        });
        
        painelBotoes.add(btnLogout);
        
        painelPrincipal.add(lblBoasVindas, BorderLayout.NORTH);
        painelPrincipal.add(painelInfo, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        getContentPane().add(painelPrincipal);
    }
    
    private void realizarLogout() {
        dispose();
        
        new LoginForm().setVisible(true);
    }
}