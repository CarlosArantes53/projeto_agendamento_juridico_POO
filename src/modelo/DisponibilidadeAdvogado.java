package modelo;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DisponibilidadeAdvogado {
    private int id;
    private int idUsuario;
    private int diaSemana;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    
    public DisponibilidadeAdvogado() {
    }
    
    public DisponibilidadeAdvogado(int idUsuario, int diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        this.idUsuario = idUsuario;
        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }
    
    public DisponibilidadeAdvogado(int id, int idUsuario, int diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
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

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }
    
    public String getNomeDiaSemana() {
        switch (diaSemana) {
            case 0: return "Domingo";
            case 1: return "Segunda-feira";
            case 2: return "Terça-feira";
            case 3: return "Quarta-feira";
            case 4: return "Quinta-feira";
            case 5: return "Sexta-feira";
            case 6: return "Sábado";
            default: return "Dia inválido";
        }
    }
    
    public DayOfWeek getDayOfWeek() {
        return switch (diaSemana) {
            case 0 -> DayOfWeek.SUNDAY;
            case 1 -> DayOfWeek.MONDAY;
            case 2 -> DayOfWeek.TUESDAY;
            case 3 -> DayOfWeek.WEDNESDAY;
            case 4 -> DayOfWeek.THURSDAY;
            case 5 -> DayOfWeek.FRIDAY;
            case 6 -> DayOfWeek.SATURDAY;
            default -> throw new IllegalArgumentException("Dia da semana inválido: " + diaSemana);
        };
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s - %s", 
                getNomeDiaSemana(), 
                horarioInicio.toString(), 
                horarioFim.toString());
    }
}