package com.flavio.plotdash.model;

import java.time.LocalDateTime;

public class Comentario {

    private int idComentario;
    private Usuario idUsuario;
    private Historia idHistoria;
    private LocalDateTime fecha;
    private String comentario;

    public Comentario() {
    }

    public Comentario(int idComentario, Usuario idUsuario, Historia idHistoria, LocalDateTime fecha, String comentario) {
        this.idComentario = idComentario;
        this.idUsuario = idUsuario;
        this.idHistoria = idHistoria;
        this.fecha = fecha;
        this.comentario = comentario;
    }


    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Historia getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Historia idHistoria) {
        this.idHistoria = idHistoria;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha= fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
