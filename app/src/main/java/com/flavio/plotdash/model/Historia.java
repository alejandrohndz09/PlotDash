package com.flavio.plotdash.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


public class Historia implements Serializable {
    private int idHistoria;
    private String titulo;
    private String descripcion;
    private Genero idGenero;
    private String portada;
    private LocalDateTime fecha_creacion;
    private Usuario idUsuario;
    private int vistas;
    private Double calificacion;
    private ArrayList <Comentario>  comentarios;

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Historia() {
    }

    public Historia(int idHistoria, String titulo, String descripcion, Genero idGenero, String portada, LocalDateTime fecha_creacion, Usuario idUsuario, int vistas, Double calificacion) {
        this.idHistoria = idHistoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idGenero = idGenero;
        this.portada = portada;
        this.fecha_creacion = fecha_creacion;
        this.idUsuario = idUsuario;
        this.vistas = vistas;
        this.calificacion = calificacion;
    }

    public Historia(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    public int getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Genero getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Genero idGenero) {
        this.idGenero = idGenero;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }
}