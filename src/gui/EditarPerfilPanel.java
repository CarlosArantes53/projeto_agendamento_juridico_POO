package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;
import util.UIConstants;
import util.Validador;

public class EditarPerfilPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JRadioButton rdbAdvogado;
    private JRadioButton rdbSecretario;
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioLogado;
    private MainFrame mainFrame;
    
    public EditarPerfilPanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        usuarioDAO = new UsuarioDAO();
        
        // Set background color
        UIConstants.setupPanel(this);
        
        setLayout(new BorderLayout());
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(UIConstants.PANEL_PADDING);
        painelPrincipal.setBackground(UIConstants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel lblTitulo = new JLabel();
        UIConstants.setupSubtitleLabel(lblTitulo, "Editar Perfil");
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        painelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nome label
        JLabel lblNome = new JLabel();
        UIConstants.setupLabel(lblNome, "Nome Completo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(lblNome, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        txtNome.setText(usuarioLogado.getNome());
        painelPrincipal.add(txtNome, gbc);
        
        // Email label
        JLabel lblEmail = new JLabel();
        UIConstants.setupLabel(lblEmail, "E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        txtEmail.setText(usuarioLogado.getEmail());
        painelPrincipal.add(txtEmail, gbc);
        
        // Telefone label
        JLabel lblTelefone = new JLabel();
        UIConstants.setupLabel(lblTelefone, "Telefone:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTelefone, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTelefone = new JTextField(20);
        txtTelefone.setText(usuarioLogado.getTelefone());
        painelPrincipal.add(txtTelefone, gbc);
        
        // Tipo de usuário label
        JLabel lblTipoUsuario = new JLabel();
        UIConstants.setupLabel(lblTipoUsuario, "Tipo de Usuário:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTipoUsuario, gbc);
        
        gbc.gridx = 1;
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.setBackground(UIConstants.BACKGROUND_COLOR);
        
        rdbAdvogado = new JRadioButton();
        UIConstants.setupRadioButton(rdbAdvogado, "Advogado");
        
        rdbSecretario = new JRadioButton();
        UIConstants.setupRadioButton(rdbSecretario, "Secretário");
        
        if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADVOGADO) {
            rdbAdvogado.setSelected(true);
        } else {
            rdbSecretario.setSelected(true);
        }
        
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rdbAdvogado);
        grupoTipo.add(rdbSecretario);
        
        painelTipo.add(rdbAdvogado);
        painelTipo.add(rdbSecretario);
        painelPrincipal.add(painelTipo, gbc);
        
        // Buttons panel
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnSalvar = new JButton();
        UIConstants.setupPrimaryButton(btnSalvar, "Salvar Alterações");
        
        JButton btnCancelar = new JButton();
        UIConstants.setupDangerButton(btnCancelar, "Cancelar");
        
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainel(MainFrame.HOME_PANEL);
            }
        });
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void salvarAlteracoes() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        TipoUsuario tipoUsuario = rdbAdvogado.isSelected() ? TipoUsuario.ADVOGADO : TipoUsuario.SECRETARIO;
        
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarEmail(email)) {
            JOptionPane.showMessageDialog(this, "Formato de e-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarTelefone(telefone)) {
            JOptionPane.showMessageDialog(this, "Formato de telefone inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Verifica se o novo email já existe para outro usuário
            if (!email.equals(usuarioLogado.getEmail()) && usuarioDAO.emailExiste(email)) {
                JOptionPane.showMessageDialog(this, "Este e-mail já está cadastrado no sistema!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Atualiza os dados do usuário
            usuarioLogado.setNome(nome);
            usuarioLogado.setEmail(email);
            usuarioLogado.setTelefone(telefone);
            usuarioLogado.setTipoUsuario(tipoUsuario);
            
            boolean sucesso = usuarioDAO.atualizarPerfil(usuarioLogado);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Perfil atualizado com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                mainFrame.atualizarUsuarioLogado(usuarioLogado);
                mainFrame.mostrarPainelHome(usuarioLogado);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar perfil!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao acessar o banco de dados: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}