package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gui.cliente.ClientesListPanel;
import gui.cliente.EditarClientePanel;
import gui.common.HeaderPanel;
import gui.util.IconManager;
import gui.util.UIConstants;
import modelo.Cliente;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private static final String DASHBOARD_PANEL = "dashboard";
    private static final String CLIENTES_PANEL = "clientes";
    private static final String EDITAR_CLIENTE_PANEL = "editarCliente";
    
    private Usuario usuarioLogado;
    private JPanel sideMenu;
    private boolean sideMenuVisible = true;
    private JPanel contentContainer;
    private CardLayout contentCardLayout;
    
    private ClientesListPanel clientesListPanel;
    private EditarClientePanel editarClientePanel;
    
    private MainFrame mainFrame;
    
    public HomePanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        JPanel headerPanel = criarPainelCabecalho();
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        sideMenu = criarMenuLateral();
        
        contentCardLayout = new CardLayout();
        contentContainer = new JPanel(contentCardLayout);
        contentContainer.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JPanel dashboardPanel = criarPainelDashboard();
        contentContainer.add(dashboardPanel, DASHBOARD_PANEL);
        
        clientesListPanel = new ClientesListPanel(mainFrame);
        contentContainer.add(clientesListPanel, CLIENTES_PANEL);
        
        mainPanel.add(sideMenu, BorderLayout.WEST);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel welcomePanel = criarPainelBoasVindas();
        JPanel welcomeContainer = new JPanel(new BorderLayout());
        welcomeContainer.setOpaque(false);
        welcomeContainer.add(welcomePanel, BorderLayout.NORTH);
        welcomeContainer.setBorder(new EmptyBorder(15, 0, 0, 15));
        
        contentCardLayout.show(contentContainer, DASHBOARD_PANEL);
    }
    
    private JPanel criarPainelCabecalho() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        
        JButton btnMenu = new JButton();
        IconManager.setupIconButton(btnMenu, IconManager.ICON_MENU, "Menu", 40);
        if (btnMenu.getIcon() == null) {
            UIConstants.setupIconButton(btnMenu, "Menu");
            btnMenu.setText("☰");
            btnMenu.setFont(new Font("Arial", Font.BOLD, 16));
        }
        
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alternarMenuLateral();
            }
        });
        
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        leftHeaderPanel.setBackground(UIConstants.HEADER_COLOR);
        leftHeaderPanel.add(btnMenu);
        
        JLabel lblTitle = new JLabel("HERMES");
        lblTitle.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblTitle.setFont(UIConstants.TITLE_FONT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightHeaderPanel.setBackground(UIConstants.HEADER_COLOR);
        
        JLabel lblUserName = new JLabel(usuarioLogado.getNome());
        lblUserName.setForeground(UIConstants.TEXT_SECONDARY);
        lblUserName.setFont(UIConstants.LABEL_FONT);
        
        JButton btnProfile = new JButton();
        IconManager.setupIconButton(btnProfile, IconManager.ICON_PROFILE, "Editar Perfil", 40);
        if (btnProfile.getIcon() == null) {
            UIConstants.setupIconButton(btnProfile, "Editar Perfil");
            btnProfile.setText("👤");
            btnProfile.setFont(new Font("Arial", Font.PLAIN, 16));
        }
        
        JButton btnLogout = new JButton();
        IconManager.setupIconButton(btnLogout, IconManager.ICON_LOGOUT, "Sair", 40);
        if (btnLogout.getIcon() == null) {
            UIConstants.setupIconButton(btnLogout, "Sair");
            btnLogout.setText("⊗");
            btnLogout.setFont(new Font("Arial", Font.PLAIN, 16));
        }
        
        btnProfile.addActionListener(e -> mainFrame.mostrarPainelEditarPerfil());
        btnLogout.addActionListener(e -> mainFrame.fazerLogout());
        
        rightHeaderPanel.add(lblUserName);
        rightHeaderPanel.add(btnProfile);
        rightHeaderPanel.add(btnLogout);
        
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel criarMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(UIConstants.MENU_BACKGROUND);
        menu.setPreferredSize(UIConstants.MENU_WIDTH);
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.HEADER_BORDER_COLOR));
        
        JLabel lblMenuTitle = new JLabel("Menu Principal");
        lblMenuTitle.setForeground(UIConstants.TEXT_COLOR);
        lblMenuTitle.setFont(UIConstants.SUBTITLE_FONT);
        lblMenuTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblMenuTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        menu.add(lblMenuTitle);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel dashboardOption = criarOpcaoMenu(
            "Dashboard", 
            IconManager.ICON_DASHBOARD, 
            e -> contentCardLayout.show(contentContainer, DASHBOARD_PANEL)
        );
        menu.add(dashboardOption);
        
        JPanel clientesHeader = criarCabecalhoMenu("CLIENTES");
        menu.add(clientesHeader);
        
        JPanel listarClientesOption = criarOpcaoMenu(
            "Clientes", 
            IconManager.ICON_LIST, 
            e -> {
                clientesListPanel.carregarClientes();
                contentCardLayout.show(contentContainer, CLIENTES_PANEL);
            }
        );
        menu.add(listarClientesOption);
        
        
        JPanel agendamentosHeader = criarCabecalhoMenu("AGENDAMENTOS");
        menu.add(agendamentosHeader);
        
        JPanel novoAgendamentoOption = criarOpcaoMenu(
            "Novo Agendamento", 
            IconManager.ICON_ADD_W, 
            e -> mainFrame.mostrarPainelAgendamento()
        );
        menu.add(novoAgendamentoOption);
        
        JPanel calendarioOption = criarOpcaoMenu(
            "Calendário", 
            IconManager.ICON_CALENDAR, 
            e -> mainFrame.mostrarPainelCalendario()
        );
        menu.add(calendarioOption);
        
        
        if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADVOGADO) {
            JPanel disponibilidadeHeader = criarCabecalhoMenu("DISPONIBILIDADE");
            menu.add(disponibilidadeHeader);
            
            JPanel gerenciarDisponibilidadeOption = criarOpcaoMenu(
                "Gerenciar Horários", 
                IconManager.ICON_LIST, 
                e -> mainFrame.mostrarPainelGerenciarDisponibilidade(usuarioLogado)
            );
            menu.add(gerenciarDisponibilidadeOption);
            
            JPanel excecoesOption = criarOpcaoMenu(
                "Exceções (Férias/Folgas)", 
                IconManager.ICON_CALENDAR, 
                e -> mainFrame.mostrarPainelExcecaoDisponibilidade(usuarioLogado)
            );
            menu.add(excecoesOption);
        }
        
        
        if (usuarioLogado.getTipoUsuario() == TipoUsuario.SECRETARIO) {
            JPanel configuracoesHeader = criarCabecalhoMenu("CONFIGURAÇÕES");
            menu.add(configuracoesHeader);
            
            JPanel tiposAtendimentoOption = criarOpcaoMenu(
                "Tipos de Atendimento", 
                IconManager.ICON_LIST, 
                e -> mainFrame.mostrarPainelTipoAtendimento()
            );
            menu.add(tiposAtendimentoOption);
        }
        
        menu.add(Box.createVerticalGlue());
        
        return menu;
    }
    
    private JPanel criarCabecalhoMenu(String texto) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(new Color(30, 30, 60));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setBorder(new EmptyBorder(5, 15, 5, 10));
        
        JLabel label = new JLabel(texto);
        label.setForeground(UIConstants.MENU_HEADER_TEXT);
        label.setFont(UIConstants.MENU_HEADER_FONT);
        
        header.add(label);
        
        return header;
    }
    
    private JPanel criarOpcaoMenu(String texto, String iconName, ActionListener action) {
        JPanel option = new JPanel();
        option.setLayout(new BoxLayout(option, BoxLayout.X_AXIS));
        option.setBackground(UIConstants.MENU_BACKGROUND);
        option.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        option.setBorder(new EmptyBorder(5, 15, 5, 10));
        
        JButton button = new JButton(texto);
        button.setForeground(Color.WHITE);
        button.setBackground(UIConstants.MENU_BACKGROUND);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(UIConstants.MENU_ITEM_FONT);
        
        ImageIcon icon = IconManager.loadIcon(iconName, 18, 18);
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addActionListener(action);
        
        option.add(button);
        option.add(Box.createHorizontalGlue());
        
        option.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                option.setBackground(UIConstants.PRIMARY_LIGHT);
                button.setBackground(UIConstants.PRIMARY_LIGHT);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                option.setBackground(UIConstants.MENU_BACKGROUND);
                button.setBackground(UIConstants.MENU_BACKGROUND);
            }
        });
        
        return option;
    }
    
    private void alternarMenuLateral() {
        sideMenuVisible = !sideMenuVisible;
        sideMenu.setVisible(sideMenuVisible);
        revalidate();
        repaint();
    }
    
    private JPanel criarPainelDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(UIConstants.PANEL_BACKGROUND);
        dashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        HeaderPanel headerPanel = new HeaderPanel("Dashboard");
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        
        JLabel lblDashboard = new JLabel("Bem-vindo ao Sistema de Agendamentos HERMES", SwingConstants.CENTER);
        lblDashboard.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblDashboard.setFont(new Font(UIConstants.TITLE_FONT.getName(), Font.BOLD, 22));
        lblDashboard.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel botoesRapidos = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        botoesRapidos.setBackground(UIConstants.PANEL_BACKGROUND);
        botoesRapidos.setAlignmentX(CENTER_ALIGNMENT);
        
        
        Dimension botaoDimensao = new Dimension(250, 80);
        Font fonteBotao = new Font(UIConstants.BUTTON_FONT.getName(), Font.BOLD, 16);
        
        JButton btnNovoAgendamento = new JButton("Novo Agendamento");
        UIConstants.setupPrimaryButton(btnNovoAgendamento, "Novo Agendamento");
        btnNovoAgendamento.setPreferredSize(botaoDimensao);
        btnNovoAgendamento.setFont(fonteBotao);
        btnNovoAgendamento.addActionListener(e -> mainFrame.mostrarPainelAgendamento());
        
        JButton btnCalendario = new JButton("Ver Calendário");
        UIConstants.setupSecondaryButton(btnCalendario, "Ver Calendário");
        btnCalendario.setPreferredSize(botaoDimensao);
        btnCalendario.setFont(fonteBotao);
        btnCalendario.addActionListener(e -> mainFrame.mostrarPainelCalendario());
        
        JButton btnClientes = new JButton("Gerenciar Clientes");
        UIConstants.setupSecondaryButton(btnClientes, "Gerenciar Clientes");
        btnClientes.setPreferredSize(botaoDimensao);
        btnClientes.setFont(fonteBotao);
        btnClientes.addActionListener(e -> {
            clientesListPanel.carregarClientes();
            contentCardLayout.show(contentContainer, CLIENTES_PANEL);
        });
        
        JButton btnTiposAtendimento = new JButton("Gerenciar Tipos de Atendimento");
        UIConstants.setupSecondaryButton(btnTiposAtendimento, "Gerenciar Tipos de Atendimento");
        btnTiposAtendimento.setPreferredSize(botaoDimensao);
        btnTiposAtendimento.setFont(fonteBotao);
        btnTiposAtendimento.addActionListener(e -> mainFrame.mostrarPainelTipoAtendimento());
        
        
        JPanel linhaUm = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        linhaUm.setBackground(UIConstants.PANEL_BACKGROUND);
        linhaUm.add(btnNovoAgendamento);
        linhaUm.add(btnCalendario);
        
        JPanel linhaDois = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        linhaDois.setBackground(UIConstants.PANEL_BACKGROUND);
        linhaDois.add(btnClientes);
        linhaDois.add(btnTiposAtendimento);
        
        
        botoesRapidos.setLayout(new BoxLayout(botoesRapidos, BoxLayout.Y_AXIS));
        botoesRapidos.add(linhaUm);
        botoesRapidos.add(linhaDois);
        
        
        if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADVOGADO) {
            JButton btnDisponibilidade = new JButton("Gerenciar Disponibilidade");
            UIConstants.setupSecondaryButton(btnDisponibilidade, "Gerenciar Disponibilidade");
            btnDisponibilidade.setPreferredSize(botaoDimensao);
            btnDisponibilidade.setFont(fonteBotao);
            btnDisponibilidade.addActionListener(e -> mainFrame.mostrarPainelGerenciarDisponibilidade(usuarioLogado));
            
            JPanel linhaTres = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            linhaTres.setBackground(UIConstants.PANEL_BACKGROUND);
            linhaTres.add(btnDisponibilidade);
            botoesRapidos.add(linhaTres);
        }
        
        
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(lblDashboard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        contentPanel.add(botoesRapidos);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(Box.createVerticalGlue());
        
        dashboard.add(headerPanel, BorderLayout.NORTH);
        dashboard.add(contentPanel, BorderLayout.CENTER);
        
        return dashboard;
    }
    
    private JPanel criarPainelBoasVindas() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(UIConstants.WELCOME_PANEL_BG);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 90, 80), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        
        JLabel lblWelcome = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(UIConstants.LABEL_FONT);
        welcomePanel.add(lblWelcome);
        
        return welcomePanel;
    }
    
    public void mostrarPainelEditarCliente(Cliente cliente) {
        if (editarClientePanel != null) {
            contentContainer.remove(editarClientePanel);
        }
        
        editarClientePanel = new EditarClientePanel(cliente, this);
        contentContainer.add(editarClientePanel, EDITAR_CLIENTE_PANEL);
        contentCardLayout.show(contentContainer, EDITAR_CLIENTE_PANEL);
    }
    
    public void mostrarPainelClientes() {
        clientesListPanel.carregarClientes();
        contentCardLayout.show(contentContainer, CLIENTES_PANEL);
    }
}