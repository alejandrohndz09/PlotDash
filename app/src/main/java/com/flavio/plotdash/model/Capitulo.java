package com.flavio.plotdash.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Capitulo  implements Serializable {
    private int idCapitulo;
    private Historia idHistoria;
    private LocalDateTime fecha_creado;
    private String titulo;
    private String contenido;
    private int num;

    public Capitulo(int idCapitulo) {
        this.idCapitulo = idCapitulo;
    }

    public Capitulo() {
    }

    public Capitulo(int idCapitulo, Historia idHistoria, LocalDateTime fecha_creado, String titulo, String contenido, int num) {
        this.idCapitulo = idCapitulo;
        this.idHistoria = idHistoria;
        this.fecha_creado = fecha_creado;
        this.titulo = titulo;
        this.contenido = contenido;
        this.num = num;
    }

    public int getIdCapitulo() {
        return idCapitulo;
    }

    public void setIdCapitulo(int idCapitulo) {
        this.idCapitulo = idCapitulo;
    }

    public Historia getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Historia idHistoria) {
        this.idHistoria = idHistoria;
    }

    public LocalDateTime getFecha_creado() {
        return fecha_creado;
    }

    public void setFecha_creado(LocalDateTime fecha_creado) {
        this.fecha_creado = fecha_creado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
