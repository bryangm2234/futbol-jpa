package com.futbol.clubes.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private int numero;
    private String posicion;

    // La FK id_club la gestiona Club con @JoinColumn
    // Se define aquí solo para mapeo bidireccional (opcional, sin @ManyToOne explícito
    // porque el documento no lo requiere en Jugador). Se deja el campo simple.

    // ── Constructores ──────────────────────────────────────────────
    public Jugador() {}

    public Jugador(String nombre, String apellido, int numero, String posicion) {
        this.nombre   = nombre;
        this.apellido = apellido;
        this.numero   = numero;
        this.posicion = posicion;
    }

    // ── Getters y Setters ──────────────────────────────────────────
    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    public String getApellido()            { return apellido; }
    public void setApellido(String ap)     { this.apellido = ap; }

    public int getNumero()                 { return numero; }
    public void setNumero(int numero)      { this.numero = numero; }

    public String getPosicion()            { return posicion; }
    public void setPosicion(String pos)    { this.posicion = pos; }

    @Override
    public String toString() {
        return "#" + numero + " " + nombre + " " + apellido + " (" + posicion + ")";
    }
}
