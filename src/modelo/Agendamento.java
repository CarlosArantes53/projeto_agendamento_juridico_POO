package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Agendamento {
    
    public enum Status {
        AGENDADO,
        CONFIRMADO,
        CANCELADO,
        REALIZADO
    }
    
    private int id;
    private LocalDate dataAtendimento;
    private LocalTime horaInicio;
    private int duracao; // Em minutos
    private int idCliente;
    private String nomeCliente; // Para facilitar a exibição
    private int idAdvogado;
    private String nomeAdvogado; // Para facilitar a exibição
    private int idTipoAtendimento;
    private String nomeTipoAtendimento; // Para facilitar a exibição
    private String descricao;
    private Status status;
    private int idUsuarioCriador;
    private String cor; // Cor associada ao tipo de atendimento
    
    public Agendamento() {
    }
    
    public Agendamento(LocalDate dataAtendimento, LocalTime horaInicio, int duracao, 
            int idCliente, int idAdvogado, int idTipoAtendimento, 
            String descricao, Status status, int idUsuarioCriador) {
        this.dataAtendimento = dataAtendimento;
        this.horaInicio = horaInicio;
        this.duracao = duracao;
        this.idCliente = idCliente;
        this.idAdvogado = idAdvogado;
        this.idTipoAtendimento = idTipoAtendimento;
        this.descricao = descricao;
        this.status = status;
        this.idUsuarioCriador = idUsuarioCriador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(LocalDate dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalTime getHoraFim() {
        return horaInicio.plusMinutes(duracao);
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public int getIdAdvogado() {
        return idAdvogado;
    }

    public void setIdAdvogado(int idAdvogado) {
        this.idAdvogado = idAdvogado;
    }

    public String getNomeAdvogado() {
        return nomeAdvogado;
    }

    public void setNomeAdvogado(String nomeAdvogado) {
        this.nomeAdvogado = nomeAdvogado;
    }

    public int getIdTipoAtendimento() {
        return idTipoAtendimento;
    }

    public void setIdTipoAtendimento(int idTipoAtendimento) {
        this.idTipoAtendimento = idTipoAtendimento;
    }

    public String getNomeTipoAtendimento() {
        return nomeTipoAtendimento;
    }

    public void setNomeTipoAtendimento(String nomeTipoAtendimento) {
        this.nomeTipoAtendimento = nomeTipoAtendimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getIdUsuarioCriador() {
        return idUsuarioCriador;
    }

    public void setIdUsuarioCriador(int idUsuarioCriador) {
        this.idUsuarioCriador = idUsuarioCriador;
    }
    
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        return String.format("%s - %s até %s: %s com %s (%s)",
                dataAtendimento.format(dateFormatter),
                horaInicio.format(timeFormatter),
                getHoraFim().format(timeFormatter),
                nomeTipoAtendimento,
                nomeCliente,
                status);
    }
}