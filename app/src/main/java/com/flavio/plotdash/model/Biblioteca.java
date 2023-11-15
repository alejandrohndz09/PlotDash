package com.flavio.plotdash.model;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Biblioteca {
    private int idBiblioteca, idHistoria, idUsuario;

    private  String nombre;
    private LocalDateTime fecha_creacion;
    private int tipo;




    public Biblioteca() {
    }

    public Biblioteca(int idBiblioteca, int idHistoria, int idUsuario, String nombre, LocalDateTime fecha_creacion, int tipo) {
        this.idBiblioteca = idBiblioteca;
        this.idHistoria = idHistoria;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.tipo = tipo;
    }

    public int getIdBiblioteca() {
        return idBiblioteca;
    }

    public void setIdBiblioteca(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
    }

    public int getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}