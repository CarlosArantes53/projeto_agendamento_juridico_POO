package gui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import modelo.Usuario;
import util.UIConstants;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Constantes para identificar os painéis
    public static final String LOGIN_PANEL = "login";
    public static final String CADASTRO_PANEL = "cadastro";
    public static final String HOME_PANEL = "home";
    public static final String RECUPERAR_SENHA_PANEL = "recuperarSenha";
    public static final String EDITAR_PERFIL_PANEL = "editarPerfil";
    public static final String CADASTRO_CLIENTE_PANEL = "cadastroCliente";
    public static final String LISTAR_CLIENTES_PANEL = "listarClientes";
    
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
        setTitle("HERMES - Sistema de Agendamentos Jurídicos");
        setSize(1200, 900); // Tamanho maior para acomodar o menu lateral
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializa o layout de cartões
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        contentPane.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Inicializa os painéis
        inicializarPaineis();
        
        // Adiciona os painéis ao layout de cartões
        contentPane.add(loginPanel, LOGIN_PANEL);
        contentPane.add(cadastroPanel, CADASTRO_PANEL);
        contentPane.add(recuperarSenhaPanel, RECUPERAR_SENHA_PANEL);
        
        // Define o painel de login como inicial
        cardLayout.show(contentPane, LOGIN_PANEL);
        
        // Define o painel de conteúdo
        setContentPane(contentPane);
    }
    
    private void inicializarPaineis() {
        // Inicializa os painéis e configura os listeners
        loginPanel = new LoginPanel(this);
        cadastroPanel = new CadastroUsuarioPanel(this);
        recuperarSenhaPanel = new RecuperarSenhaPanel(this);
    }
    
    // Método para mostrar um painel específico
    public void mostrarPainel(String painelNome) {
        cardLayout.show(contentPane, painelNome);
    }
    
    // Método para criar e mostrar o painel Home após o login
    public void mostrarPainelHome(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        // Remove o painel home antigo se existir
        if (homePanel != null) {
            contentPane.remove(homePanel);
        }
        
        // Cria um novo painel home com o usuário logado
        homePanel = new HomePanel(this, usuarioLogado);
        contentPane.add(homePanel, HOME_PANEL);
        
        cardLayout.show(contentPane, HOME_PANEL);
    }
    
    // Método para criar e mostrar o painel de edição de perfil
    public void mostrarPainelEditarPerfil() {
        // Remove o painel de edição antigo se existir
        if (editarPerfilPanel != null) {
            contentPane.remove(editarPerfilPanel);
        }
        
        // Cria um novo painel de edição com o usuário logado
        editarPerfilPanel = new EditarPerfilPanel(this, usuarioLogado);
        contentPane.add(editarPerfilPanel, EDITAR_PERFIL_PANEL);
        
        cardLayout.show(contentPane, EDITAR_PERFIL_PANEL);
    }
    
    // Método para criar e mostrar o painel de cadastro de cliente
    public void mostrarPainelCadastroCliente() {
        // Remove o painel de cadastro de cliente antigo se existir
        if (cadastroClientePanel != null) {
            contentPane.remove(cadastroClientePanel);
        }
        
        // Cria um novo painel de cadastro de cliente
        cadastroClientePanel = new CadastroClientePanel(this);
        contentPane.add(cadastroClientePanel, CADASTRO_CLIENTE_PANEL);
        
        cardLayout.show(contentPane, CADASTRO_CLIENTE_PANEL);
    }
    
    // Método para atualizar o usuário logado após edição do perfil
    public void atualizarUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
    // Método para fazer logout
    public void fazerLogout() {
        this.usuarioLogado = null;
        mostrarPainel(LOGIN_PANEL);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}