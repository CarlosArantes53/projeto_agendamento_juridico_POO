package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.border.MatteBorder;

import modelo.Usuario;
import util.IconManager;
import util.UIConstants;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    private JPanel sideMenu;
    private boolean sideMenuVisible = false;
    private JPanel contentContainer;
    private CardLayout contentCardLayout;
    
    // Constantes para identificar os painéis de conteúdo
    private static final String DASHBOARD_PANEL = "dashboard";
    
    public HomePanel(MainFrame mainFrame, Usuario usuario) {
        this.usuarioLogado = usuario;
        
        // Set background color
        setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        
        setLayout(new BorderLayout());
        
        // ===========================================
        // Header panel with welcome message and user actions
        // ===========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        
        // Menu toggle button (left side)
        JButton btnMenu = new JButton();
        IconManager.setupIconButton(btnMenu, IconManager.ICON_MENU, "Menu", 40);
        if (btnMenu.getIcon() == null) {
            // Fallback se o ícone não for carregado
            UIConstants.setupSecondaryButton(btnMenu, "☰");
            btnMenu.setPreferredSize(new Dimension(40, 40));
        }
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSideMenu();
            }
        });
        
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        leftHeaderPanel.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        leftHeaderPanel.add(btnMenu);
        
        // App title
        JLabel lblTitle = new JLabel("HERMES");
        lblTitle.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // User actions (right side)
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightHeaderPanel.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        
        JButton btnProfile = new JButton();
        IconManager.setupIconButton(btnProfile, IconManager.ICON_PROFILE, "Editar Perfil", 40);
        if (btnProfile.getIcon() == null) {
            // Fallback se o ícone não for carregado
            UIConstants.setupSecondaryButton(btnProfile, "👤");
            btnProfile.setPreferredSize(new Dimension(40, 40));
        }
        
        JButton btnLogout = new JButton();
        IconManager.setupIconButton(btnLogout, IconManager.ICON_LOGOUT, "Sair", 40);
        if (btnLogout.getIcon() == null) {
            // Fallback se o ícone não for carregado
            UIConstants.setupDangerButton(btnLogout, "✖");
            btnLogout.setPreferredSize(new Dimension(40, 40));
        }
        
        btnProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainelEditarPerfil();
            }
        });
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.fazerLogout();
            }
        });
        
        rightHeaderPanel.add(btnProfile);
        rightHeaderPanel.add(btnLogout);
        
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        
        // ===========================================
        // Main content area with side menu and content panels
        // ===========================================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        
        // Side menu (initially hidden, will be shown with button click)
        sideMenu = createSideMenu(mainFrame);
        sideMenu.setVisible(false);
        
        // Content container with card layout to switch between views
        contentCardLayout = new CardLayout();
        contentContainer = new JPanel(contentCardLayout);
        contentContainer.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        
        // Create default dashboard panel
        JPanel dashboardPanel = createDashboardPanel();
        contentContainer.add(dashboardPanel, DASHBOARD_PANEL);
        
        mainPanel.add(sideMenu, BorderLayout.WEST);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        // Add all main components to the panel
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Welcome message as a floating panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(0, 150, 136, 200));
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        
        JLabel lblWelcome = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        welcomePanel.add(lblWelcome);
        
        // Add welcome panel to top-right of content
        JPanel glassPane = new JPanel(new BorderLayout());
        glassPane.setOpaque(false);
        glassPane.add(welcomePanel, BorderLayout.NORTH);
        glassPane.setBorder(new EmptyBorder(20, 0, 0, 20));
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        contentWrapper.add(mainPanel, BorderLayout.CENTER);
        contentWrapper.add(glassPane, BorderLayout.EAST);
        
        add(contentWrapper, BorderLayout.CENTER);
    }
    
    /**
     * Cria o menu lateral com as opções de navegação
     */
    private JPanel createSideMenu(MainFrame mainFrame) {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(20, 20, 40));
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.HEADER_BORDER_COLOR));
        
        // Logo ou título do menu
        JLabel lblMenuTitle = new JLabel("Menu Principal");
        lblMenuTitle.setForeground(Color.WHITE);
        lblMenuTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblMenuTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblMenuTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        menu.add(lblMenuTitle);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Opção de Dashboard
        JPanel dashboardOption = createMenuOption(
            "Dashboard", 
            IconManager.ICON_DASHBOARD, 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentCardLayout.show(contentContainer, DASHBOARD_PANEL);
                }
            }
        );
        menu.add(dashboardOption);
        
        // Opção de Clientes
        JPanel clientesHeader = createMenuHeader("CLIENTES");
        menu.add(clientesHeader);
        
        // Opção de Cadastrar Cliente
        JPanel cadastrarClienteOption = createMenuOption(
            "Cadastrar Cliente", 
            IconManager.ICON_ADD, 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFrame.mostrarPainelCadastroCliente();
                }
            }
        );
        menu.add(cadastrarClienteOption);
        
        // Opção de Listar Clientes (para futura implementação)
        JPanel listarClientesOption = createMenuOption(
            "Listar Clientes", 
            IconManager.ICON_LIST, 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implementação futura
                    // mainFrame.mostrarPainelListarClientes();
                }
            }
        );
        menu.add(listarClientesOption);
        
        // Opção de Agendamentos (para futura implementação)
        JPanel agendamentosHeader = createMenuHeader("AGENDAMENTOS");
        menu.add(agendamentosHeader);
        
        JPanel novoAgendamentoOption = createMenuOption(
            "Novo Agendamento", 
            IconManager.ICON_CALENDAR, 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implementação futura
                }
            }
        );
        menu.add(novoAgendamentoOption);
        
        // Espaçador flexível para empurrar itens para o topo
        menu.add(Box.createVerticalGlue());
        
        return menu;
    }
    
    /**
     * Cria um cabeçalho de seção para o menu lateral
     */
    private JPanel createMenuHeader(String text) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(new Color(30, 30, 60));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setBorder(new EmptyBorder(5, 15, 5, 10));
        
        JLabel label = new JLabel(text);
        label.setForeground(new Color(150, 150, 200));
        label.setFont(new Font("Arial", Font.BOLD, 12));
        
        header.add(label);
        
        return header;
    }
    
    /**
     * Cria uma opção de menu com ícone e texto
     */
    private JPanel createMenuOption(String text, String iconName, ActionListener action) {
        JPanel option = new JPanel();
        option.setLayout(new BoxLayout(option, BoxLayout.X_AXIS));
        option.setBackground(new Color(20, 20, 40));
        option.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        option.setBorder(new EmptyBorder(5, 15, 5, 10));
        
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(20, 20, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Adiciona ícone se disponível
        ImageIcon icon = IconManager.loadIcon(iconName, 18, 18);
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addActionListener(action);
        
        option.add(button);
        option.add(Box.createHorizontalGlue());
        
        // Adiciona efeito hover
        option.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                option.setBackground(new Color(40, 40, 80));
                button.setBackground(new Color(40, 40, 80));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                option.setBackground(new Color(20, 20, 40));
                button.setBackground(new Color(20, 20, 40));
            }
        });
        
        return option;
    }
    
    /**
     * Alterna a visibilidade do menu lateral
     */
    private void toggleSideMenu() {
        sideMenuVisible = !sideMenuVisible;
        sideMenu.setVisible(sideMenuVisible);
        revalidate();
        repaint();
    }
    
    /**
     * Cria o painel de dashboard
     */
    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new GridBagLayout());
        dashboard.setBackground(UIConstants.BACKGROUND_COLOR_MENU);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        JLabel lblDashboard = new JLabel("DASHBOARD", SwingConstants.CENTER);
        lblDashboard.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblDashboard.setFont(UIConstants.TITLE_FONT);
        
        dashboard.add(lblDashboard, gbc);
        
        return dashboard;
    }
}