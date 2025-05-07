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

/**
 * Painel principal da aplicação após o login
 */
public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Constantes para identificar painéis no CardLayout
    private static final String DASHBOARD_PANEL = "dashboard";
    private static final String CLIENTES_PANEL = "clientes";
    private static final String EDITAR_CLIENTE_PANEL = "editarCliente";
    
    // Componentes principais
    private Usuario usuarioLogado;
    private JPanel sideMenu;
    private boolean sideMenuVisible = true;
    private JPanel contentContainer;
    private CardLayout contentCardLayout;
    
    // Painéis de conteúdo
    private ClientesListPanel clientesListPanel;
    private EditarClientePanel editarClientePanel;
    
    private MainFrame mainFrame;
    
    /**
     * Construtor
     */
    public HomePanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        // Criar componentes principais
        JPanel headerPanel = criarPainelCabecalho();
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Criar menu lateral
        sideMenu = criarMenuLateral();
        
        // Criar container de conteúdo com CardLayout
        contentCardLayout = new CardLayout();
        contentContainer = new JPanel(contentCardLayout);
        contentContainer.setBackground(UIConstants.PANEL_BACKGROUND);
        
        // Criar painéis de conteúdo padrão
        JPanel dashboardPanel = criarPainelDashboard();
        contentContainer.add(dashboardPanel, DASHBOARD_PANEL);
        
        clientesListPanel = new ClientesListPanel(mainFrame);
        contentContainer.add(clientesListPanel, CLIENTES_PANEL);
        
        // Adicionar componentes ao painel principal
        mainPanel.add(sideMenu, BorderLayout.WEST);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        // Adicionar componentes à tela
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Painel de boas-vindas
        JPanel welcomePanel = criarPainelBoasVindas();
        JPanel welcomeContainer = new JPanel(new BorderLayout());
        welcomeContainer.setOpaque(false);
        welcomeContainer.add(welcomePanel, BorderLayout.NORTH);
        welcomeContainer.setBorder(new EmptyBorder(15, 0, 0, 15));
        
        // Mostrar dashboard por padrão
        contentCardLayout.show(contentContainer, DASHBOARD_PANEL);
    }
    
    /**
     * Cria o painel de cabeçalho
     */
    private JPanel criarPainelCabecalho() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        
        // Botão do menu
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
        
        // Título do aplicativo no centro
        JLabel lblTitle = new JLabel("HERMES");
        lblTitle.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblTitle.setFont(UIConstants.TITLE_FONT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Painel direito com perfil do usuário e botão de logout
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
    
    /**
     * Cria o painel de menu lateral
     */
    private JPanel criarMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(UIConstants.MENU_BACKGROUND);
        menu.setPreferredSize(UIConstants.MENU_WIDTH);
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.HEADER_BORDER_COLOR));
        
        // Título do menu
        JLabel lblMenuTitle = new JLabel("Menu Principal");
        lblMenuTitle.setForeground(UIConstants.TEXT_COLOR);
        lblMenuTitle.setFont(UIConstants.SUBTITLE_FONT);
        lblMenuTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblMenuTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        menu.add(lblMenuTitle);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Opção Dashboard
        JPanel dashboardOption = criarOpcaoMenu(
            "Dashboard", 
            IconManager.ICON_DASHBOARD, 
            e -> contentCardLayout.show(contentContainer, DASHBOARD_PANEL)
        );
        menu.add(dashboardOption);
        
        // Seção CLIENTES
        JPanel clientesHeader = criarCabecalhoMenu("CLIENTES");
        menu.add(clientesHeader);
        
        // Opção Listar clientes
        JPanel listarClientesOption = criarOpcaoMenu(
            "Clientes", 
            IconManager.ICON_LIST, 
            e -> {
                clientesListPanel.carregarClientes();
                contentCardLayout.show(contentContainer, CLIENTES_PANEL);
            }
        );
        menu.add(listarClientesOption);
        
        // Seção AGENDAMENTOS
        JPanel agendamentosHeader = criarCabecalhoMenu("AGENDAMENTOS");
        menu.add(agendamentosHeader);
        
        // Opção Novo Agendamento
        JPanel novoAgendamentoOption = criarOpcaoMenu(
            "Novo Agendamento", 
            IconManager.ICON_CALENDAR, 
            e -> {
                // Implementação futura
            }
        );
        menu.add(novoAgendamentoOption);
        
        // Espaçador no final
        menu.add(Box.createVerticalGlue());
        
        return menu;
    }
    
    /**
     * Cria um cabeçalho para seção do menu
     */
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
    
    /**
     * Cria uma opção do menu lateral
     */
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
        
        // Adicionar ícone se disponível
        ImageIcon icon = IconManager.loadIcon(iconName, 18, 18);
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addActionListener(action);
        
        option.add(button);
        option.add(Box.createHorizontalGlue());
        
        // Adicionar efeito hover
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
    
    /**
     * Alterna a visibilidade do menu lateral
     */
    private void alternarMenuLateral() {
        sideMenuVisible = !sideMenuVisible;
        sideMenu.setVisible(sideMenuVisible);
        revalidate();
        repaint();
    }
    
    /**
     * Cria o painel de dashboard
     */
    private JPanel criarPainelDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(UIConstants.PANEL_BACKGROUND);
        dashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        HeaderPanel headerPanel = new HeaderPanel("Dashboard");
        
        JPanel contentPanel = new JPanel(new java.awt.GridBagLayout());
        contentPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JLabel lblDashboard = new JLabel("DASHBOARD", SwingConstants.CENTER);
        lblDashboard.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblDashboard.setFont(UIConstants.TITLE_FONT);
        
        contentPanel.add(lblDashboard);
        
        dashboard.add(headerPanel, BorderLayout.NORTH);
        dashboard.add(contentPanel, BorderLayout.CENTER);
        
        return dashboard;
    }
    
    /**
     * Cria o painel de boas-vindas
     */
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
    
    /**
     * Mostra o painel de edição de cliente
     */
    public void mostrarPainelEditarCliente(Cliente cliente) {
        if (editarClientePanel != null) {
            contentContainer.remove(editarClientePanel);
        }
        
        editarClientePanel = new EditarClientePanel(cliente, this);
        contentContainer.add(editarClientePanel, EDITAR_CLIENTE_PANEL);
        contentCardLayout.show(contentContainer, EDITAR_CLIENTE_PANEL);
    }
    
    /**
     * Mostra o painel de listagem de clientes
     */
    public void mostrarPainelClientes() {
        clientesListPanel.carregarClientes();
        contentCardLayout.show(contentContainer, CLIENTES_PANEL);
    }
}