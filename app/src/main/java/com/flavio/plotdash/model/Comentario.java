package com.flavio.plotdash.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Comentario  implements Serializable {

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

    public String getFechaCreacionFormateada() {
        LocalDateTime dateTimeToCompare=this.fecha;

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
