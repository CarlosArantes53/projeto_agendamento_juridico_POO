package gui.agendamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.AgendamentoDAO;
import dao.UsuarioDAO;
import gui.MainFrame;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.util.IconManager;
import gui.util.UIConstants;
import modelo.Agendamento;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;

public class CalendarioPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    private LocalDate mesAtual;
    private JPanel calendarPanel;
    private JLabel lblMesAno;
    private JComboBox<Usuario> cbAdvogado;
    private JPanel agendamentosPanel;
    
    private AgendamentoDAO agendamentoDAO;
    private UsuarioDAO usuarioDAO;
    private MainFrame mainFrame;
    
    private Map<LocalDate, List<Agendamento>> agendamentosPorDia;
    
    public CalendarioPanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        this.agendamentoDAO = new AgendamentoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.mesAtual = LocalDate.now();
        this.agendamentosPorDia = new HashMap<>();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Calendário de Agendamentos");
        headerPanel.addButton("Novo Agendamento", IconManager.ICON_ADD, e -> novoAgendamento());
        headerPanel.addButton("Voltar", IconManager.ICON_CANCEL, e -> mainFrame.voltarParaHome());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JPanel controlPanel = criarPainelControle();
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JPanel weekDaysPanel = criarPainelDiasSemana();
        calendarPanel.add(weekDaysPanel, BorderLayout.NORTH);
        
        JPanel daysPanel = new JPanel(new GridLayout(6, 7));
        daysPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        daysPanel.setBorder(BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR, 1));
        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        
        agendamentosPanel = new JPanel();
        agendamentosPanel.setLayout(new BoxLayout(agendamentosPanel, BoxLayout.Y_AXIS));
        agendamentosPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        agendamentosPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR),
                "Agendamentos do Dia",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                UIConstants.SUBTITLE_FONT,
                UIConstants.TEXT_COLOR
        ));
        agendamentosPanel.setPreferredSize(new Dimension(300, 0));
        
        JScrollPane scrollPane = new JScrollPane(agendamentosPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);
        
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        contentPanel.add(scrollPane, BorderLayout.EAST);
        
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        carregarAdvogados();
        atualizarCalendario();
    }
    
    private JPanel criarPainelControle() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JButton btnMesAnterior = new JButton("<<");
        UIConstants.setupSecondaryButton(btnMesAnterior, "<<");
        btnMesAnterior.addActionListener(e -> mesAnterior());
        
        lblMesAno = new JLabel();
        lblMesAno.setFont(UIConstants.SUBTITLE_FONT);
        lblMesAno.setForeground(UIConstants.TEXT_COLOR);
        lblMesAno.setHorizontalAlignment(SwingConstants.CENTER);
        lblMesAno.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        JButton btnProximoMes = new JButton(">>");
        UIConstants.setupSecondaryButton(btnProximoMes, ">>");
        btnProximoMes.addActionListener(e -> proximoMes());
        
        navPanel.add(btnMesAnterior);
        navPanel.add(lblMesAno);
        navPanel.add(btnProximoMes);
        
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filtroPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JLabel lblAdvogado = new JLabel("Advogado:");
        lblAdvogado.setForeground(UIConstants.TEXT_COLOR);
        
        cbAdvogado = new JComboBox<>();
        cbAdvogado.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Usuario) {
                    setText(((Usuario) value).getNome());
                } else if (value == null) {
                    setText("Todos os advogados");
                }
                
                return this;
            }
        });
        
        cbAdvogado.addActionListener(e -> atualizarCalendario());
        
        filtroPanel.add(lblAdvogado);
        filtroPanel.add(cbAdvogado);
        
        panel.add(navPanel, BorderLayout.CENTER);
        
        // Adicionar o filtro de advogados apenas se for secretário
        if (usuarioLogado.getTipoUsuario() == TipoUsuario.SECRETARIO) {
            panel.add(filtroPanel, BorderLayout.EAST);
        }
        
        return panel;
    }
    
    private JPanel criarPainelDiasSemana() {
        JPanel panel = new JPanel(new GridLayout(1, 7));
        panel.setBackground(UIConstants.HEADER_COLOR);
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIConstants.HEADER_BORDER_COLOR));
        
        DayOfWeek[] diasSemana = {
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        };
        
        for (DayOfWeek dia : diasSemana) {
            JLabel label = new JLabel(dia.getDisplayName(TextStyle.SHORT, new Locale("pt", "BR")).toUpperCase());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setForeground(UIConstants.TEXT_COLOR);
            label.setFont(UIConstants.LABEL_FONT);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            if (dia == DayOfWeek.SUNDAY) {
                label.setForeground(UIConstants.DANGER_BUTTON_COLOR);
            }
            
            panel.add(label);
        }
        
        return panel;
    }
    
    private void atualizarCalendario() {
        YearMonth yearMonth = YearMonth.of(mesAtual.getYear(), mesAtual.getMonth());
        LocalDate primeiroDiaMes = yearMonth.atDay(1);
        LocalDate ultimoDiaMes = yearMonth.atEndOfMonth();
        
        // Atualizar label do mês/ano
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"));
        lblMesAno.setText(mesAtual.format(formatter));
        
        // Remover dias antigos
        JPanel daysPanel = (JPanel) calendarPanel.getComponent(1);
        daysPanel.removeAll();
        
        int diaSemanaInicio = primeiroDiaMes.getDayOfWeek().getValue() % 7; // Ajuste para domingo = 0
        
        // Carregar agendamentos do mês
        try {
            carregarAgendamentosMes(yearMonth);
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar agendamentos: " + e.getMessage());
        }
        
        // Adicionar células vazias antes do primeiro dia
        for (int i = 0; i < diaSemanaInicio; i++) {
            JPanel dayPanel = criarPainelDiaVazio();
            daysPanel.add(dayPanel);
        }
        
        // Adicionar dias do mês
        for (int dia = 1; dia <= ultimoDiaMes.getDayOfMonth(); dia++) {
            final LocalDate data = LocalDate.of(mesAtual.getYear(), mesAtual.getMonth(), dia);
            JPanel dayPanel = criarPainelDia(dia, data);
            daysPanel.add(dayPanel);
        }
        
        // Preencher células restantes
        int totalCells = 42; // 6 semanas * 7 dias
        int diasAdicionados = diaSemanaInicio + ultimoDiaMes.getDayOfMonth();
        for (int i = diasAdicionados; i < totalCells; i++) {
            JPanel dayPanel = criarPainelDiaVazio();
            daysPanel.add(dayPanel);
        }
        
        // Limpar painel de agendamentos
        agendamentosPanel.removeAll();
        JLabel lblSelecioneData = new JLabel("Selecione uma data para ver os agendamentos");
        lblSelecioneData.setForeground(UIConstants.TEXT_SECONDARY);
        lblSelecioneData.setAlignmentX(Component.CENTER_ALIGNMENT);
        agendamentosPanel.add(Box.createVerticalGlue());
        agendamentosPanel.add(lblSelecioneData);
        agendamentosPanel.add(Box.createVerticalGlue());
        
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
    
    private JPanel criarPainelDia(int dia, LocalDate data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.ROW_EVEN);
        panel.setBorder(BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR, 1));
        
        boolean isDomingo = data.getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean isHoje = data.equals(LocalDate.now());
        
        JLabel lblDia = new JLabel(String.valueOf(dia));
        lblDia.setHorizontalAlignment(SwingConstants.CENTER);
        lblDia.setFont(UIConstants.LABEL_FONT);
        lblDia.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        if (isDomingo) {
            lblDia.setForeground(UIConstants.DANGER_BUTTON_COLOR);
        } else {
            lblDia.setForeground(UIConstants.TEXT_COLOR);
        }
        
        if (isHoje) {
            panel.setBackground(UIConstants.PRIMARY_LIGHT);
            lblDia.setFont(UIConstants.SUBTITLE_FONT);
        }
        
        panel.add(lblDia, BorderLayout.NORTH);
        
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(panel.getBackground());
        
        // Verificar se tem agendamentos neste dia
        if (agendamentosPorDia.containsKey(data)) {
            List<Agendamento> agendamentosDia = agendamentosPorDia.get(data);
            if (!agendamentosDia.isEmpty()) {
                // Mostrar indicador de agendamentos
                int qtdAgendamentos = agendamentosDia.size();
                
                JLabel lblIndicador = new JLabel("" + qtdAgendamentos + " agend.");
                lblIndicador.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
                lblIndicador.setForeground(Color.WHITE);
                lblIndicador.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblIndicador.setBackground(Color.decode(agendamentosDia.get(0).getCor()));
                lblIndicador.setOpaque(true);
                lblIndicador.setBorder(new EmptyBorder(2, 5, 2, 5));
                
                eventPanel.add(lblIndicador);
            }
        }
        
        panel.add(eventPanel, BorderLayout.CENTER);
        
        // Adicionar evento de clique para mostrar agendamentos
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarAgendamentosDia(data);
            }
        });
        
        return panel;
    }
    
    private JPanel criarPainelDiaVazio() {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR, 1));
        return panel;
    }
    
    private void carregarAgendamentosMes(YearMonth yearMonth) throws SQLException {
        agendamentosPorDia.clear();
        
        LocalDate dataInicio = yearMonth.atDay(1);
        LocalDate dataFim = yearMonth.atEndOfMonth();
        
        List<Agendamento> agendamentos;
        
        // Se for advogado, sempre filtrar apenas pelos próprios agendamentos
        if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADVOGADO) {
            agendamentos = agendamentoDAO.listarPorAdvogado(
                    usuarioLogado.getId(), dataInicio, dataFim);
        } else {
            // Para secretário, usar o filtro selecionado
            Usuario advogadoSelecionado = (Usuario) cbAdvogado.getSelectedItem();
            
            if (advogadoSelecionado == null) {
                // Carregar todos os agendamentos
                agendamentos = agendamentoDAO.listarPorPeriodo(dataInicio, dataFim);
            } else {
                // Carregar apenas agendamentos do advogado selecionado
                agendamentos = agendamentoDAO.listarPorAdvogado(
                        advogadoSelecionado.getId(), dataInicio, dataFim);
            }
        }
        
        // Agrupar agendamentos por dia
        for (Agendamento agendamento : agendamentos) {
            LocalDate data = agendamento.getDataAtendimento();
            
            if (!agendamentosPorDia.containsKey(data)) {
                agendamentosPorDia.put(data, new java.util.ArrayList<>());
            }
            
            agendamentosPorDia.get(data).add(agendamento);
        }
    }
    
    private void mostrarAgendamentosDia(LocalDate data) {
        agendamentosPanel.removeAll();
        
        JLabel lblData = new JLabel(
                data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                SwingConstants.CENTER);
        lblData.setForeground(UIConstants.TEXT_COLOR);
        lblData.setFont(UIConstants.SUBTITLE_FONT);
        lblData.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblData.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        agendamentosPanel.add(lblData);
        
        if (agendamentosPorDia.containsKey(data) && !agendamentosPorDia.get(data).isEmpty()) {
            List<Agendamento> agendamentos = agendamentosPorDia.get(data);
            
            for (Agendamento agendamento : agendamentos) {
                JPanel agendamentoPanel = criarPainelAgendamento(agendamento);
                agendamentosPanel.add(agendamentoPanel);
                agendamentosPanel.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel lblSemAgendamentos = new JLabel("Não há agendamentos para este dia");
            lblSemAgendamentos.setForeground(UIConstants.TEXT_SECONDARY);
            lblSemAgendamentos.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            agendamentosPanel.add(Box.createVerticalGlue());
            agendamentosPanel.add(lblSemAgendamentos);
            agendamentosPanel.add(Box.createVerticalGlue());
        }
        
        agendamentosPanel.revalidate();
        agendamentosPanel.repaint();
    }
    
    private JPanel criarPainelAgendamento(Agendamento agendamento) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.ROW_EVEN);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode(agendamento.getCor()), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String horario = agendamento.getHoraInicio().format(formatter) + " - " +
                        agendamento.getHoraFim().format(formatter);
        
        JLabel lblHorario = new JLabel(horario);
        lblHorario.setForeground(UIConstants.TEXT_COLOR);
        lblHorario.setFont(UIConstants.SUBTITLE_FONT);
        lblHorario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblTipo = new JLabel(agendamento.getNomeTipoAtendimento());
        lblTipo.setForeground(UIConstants.TEXT_SECONDARY);
        lblTipo.setFont(UIConstants.LABEL_FONT);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCliente = new JLabel("Cliente: " + agendamento.getNomeCliente());
        lblCliente.setForeground(UIConstants.TEXT_COLOR);
        lblCliente.setFont(UIConstants.LABEL_FONT);
        lblCliente.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblAdvogado = new JLabel("Advogado: " + agendamento.getNomeAdvogado());
        lblAdvogado.setForeground(UIConstants.TEXT_COLOR);
        lblAdvogado.setFont(UIConstants.LABEL_FONT);
        lblAdvogado.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblStatus = new JLabel("Status: " + agendamento.getStatus());
        lblStatus.setForeground(getCorStatus(agendamento.getStatus()));
        lblStatus.setFont(UIConstants.LABEL_FONT);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lblHorario);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblTipo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblCliente);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblAdvogado);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblStatus);
        
        // Adicionar evento de clique para abrir detalhes do agendamento
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.mostrarPainelDetalhesAgendamento(agendamento);
            }
        });
        
        return panel;
    }
    
    private Color getCorStatus(Agendamento.Status status) {
        switch (status) {
            case AGENDADO:
                return UIConstants.PRIMARY_BUTTON_COLOR;
            case CONFIRMADO:
                return UIConstants.SECONDARY_BUTTON_COLOR;
            case CANCELADO:
                return UIConstants.DANGER_BUTTON_COLOR;
            case REALIZADO:
                return new Color(40, 180, 40);
            default:
                return UIConstants.TEXT_COLOR;
        }
    }
    
    private void proximoMes() {
        mesAtual = mesAtual.plusMonths(1);
        atualizarCalendario();
    }
    
    private void mesAnterior() {
        mesAtual = mesAtual.minusMonths(1);
        atualizarCalendario();
    }
    
    private void carregarAdvogados() {
        try {
            List<Usuario> advogados = usuarioDAO.listarPorTipo(TipoUsuario.ADVOGADO);
            
            // Para usuários tipo SECRETARIO, permitir filtrar por advogado
            if (usuarioLogado.getTipoUsuario() == TipoUsuario.SECRETARIO) {
                DefaultComboBoxModel<Usuario> model = new DefaultComboBoxModel<>();
                model.addElement(null); // Opção "Todos os advogados"
                
                for (Usuario advogado : advogados) {
                    model.addElement(advogado);
                }
                
                cbAdvogado.setModel(model);
            } 
            // Para usuários tipo ADVOGADO, forçar visualização apenas da própria agenda
            else if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADVOGADO) {
                // Não exibimos o combobox para advogados, mas podemos configurá-lo internamente
                // para usar a lógica de carregamento de agendamentos
                DefaultComboBoxModel<Usuario> model = new DefaultComboBoxModel<>();
                model.addElement(usuarioLogado);
                cbAdvogado.setModel(model);
                cbAdvogado.setSelectedItem(usuarioLogado);
            }
            
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar advogados: " + e.getMessage());
        }
    }
    
    private void novoAgendamento() {
        mainFrame.mostrarPainelAgendamento();
    }
}