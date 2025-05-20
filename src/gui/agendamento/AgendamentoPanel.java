package gui.agendamento;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

import dao.AgendamentoDAO;
import dao.ClienteDAO;
import dao.TipoAtendimentoDAO;
import dao.UsuarioDAO;
import gui.MainFrame;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.util.IconManager;
import gui.util.UIConstants;
import modelo.Agendamento;
import modelo.Agendamento.Status;
import modelo.Cliente;
import modelo.TipoAtendimento;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;

public class AgendamentoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    private AgendamentoDAO agendamentoDAO;
    private ClienteDAO clienteDAO;
    private UsuarioDAO usuarioDAO;
    private TipoAtendimentoDAO tipoAtendimentoDAO;
    
    private JFormattedTextField txtData;
    private JSpinner spnHora;
    private JSpinner spnMinuto;
    private JSpinner spnDuracao;
    private JComboBox<Cliente> cbCliente;
    private JComboBox<Usuario> cbAdvogado;
    private JComboBox<TipoAtendimento> cbTipoAtendimento;
    private JTextArea txtDescricao;
    
    private MainFrame mainFrame;
    
    public AgendamentoPanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        this.agendamentoDAO = new AgendamentoDAO();
        this.clienteDAO = new ClienteDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.tipoAtendimentoDAO = new TipoAtendimentoDAO();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Novo Agendamento");
        headerPanel.addButton("Calendário", IconManager.ICON_CALENDAR_B, e -> abrirCalendario());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel formPanel = criarFormularioAgendamento();
        
        mainPanel.add(formPanel);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        carregarDados();
    }
    
    private JPanel criarFormularioAgendamento() {
        FormPanel formPanel = new FormPanel();
        
        // Data - usando JFormattedTextField com máscara
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            
            txtData = new JFormattedTextField(dateMask);
            txtData.setColumns(10);
            txtData.setFont(UIConstants.LABEL_FONT);
            
            // Preencher com a data atual + 1 dia
            LocalDate amanha = LocalDate.now().plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtData.setValue(amanha.format(formatter));
            
        } catch (ParseException e) {
            e.printStackTrace();
            txtData = new JFormattedTextField();
        }
        
        formPanel.addComponent("Data:", txtData);
        
        // Hora e minuto
        JPanel panelHorario = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelHorario.setBackground(UIConstants.PANEL_BACKGROUND);
        
        spnHora = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        spnHora.setEditor(new JSpinner.NumberEditor(spnHora, "00"));
        spnMinuto = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        spnMinuto.setEditor(new JSpinner.NumberEditor(spnMinuto, "00"));
        
        JLabel lblSeparador = new JLabel(":");
        lblSeparador.setForeground(UIConstants.TEXT_COLOR);
        
        panelHorario.add(spnHora);
        panelHorario.add(lblSeparador);
        panelHorario.add(spnMinuto);
        
        formPanel.addComponent("Hora:", panelHorario);
        
        // Duração
        spnDuracao = new JSpinner(new SpinnerNumberModel(60, 15, 480, 15));
        formPanel.addComponent("Duração (minutos):", spnDuracao);
        
        // Cliente
        cbCliente = new JComboBox<>();
        formPanel.addComponent("Cliente:", cbCliente);
        
        // Advogado
        cbAdvogado = new JComboBox<>();
        formPanel.addComponent("Advogado:", cbAdvogado);
        
        // Tipo de atendimento
        cbTipoAtendimento = new JComboBox<>();
        cbTipoAtendimento.addActionListener(e -> atualizarDuracaoPadrao());
        formPanel.addComponent("Tipo de Atendimento:", cbTipoAtendimento);
        
        // Descrição
        txtDescricao = formPanel.addTextArea("Descrição/Observações:", null, 4);
        
        // Ações
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JButton btnAgendar = new JButton("Agendar");
        UIConstants.setupPrimaryButton(btnAgendar, "Agendar");
        btnAgendar.addActionListener(this::criarAgendamento);
        
        JButton btnCancelar = new JButton("Cancelar");
        UIConstants.setupDangerButton(btnCancelar, "Cancelar");
        btnCancelar.addActionListener(e -> mainFrame.voltarParaHome());
        
        btnPanel.add(btnAgendar);
        btnPanel.add(btnCancelar);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private void carregarDados() {
        try {
            // Carregar clientes
            List<Cliente> clientes = clienteDAO.listarTodos();
            DefaultComboBoxModel<Cliente> clienteModel = new DefaultComboBoxModel<>();
            
            for (Cliente cliente : clientes) {
                clienteModel.addElement(cliente);
            }
            
            cbCliente.setModel(clienteModel);
            
            // Configurar o ComboBox para mostrar apenas o nome do cliente
            cbCliente.setRenderer(new javax.swing.DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Cliente) {
                        setText(((Cliente) value).getNome());
                    }
                    return this;
                }
            });
            
            // Carregar advogados
            List<Usuario> advogados = usuarioDAO.listarPorTipo(TipoUsuario.ADVOGADO);
            DefaultComboBoxModel<Usuario> advogadoModel = new DefaultComboBoxModel<>();
            
            for (Usuario advogado : advogados) {
                advogadoModel.addElement(advogado);
            }
            
            cbAdvogado.setModel(advogadoModel);
            
            // Configurar o ComboBox para mostrar apenas o nome do advogado
            cbAdvogado.setRenderer(new javax.swing.DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Usuario) {
                        setText(((Usuario) value).getNome());
                    }
                    return this;
                }
            });
            
            // Carregar tipos de atendimento
            List<TipoAtendimento> tipos = tipoAtendimentoDAO.listarTodos();
            DefaultComboBoxModel<TipoAtendimento> tipoModel = new DefaultComboBoxModel<>();
            
            for (TipoAtendimento tipo : tipos) {
                tipoModel.addElement(tipo);
            }
            
            cbTipoAtendimento.setModel(tipoModel);
            
            // Se for um advogado, pré-selecionar ele mesmo e desabilitar seleção
            if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADVOGADO) {
                for (int i = 0; i < advogadoModel.getSize(); i++) {
                    if (advogadoModel.getElementAt(i).getId() == usuarioLogado.getId()) {
                        cbAdvogado.setSelectedIndex(i);
                        break;
                    }
                }
                // Desabilitar a seleção de advogado para usuários do tipo advogado
                cbAdvogado.setEnabled(false);
            }
            
            // Atualizar duração padrão baseada no tipo de atendimento selecionado
            atualizarDuracaoPadrao();
            
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    private void atualizarDuracaoPadrao() {
        TipoAtendimento tipoSelecionado = (TipoAtendimento) cbTipoAtendimento.getSelectedItem();
        if (tipoSelecionado != null) {
            spnDuracao.setValue(tipoSelecionado.getDuracaoPadrao());
        }
    }
    
    private void criarAgendamento(ActionEvent e) {
        try {
            String dataStr = txtData.getText();
            
            if (dataStr.contains("_")) {
                FormValidator.mostrarErro(this, "Preencha a data corretamente!");
                return;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataStr, formatter);
            
            int hora = (Integer) spnHora.getValue();
            int minuto = (Integer) spnMinuto.getValue();
            LocalTime horaInicio = LocalTime.of(hora, minuto);
            
            int duracao = (Integer) spnDuracao.getValue();
            
            Cliente cliente = (Cliente) cbCliente.getSelectedItem();
            Usuario advogado = (Usuario) cbAdvogado.getSelectedItem();
            TipoAtendimento tipo = (TipoAtendimento) cbTipoAtendimento.getSelectedItem();
            
            String descricao = txtDescricao.getText().trim();
            
            if (cliente == null || advogado == null || tipo == null) {
                FormValidator.mostrarErro(this, "Selecione cliente, advogado e tipo de atendimento!");
                return;
            }
            
            // Verificar disponibilidade do advogado
            LocalTime horaFim = horaInicio.plusMinutes(duracao);
            boolean disponivel = agendamentoDAO.verificarDisponibilidade(
                    advogado.getId(), data, horaInicio, horaFim);
            
            if (!disponivel) {
                FormValidator.mostrarErro(this, 
                        "O advogado não está disponível neste horário!\n" +
                        "Verifique a disponibilidade do advogado ou escolha outro horário.");
                return;
            }
            
            // Criar o agendamento
            Agendamento agendamento = new Agendamento(
                    data, horaInicio, duracao,
                    cliente.getId(), advogado.getId(), tipo.getId(),
                    descricao, Status.AGENDADO, usuarioLogado.getId());
            
            AgendamentoDAO.Resultado resultado = agendamentoDAO.inserir(agendamento);
            
            if (resultado.isSucesso()) {
                FormValidator.mostrarSucesso(this, 
                        "Agendamento criado com sucesso!\n" +
                        "O agendamento foi registrado e os envolvidos serão notificados.");
                
                mainFrame.voltarParaHome();
                mainFrame.mostrarPainelCalendario(); // Mostrar calendário atualizado
            } else {
                FormValidator.mostrarErro(this, "Erro ao criar agendamento: " + resultado.getMensagem());
            }
            
        } catch (Exception ex) {
            FormValidator.mostrarErro(this, "Erro ao processar dados: " + ex.getMessage());
        }
    }
    
    private void abrirCalendario() {
        mainFrame.mostrarPainelCalendario();
    }
}