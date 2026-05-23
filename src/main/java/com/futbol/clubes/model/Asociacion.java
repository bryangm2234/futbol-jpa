package com.futbol.clubes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asociaciones")
public class Asociacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String pais;
    private String presidente;

    // ── Constructores ──────────────────────────────────────────────
    public Asociacion() {}

    public Asociacion(String nombre, String pais, String presidente) {
        this.nombre     = nombre;
        this.pais       = pais;
        this.presidente = presidente;
    }

    // ── Getters y Setters ──────────────────────────────────────────
    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getNombre()                 { return nombre; }
    public void setNombre(String nombre)      { this.nombre = nombre; }

    public String getPais()                   { return pais; }
    public void setPais(String pais)          { this.pais = pais; }

    public String getPresidente()             { return presidente; }
    public void setPresidente(String pres)    { this.presidente = pres; }

    @Override
    public String toString() {
        return nombre + " (" + pais + ")";
    }
}
