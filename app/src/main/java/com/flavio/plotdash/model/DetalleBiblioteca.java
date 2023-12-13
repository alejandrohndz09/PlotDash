package com.flavio.plotdash.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DetalleBiblioteca {
    private int idDetalle;
    private Historia idHistoria;
    private int idBiblioteca;
    private LocalDateTime fecha_creado;

    public DetalleBiblioteca() {
    }

    public DetalleBiblioteca(int idDetalle, int idBiblioteca, LocalDateTime fecha_creado) {
        this.idDetalle = idDetalle;
        this.idBiblioteca = idBiblioteca;
        this.fecha_creado = fecha_creado;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Historia getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Historia idHistoria) {
        this.idHistoria = idHistoria;
    }

    public int getIdBiblioteca() {
        return idBiblioteca;
    }

    public void setIdBiblioteca(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
    }

    public LocalDateTime getFecha_creado() {
        return fecha_creado;
    }

    public void setFecha_creado(LocalDateTime fecha_creado) {
        this.fecha_creado = fecha_creado;
    }
    public  String getFechaCreacionFormateada() {
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
}
