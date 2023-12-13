package com.flavio.plotdash.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class Biblioteca {
    private int idBiblioteca,  idUsuario;

    private  String nombre;
    private LocalDateTime fecha_creacion;
    private int tipo;
    private ArrayList<DetalleBiblioteca> detalles;




    public Biblioteca() {
    }

    public Biblioteca(int idBiblioteca, int idUsuario, String nombre, LocalDateTime fecha_creacion, int tipo) {
        this.idBiblioteca = idBiblioteca;
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

    public ArrayList<DetalleBiblioteca> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<DetalleBiblioteca> detalles) {
        this.detalles = detalles;
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