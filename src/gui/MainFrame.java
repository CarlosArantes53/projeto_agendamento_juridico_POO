package gui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.advogado.ExcecaoDisponibilidadePanel;
import gui.advogado.GerenciarDisponibilidadePanel;
import gui.agendamento.AgendamentoPanel;
import gui.agendamento.CalendarioPanel;
import gui.agendamento.DetalhesAgendamentoPanel;
import gui.agendamento.TipoAtendimentoPanel;
import gui.cliente.CadastroClientePanel;
import gui.usuario.CadastroUsuarioPanel;
import gui.usuario.EditarPerfilPanel;
import gui.usuario.LoginPanel;
import gui.usuario.RecuperarSenhaPanel;
import modelo.Agendamento;
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
    public static final String DISPONIBILIDADE_PANEL = "disponibilidade";
    public static final String EXCECAO_DISPONIBILIDADE_PANEL = "excecaoDisponibilidade";
    public static final String AGENDAMENTO_PANEL = "agendamento";
    public static final String CALENDARIO_PANEL = "calendario";
    public static final String DETALHES_AGENDAMENTO_PANEL = "detalhesAgendamento";
    public static final String TIPO_ATENDIMENTO_PANEL = "tipoAtendimento";
    
    private JPanel contentPane;
    private CardLayout cardLayout;
    
    private LoginPanel loginPanel;
    private CadastroUsuarioPanel cadastroPanel;
    private HomePanel homePanel;
    private RecuperarSenhaPanel recuperarSenhaPanel;
    private EditarPerfilPanel editarPerfilPanel;
    private CadastroClientePanel cadastroClientePanel;
    private GerenciarDisponibilidadePanel disponibilidadePanel;
    private ExcecaoDisponibilidadePanel excecaoDisponibilidadePanel;
    private AgendamentoPanel agendamentoPanel;
    private CalendarioPanel calendarioPanel;
    private DetalhesAgendamentoPanel detalhesAgendamentoPanel;
    private TipoAtendimentoPanel tipoAtendimentoPanel;
    
    private Usuario usuarioLogado;
    
    public MainFrame() {
        configurarAparencia();
        
        setTitle("HERMES - Sistema de Agendamentos Jurídicos");
        setSize(1000, 700);
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
    
    public void mostrarPainelGerenciarDisponibilidade(Usuario usuario) {
        if (disponibilidadePanel != null) {
            contentPane.remove(disponibilidadePanel);
        }
        
        disponibilidadePanel = new GerenciarDisponibilidadePanel(this, usuario);
        contentPane.add(disponibilidadePanel, DISPONIBILIDADE_PANEL);
        
        cardLayout.show(contentPane, DISPONIBILIDADE_PANEL);
    }
    
    public void mostrarPainelExcecaoDisponibilidade(Usuario usuario) {
        if (excecaoDisponibilidadePanel != null) {
            contentPane.remove(excecaoDisponibilidadePanel);
        }
        
        excecaoDisponibilidadePanel = new ExcecaoDisponibilidadePanel(this, usuario);
        contentPane.add(excecaoDisponibilidadePanel, EXCECAO_DISPONIBILIDADE_PANEL);
        
        cardLayout.show(contentPane, EXCECAO_DISPONIBILIDADE_PANEL);
    }
    
    public void mostrarPainelAgendamento() {
        if (agendamentoPanel != null) {
            contentPane.remove(agendamentoPanel);
        }
        
        agendamentoPanel = new AgendamentoPanel(this, usuarioLogado);
        contentPane.add(agendamentoPanel, AGENDAMENTO_PANEL);
        
        cardLayout.show(contentPane, AGENDAMENTO_PANEL);
    }
    
    public void mostrarPainelCalendario() {
        if (calendarioPanel != null) {
            contentPane.remove(calendarioPanel);
        }
        
        calendarioPanel = new CalendarioPanel(this, usuarioLogado);
        contentPane.add(calendarioPanel, CALENDARIO_PANEL);
        
        cardLayout.show(contentPane, CALENDARIO_PANEL);
    }
    
    public void mostrarPainelDetalhesAgendamento(Agendamento agendamento) {
        if (detalhesAgendamentoPanel != null) {
            contentPane.remove(detalhesAgendamentoPanel);
        }
        
        detalhesAgendamentoPanel = new DetalhesAgendamentoPanel(this, usuarioLogado, agendamento);
        contentPane.add(detalhesAgendamentoPanel, DETALHES_AGENDAMENTO_PANEL);
        
        cardLayout.show(contentPane, DETALHES_AGENDAMENTO_PANEL);
    }
    
    public void mostrarPainelTipoAtendimento() {
        if (tipoAtendimentoPanel != null) {
            contentPane.remove(tipoAtendimentoPanel);
        }
        
        tipoAtendimentoPanel = new TipoAtendimentoPanel(this, usuarioLogado);
        contentPane.add(tipoAtendimentoPanel, TIPO_ATENDIMENTO_PANEL);
        
        cardLayout.show(contentPane, TIPO_ATENDIMENTO_PANEL);
    }
    
    public void voltarParaHome() {
        voltarParaHome(false);
    }
    
    public void voltarParaHome(boolean mostrarClientes) {
        if (homePanel != null) {
            cardLayout.show(contentPane, HOME_PANEL);
            
            if (mostrarClientes) {
                homePanel.mostrarPainelClientes();
            }
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