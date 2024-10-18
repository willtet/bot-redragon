package dev.willtet.model;

import java.util.Date;

public class Publicacao {
    private Long id;
    private String idMensagem;
    private String idUsuario;
    private boolean valido;
    private String mensagem;
    private int pontoPeso;
    private Date dataEntrada;

    public Publicacao(Long id, String idMensagem, String idUsuario, boolean valido, String mensagem, int pontoPeso, Date dataEntrada) {
        this.id = id;
        this.idMensagem = idMensagem;
        this.idUsuario = idUsuario;
        this.valido = valido;
        this.mensagem = mensagem;
        this.pontoPeso = pontoPeso;
        this.dataEntrada = dataEntrada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getPontoPeso() {
        return pontoPeso;
    }

    public void setPontoPeso(int pontoPeso) {
        this.pontoPeso = pontoPeso;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
}
