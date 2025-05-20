package modelo;

import java.awt.Color;

public class TipoAtendimento {
    private int id;
    private String nome;
    private int duracaoPadrao; // Em minutos
    private String cor;
    
    public TipoAtendimento() {
    }
    
    public TipoAtendimento(String nome, int duracaoPadrao, String cor) {
        this.nome = nome;
        this.duracaoPadrao = duracaoPadrao;
        this.cor = cor;
    }
    
    public TipoAtendimento(int id, String nome, int duracaoPadrao, String cor) {
        this.id = id;
        this.nome = nome;
        this.duracaoPadrao = duracaoPadrao;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuracaoPadrao() {
        return duracaoPadrao;
    }

    public void setDuracaoPadrao(int duracaoPadrao) {
        this.duracaoPadrao = duracaoPadrao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
    
    public Color getColor() {
        try {
            return Color.decode(cor);
        } catch (NumberFormatException e) {
            // Fallback para cor padrão
            return Color.BLUE;
        }
    }
    
    @Override
    public String toString() {
        return nome;
    }
}