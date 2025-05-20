package modelo;

import java.time.LocalDate;

public class ExcecaoDisponibilidade {
    private int id;
    private int idUsuario;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String motivo;
    
    public ExcecaoDisponibilidade() {
    }
    
    public ExcecaoDisponibilidade(int idUsuario, LocalDate dataInicio, LocalDate dataFim, String motivo) {
        this.idUsuario = idUsuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.motivo = motivo;
    }
    
    public ExcecaoDisponibilidade(int id, int idUsuario, LocalDate dataInicio, LocalDate dataFim, String motivo) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.motivo = motivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    @Override
    public String toString() {
        return String.format("%s a %s: %s", 
                dataInicio.toString(), 
                dataFim.toString(), 
                motivo);
    }
}