package dev.willtet.model.vo;

public class TopRankVO {
    private Integer pontos;
    private String nome;

    public TopRankVO(Integer pontos, String nome) {
        this.pontos = pontos;
        this.nome = nome;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
