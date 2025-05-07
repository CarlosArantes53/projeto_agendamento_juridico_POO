package gui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.cliente.CadastroClientePanel;
import gui.usuario.CadastroUsuarioPanel;
import gui.usuario.EditarPerfilPanel;
import gui.usuario.LoginPanel;
import gui.usuario.RecuperarSenhaPanel;
import modelo.Cliente;
import modelo.Usuario;
import gui.util.UIConstants;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public static final String LOGIN_PANEL = "login";
    public static final String CADASTRO_PANEL = "cadastro";
    public static final String HOME_PANEL = "home";
    public static final String RECUPERAR_SENHA_PANEL = "recuperarSenha";
    public static final String EDITAR_PERFIL_PANEL = "editarPerfil";
    public static final String CADASTRO_CLIENTE_PANEL = "cadastroCliente";
    
    private JPanel contentPane;
    private CardLayout cardLayout;
    
    private LoginPanel loginPanel;
    private CadastroUsuarioPanel cadastroPanel;
    private HomePanel homePanel;
    private RecuperarSenhaPanel recuperarSenhaPanel;
    private EditarPerfilPanel editarPerfilPanel;
    private CadastroClientePanel cadastroClientePanel;
    
    private Usuario usuarioLogado;
    
    public MainFrame() {
        configurarAparencia();
        
        setTitle("HERMES - Sistema de Agendamentos Jurídicos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        contentPane.setBackground(UIConstants.BACKGROUND_COLOR);
        
        inicializarPaineis();
        
        contentPane.add(loginPanel, LOGIN_PANEL);
        contentPane.add(cadastroPanel, CADASTRO_PANEL);
        contentPane.add(recuperarSenhaPanel, RECUPERAR_SENHA_PANEL);
        
        cardLayout.show(contentPane, LOGIN_PANEL);
        
        setContentPane(contentPane);
    }
    
    private void configurarAparencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    private void inicializarPaineis() {
        loginPanel = new LoginPanel(this);
        cadastroPanel = new CadastroUsuarioPanel(this);
        recuperarSenhaPanel = new RecuperarSenhaPanel(this);
    }
    
    public void mostrarPainel(String painelNome) {
        cardLayout.show(contentPane, painelNome);
    }
    
    public void mostrarPainelHome(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        if (homePanel != null) {
            contentPane.remove(homePanel);
        }
        
        homePanel = new HomePanel(this, usuarioLogado);
        contentPane.add(homePanel, HOME_PANEL);
        
        cardLayout.show(contentPane, HOME_PANEL);
    }
    
    public void mostrarPainelEditarPerfil() {
        if (editarPerfilPanel != null) {
            contentPane.remove(editarPerfilPanel);
        }
        
        editarPerfilPanel = new EditarPerfilPanel(this, usuarioLogado);
        contentPane.add(editarPerfilPanel, EDITAR_PERFIL_PANEL);
        
        cardLayout.show(contentPane, EDITAR_PERFIL_PANEL);
    }
    
    public void mostrarPainelCadastroCliente() {
        if (cadastroClientePanel != null) {
            contentPane.remove(cadastroClientePanel);
        }
        
        cadastroClientePanel = new CadastroClientePanel(this);
        contentPane.add(cadastroClientePanel, CADASTRO_CLIENTE_PANEL);
        
        cardLayout.show(contentPane, CADASTRO_CLIENTE_PANEL);
    }
    
    public void mostrarPainelEditarCliente(Cliente cliente) {
        homePanel.mostrarPainelEditarCliente(cliente);
    }
    
    public void voltarParaHome() {
        if (homePanel != null) {
            cardLayout.show(contentPane, HOME_PANEL);
        }
    }
    
    public void atualizarUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
    public void fazerLogout() {
        this.usuarioLogado = null;
        mostrarPainel(LOGIN_PANEL);
    }
}