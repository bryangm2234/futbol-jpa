package com.futbol.clubes.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "competiciones")
public class Competicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double montoPremio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // ── Constructores ──────────────────────────────────────────────
    public Competicion() {}

    public Competicion(String nombre, double montoPremio,
                       LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre      = nombre;
        this.montoPremio = montoPremio;
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
    }

    // ── Getters y Setters ──────────────────────────────────────────
    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getNombre()                    { return nombre; }
    public void setNombre(String nombre)         { this.nombre = nombre; }

    public double getMontoPremio()               { return montoPremio; }
    public void setMontoPremio(double monto)     { this.montoPremio = monto; }

    public LocalDate getFechaInicio()            { return fechaInicio; }
    public void setFechaInicio(LocalDate fecha)  { this.fechaInicio = fecha; }

    public LocalDate getFechaFin()               { return fechaFin; }
    public void setFechaFin(LocalDate fecha)     { this.fechaFin = fecha; }

    @Override
    public String toString() {
        return nombre;
    }
}
