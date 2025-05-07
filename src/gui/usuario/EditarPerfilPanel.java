package gui.usuario;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import gui.MainFrame;
import gui.common.ActionPanel;
import gui.common.FormPanel;
import gui.common.FormValidator;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;
import gui.util.UIConstants;

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
        
        UIConstants.setupPanel(this);
        setLayout(new BorderLayout());
        
        // Formulário principal
        FormPanel formPanel = new FormPanel();
        
        // Título
        formPanel.addTitle("Editar Perfil");
        
        // Campos de texto
        txtNome = formPanel.addTextField("Nome Completo:", usuarioLogado.getNome());
        txtEmail = formPanel.addTextField("E-mail:", usuarioLogado.getEmail());
        txtTelefone = formPanel.addTextField("Telefone:", usuarioLogado.getTelefone());
        
        // Tipo de usuário
        JPanel tipoUsuarioPanel = criarPainelTipoUsuario();
        formPanel.addComponent("Tipo de Usuário:", tipoUsuarioPanel);
        
        // Painel de botões
        ActionPanel actionPanel = new ActionPanel();
        
        actionPanel.addPrimaryButton("Salvar Alterações", this::salvarAlteracoes);
        actionPanel.addDangerButton("Cancelar", e -> mainFrame.mostrarPainel(MainFrame.HOME_PANEL));
        
        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelTipoUsuario() {
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
        
        return painelTipo;
    }
    
    private void salvarAlteracoes(ActionEvent e) {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        TipoUsuario tipoUsuario = rdbAdvogado.isSelected() ? TipoUsuario.ADVOGADO : TipoUsuario.SECRETARIO;
        
        // Validar campos obrigatórios
        if (!FormValidator.camposObrigatorios(this, txtNome, txtEmail, txtTelefone)) {
            return;
        }
        
        // Validar formato de email
        if (!FormValidator.validarEmail(this, txtEmail)) {
            return;
        }
        
        // Validar formato de telefone
        if (!FormValidator.validarTelefone(this, txtTelefone)) {
            return;
        }
        
        try {
            // Verificar se o novo email já existe para outro usuário
            if (!email.equals(usuarioLogado.getEmail()) && usuarioDAO.emailExiste(email)) {
                FormValidator.mostrarErro(this, "Este e-mail já está cadastrado no sistema!");
                return;
            }
            
            // Atualizar os dados do usuário
            usuarioLogado.setNome(nome);
            usuarioLogado.setEmail(email);
            usuarioLogado.setTelefone(telefone);
            usuarioLogado.setTipoUsuario(tipoUsuario);
            
            boolean sucesso = usuarioDAO.atualizarPerfil(usuarioLogado);
            
            if (sucesso) {
                FormValidator.mostrarSucesso(this, "Perfil atualizado com sucesso!");
                
                mainFrame.atualizarUsuarioLogado(usuarioLogado);
                mainFrame.mostrarPainelHome(usuarioLogado);
            } else {
                FormValidator.mostrarErro(this, "Erro ao atualizar perfil!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
}