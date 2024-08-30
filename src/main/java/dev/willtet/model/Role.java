package dev.willtet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Role {
    private Integer id;
    private String nome;
    private Integer minRange;
    private Integer maxRange;
    private String idDiscord;

    public Role(Integer id, String nome, Integer minRange, Integer maxRange, String idDiscord) {
        this.id = id;
        this.nome = nome;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.idDiscord = idDiscord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getMinRange() {
        return minRange;
    }

    public void setMinRange(Integer minRange) {
        this.minRange = minRange;
    }

    public Integer getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Integer maxRange) {
        this.maxRange = maxRange;
    }

    public String getIdDiscord() {
        return idDiscord;
    }

    public void setIdDiscord(String idDiscord) {
        this.idDiscord = idDiscord;
    }
}
