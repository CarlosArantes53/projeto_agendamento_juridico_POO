package gui.cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import dao.ClienteDAO;
import gui.HomePanel;
import gui.common.ActionPanel;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import modelo.Cliente;
import gui.util.UIConstants;

public class EditarClientePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextArea txtEndereco;
    private ClienteDAO clienteDAO;
    private Cliente cliente;
    private HomePanel homePanel;
    
    public EditarClientePanel(Cliente cliente, HomePanel homePanel) {
        this.cliente = cliente;
        this.homePanel = homePanel;
        clienteDAO = new ClienteDAO();
        
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        HeaderPanel headerPanel = new HeaderPanel("Editar Cliente");
        
        FormPanel formPanel = new FormPanel();
        
        txtNome = formPanel.addTextField("Nome Completo:", cliente.getNome());
        formPanel.addReadOnlyField("CPF/CNPJ:", cliente.getDocumento());
        txtTelefone = formPanel.addTextField("Telefone:", cliente.getTelefone());
        txtEmail = formPanel.addTextField("E-mail:", cliente.getEmail());
        txtEndereco = formPanel.addTextArea("Endereço Completo:", cliente.getEndereco(), 4);
        
        ActionPanel actionPanel = new ActionPanel();
        
        actionPanel.addPrimaryButton("Salvar Alterações", this::salvarAlteracoes);
        actionPanel.addDangerButton("Cancelar", e -> homePanel.mostrarPainelClientes());
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void salvarAlteracoes(ActionEvent e) {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String endereco = txtEndereco.getText().trim();
        
        if (!FormValidator.camposObrigatorios(this, txtNome, txtTelefone, txtEmail, txtEndereco)) {
            return;
        }
        
        if (!FormValidator.validarEmail(this, txtEmail)) {
            return;
        }
        
        if (!FormValidator.validarTelefone(this, txtTelefone)) {
            return;
        }
        
        try {
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEmail(email);
            cliente.setEndereco(endereco);
            
            boolean sucesso = clienteDAO.atualizar(cliente);
            
            if (sucesso) {
                FormValidator.mostrarSucesso(this, "Cliente atualizado com sucesso!");
                homePanel.mostrarPainelClientes();
            } else {
                FormValidator.mostrarErro(this, "Erro ao atualizar cliente!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
}