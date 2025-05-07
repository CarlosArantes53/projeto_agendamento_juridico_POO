package gui.cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import dao.ClienteDAO;
import gui.MainFrame;
import gui.common.ActionPanel;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import modelo.Cliente;
import gui.util.IconManager;
import gui.util.UIConstants;

public class CadastroClientePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JTextField txtDocumento;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextArea txtEndereco;
    private ClienteDAO clienteDAO;
    private MainFrame mainFrame;
    
    public CadastroClientePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        clienteDAO = new ClienteDAO();
        
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Cadastro de Cliente");
        
        FormPanel formPanel = new FormPanel();
        
        txtNome = formPanel.addTextField("Nome Completo:", null);
        txtDocumento = formPanel.addTextField("CPF/CNPJ:", null);
        txtTelefone = formPanel.addTextField("Telefone:", null);
        txtEmail = formPanel.addTextField("E-mail:", null);
        txtEndereco = formPanel.addTextArea("Endereço Completo:", null, 4);
        
        ActionPanel actionPanel = new ActionPanel();
        
        JButton btnCadastrar = actionPanel.addPrimaryButton("Cadastrar", this::cadastrarCliente);
        JButton btnCancelar = actionPanel.addDangerButton("Cancelar", e -> {
            limparCampos();
            mainFrame.voltarParaHome(true); 
        });
        
        IconManager.setButtonIcon(btnCadastrar, "save.png", 16, 16);
        IconManager.setButtonIcon(btnCancelar, "cancel.png", 16, 16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void cadastrarCliente(ActionEvent e) {
        String nome = txtNome.getText().trim();
        String documento = txtDocumento.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String endereco = txtEndereco.getText().trim();
        
        if (!FormValidator.camposObrigatorios(this, txtNome, txtDocumento, txtTelefone, txtEmail, txtEndereco)) {
            return;
        }
        
        if (!FormValidator.validarDocumento(this, txtDocumento)) {
            return;
        }
        
        if (!FormValidator.validarEmail(this, txtEmail)) {
            return;
        }
        
        if (!FormValidator.validarTelefone(this, txtTelefone)) {
            return;
        }
        
        try {
            if (clienteDAO.documentoExiste(documento)) {
                FormValidator.mostrarErro(this, "Este CPF/CNPJ já está cadastrado no sistema!");
                return;
            }
            
            Cliente cliente = new Cliente(nome, documento, telefone, email, endereco);
            
            int idGerado = clienteDAO.inserir(cliente);
            
            if (idGerado > 0) {
                FormValidator.mostrarSucesso(this, "Cliente cadastrado com sucesso!");
                limparCampos();
                mainFrame.voltarParaHome(true); 
            } else {
                FormValidator.mostrarErro(this, "Erro ao cadastrar cliente!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtDocumento.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
    }
}