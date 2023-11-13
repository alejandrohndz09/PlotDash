package com.flavio.plotdash.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;


public class Historia implements Serializable {
    private int idHistoria;
    private String titulo;
    private String descripcion;
    private Genero idGenero;
    private String portada;
    private LocalDateTime fecha_creacion;

    public ArrayList<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(ArrayList<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    private Usuario idUsuario;
    private int vistas;
    private int estado;
    private Double calificacion;
    private ArrayList<Capitulo> capitulos;

    public Historia() {
    }

    public Historia(int idHistoria, String titulo, String descripcion, Genero idGenero, String portada, LocalDateTime fecha_creacion, Usuario idUsuario, int vistas, int estado, Double calificacion) {
        this.idHistoria = idHistoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idGenero = idGenero;
        this.portada = portada;
        this.fecha_creacion = fecha_creacion;
        this.idUsuario = idUsuario;
        this.vistas = vistas;
        this.estado = estado;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int vistas) {
        this.estado = estado;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public  String getFechaCreacionFormateada() {
        LocalDateTime dateTimeToCompare=this.fecha_creacion;

        ZoneId timeZone = ZoneId.of("America/El_Salvador");
        ZonedDateTime now = ZonedDateTime.now(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTimeToCompare, timeZone);
        Duration duration = Duration.between(zonedDateTime, now);

        if (duration.toDays() < 1) {
            if (duration.toMinutes() < 1) {
                return "unos momentos";
            } else if (duration.toHours() < 1) {
                long minutes = duration.toMinutes();
                return minutes + "min";
            }else {
                long hours = duration.toHours();
                return hours + "h";
            }
        } else if (zonedDateTime.toLocalDate().getYear() < now.toLocalDate().getYear()) {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("d 'de' MMM", new Locale("es", "ES")));
        } else {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("d 'de' MMM", new Locale("es", "ES")));
        }
    }
}