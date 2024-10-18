package dev.willtet.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


public class User {
    private String id;
    private String name;
    private String username;

    private Date dataEntrada;

    public User(String id, String name, String username, Date dataEntrada) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.dataEntrada = dataEntrada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
}
