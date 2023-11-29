package com.flavio.plotdash.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public String getFechaCreacionFormateada() {
        LocalDateTime dateTimeToCompare=this.fecha_creado;

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

    @Override
    public String toString() {
        return "Capitulo{" +
                "idCapitulo=" + idCapitulo +
                ", idHistoria=" + idHistoria +
                ", fecha_creado=" + fecha_creado +
                ", titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", num=" + num +
                '}';
    }
}
