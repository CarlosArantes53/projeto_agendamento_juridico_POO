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
        
        // Criar o cabeçalho
        HeaderPanel headerPanel = new HeaderPanel("Cadastro de Cliente");
        
        // Criar o formulário
        FormPanel formPanel = new FormPanel();
        
        txtNome = formPanel.addTextField("Nome Completo:", null);
        txtDocumento = formPanel.addTextField("CPF/CNPJ:", null);
        txtTelefone = formPanel.addTextField("Telefone:", null);
        txtEmail = formPanel.addTextField("E-mail:", null);
        txtEndereco = formPanel.addTextArea("Endereço Completo:", null, 4);
        
        // Criar o painel de botões
        ActionPanel actionPanel = new ActionPanel();
        
        // Adicionar botões ao painel de ações
        JButton btnCadastrar = actionPanel.addPrimaryButton("Cadastrar", this::cadastrarCliente);
        JButton btnCancelar = actionPanel.addDangerButton("Cancelar", e -> {
            limparCampos();
            mainFrame.voltarParaHome();
        });
        
        // Adicionar ícones aos botões - VERSÃO CORRIGIDA
        IconManager.setButtonIcon(btnCadastrar, "save.png", 16, 16);
        IconManager.setButtonIcon(btnCancelar, "cancel.png", 16, 16);
        
        // Adicionar componentes ao painel principal
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
        
        // Validar campos obrigatórios
        if (!FormValidator.camposObrigatorios(this, txtNome, txtDocumento, txtTelefone, txtEmail, txtEndereco)) {
            return;
        }
        
        // Validar formato de documento
        if (!FormValidator.validarDocumento(this, txtDocumento)) {
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
            // Verificar se documento já existe
            if (clienteDAO.documentoExiste(documento)) {
                FormValidator.mostrarErro(this, "Este CPF/CNPJ já está cadastrado no sistema!");
                return;
            }
            
            // Criar e inserir cliente
            Cliente cliente = new Cliente(nome, documento, telefone, email, endereco);
            
            int idGerado = clienteDAO.inserir(cliente);
            
            if (idGerado > 0) {
                FormValidator.mostrarSucesso(this, "Cliente cadastrado com sucesso!");
                limparCampos();
                mainFrame.voltarParaHome();
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