package com.flavio.model;

import java.io.Serializable;

public class Genero  implements Serializable {
    private int idGenero;
    private String genero;

    public Genero(int idGenero) {
        this.idGenero = idGenero;
    }

    public Genero() {
    }

    public Genero(int idGenero, String genero) {
        this.idGenero = idGenero;
        this.genero = genero;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
