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

import modelo.Cliente;
import modelo.Usuario;
import util.IconManager;
import util.UIConstants;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    private JPanel sideMenu;
    private boolean sideMenuVisible = true;
    private JPanel contentContainer;
    private CardLayout contentCardLayout;
    
    private ClientesListPanel clientesListPanel;
    private EditarClientePanel editarClientePanel;
    
    private static final String DASHBOARD_PANEL = "dashboard";
    private static final String CLIENTES_PANEL = "clientes";
    private static final String EDITAR_CLIENTE_PANEL = "editarCliente";
    
    public HomePanel(MainFrame mainFrame, Usuario usuario) {
        this.usuarioLogado = usuario;
        
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        // HEADER PANEL
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        
        // Menu button
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
                toggleSideMenu();
            }
        });
        
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        leftHeaderPanel.setBackground(UIConstants.HEADER_COLOR);
        leftHeaderPanel.add(btnMenu);
        
        // App title in center
        JLabel lblTitle = new JLabel("HERMES");
        lblTitle.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblTitle.setFont(UIConstants.TITLE_FONT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // User profile and logout buttons in right corner
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
        
        rightHeaderPanel.add(lblUserName);
        rightHeaderPanel.add(btnProfile);
        rightHeaderPanel.add(btnLogout);
        
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        
        // MAIN CONTENT AREA
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Create and show side menu
        sideMenu = createSideMenu(mainFrame);
        
        // Content container with card layout
        contentCardLayout = new CardLayout();
        contentContainer = new JPanel(contentCardLayout);
        contentContainer.setBackground(UIConstants.PANEL_BACKGROUND);
        
        // Create default panels
        JPanel dashboardPanel = createDashboardPanel();
        contentContainer.add(dashboardPanel, DASHBOARD_PANEL);
        
        clientesListPanel = new ClientesListPanel(mainFrame);
        contentContainer.add(clientesListPanel, CLIENTES_PANEL);
        
        // Add main components to the panel
        mainPanel.add(sideMenu, BorderLayout.WEST);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        // Add main components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Welcome message panel in top-right corner
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
        
        // Welcome panel positioning
    // Welcome panel positioning
    JPanel welcomeContainer = new JPanel(new BorderLayout());
    welcomeContainer.setOpaque(false);
    welcomeContainer.add(welcomePanel, BorderLayout.NORTH);
    welcomeContainer.setBorder(new EmptyBorder(15, 0, 0, 15));

    // Show dashboard panel by default
    contentCardLayout.show(contentContainer, DASHBOARD_PANEL);
        
    }
    
    private JPanel createSideMenu(MainFrame mainFrame) {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(UIConstants.MENU_BACKGROUND);
        menu.setPreferredSize(UIConstants.MENU_WIDTH);
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.HEADER_BORDER_COLOR));
        
        // Menu title
        JLabel lblMenuTitle = new JLabel("Menu Principal");
        lblMenuTitle.setForeground(UIConstants.TEXT_COLOR);
        lblMenuTitle.setFont(UIConstants.SUBTITLE_FONT);
        lblMenuTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblMenuTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        menu.add(lblMenuTitle);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Dashboard option
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
        
        // CLIENTES section
        JPanel clientesHeader = createMenuHeader("CLIENTES");
        menu.add(clientesHeader);
        
        // Listar clientes option
        JPanel listarClientesOption = createMenuOption(
            "Clientes", 
            IconManager.ICON_LIST, 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clientesListPanel.carregarClientes();
                    contentCardLayout.show(contentContainer, CLIENTES_PANEL);
                }
            }
        );
        menu.add(listarClientesOption);
        
        // AGENDAMENTOS section
        JPanel agendamentosHeader = createMenuHeader("AGENDAMENTOS");
        menu.add(agendamentosHeader);
        
        // Novo Agendamento option
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
        
        // Spacer at bottom
        menu.add(Box.createVerticalGlue());
        
        return menu;
    }
    
    private JPanel createMenuHeader(String text) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(new Color(30, 30, 60));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setBorder(new EmptyBorder(5, 15, 5, 10));
        
        JLabel label = new JLabel(text);
        label.setForeground(UIConstants.MENU_HEADER_TEXT);
        label.setFont(UIConstants.MENU_HEADER_FONT);
        
        header.add(label);
        
        return header;
    }
    
    private JPanel createMenuOption(String text, String iconName, ActionListener action) {
        JPanel option = new JPanel();
        option.setLayout(new BoxLayout(option, BoxLayout.X_AXIS));
        option.setBackground(UIConstants.MENU_BACKGROUND);
        option.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        option.setBorder(new EmptyBorder(5, 15, 5, 10));
        
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(UIConstants.MENU_BACKGROUND);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(UIConstants.MENU_ITEM_FONT);
        
        // Add icon if available
        ImageIcon icon = IconManager.loadIcon(iconName, 18, 18);
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.addActionListener(action);
        
        option.add(button);
        option.add(Box.createHorizontalGlue());
        
        // Add hover effect
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
    
    private void toggleSideMenu() {
        sideMenuVisible = !sideMenuVisible;
        sideMenu.setVisible(sideMenuVisible);
        revalidate();
        repaint();
    }
    
    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new GridBagLayout());
        UIConstants.setupContentPanel(dashboard);
        
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