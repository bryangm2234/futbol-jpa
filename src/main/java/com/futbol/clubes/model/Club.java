package com.futbol.clubes.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubes")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // ── @OneToOne con Entrenador ───────────────────────────────────
    // Club es propietario de la relación (tiene la FK entrenador_id).
    // Se usa LAZY para evitar carga innecesaria.
    @OneToOne(fetch = FetchType.LAZY,
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    @JoinColumn(name = "entrenador_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Entrenador entrenador;

    // ── @OneToMany con Jugador ─────────────────────────────────────
    // La columna id_club vive en la tabla jugadores como FK.
    // CascadeType.ALL permite persistir jugadores al guardar el club.
    @OneToMany(fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JoinColumn(name = "id_club")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Jugador> jugadores = new ArrayList<>();

    // ── @ManyToOne con Asociacion ──────────────────────────────────
    // La FK asociacion_id vive en la tabla clubes.
    // LAZY: solo se carga la asociación cuando se necesite.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asociacion_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Asociacion asociacion;

    // ── @ManyToMany con Competicion ────────────────────────────────
    // JPA crea la tabla intermedia clubes_competiciones automáticamente.
    // LAZY: solo se cargan las competiciones cuando se accedan.
    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "clubes_competiciones",
        joinColumns        = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "competicion_id")
    )
    private List<Competicion> competiciones = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────────
    public Club() {}

    public Club(String nombre) {
        this.nombre = nombre;
    }

    // ── Helpers ────────────────────────────────────────────────────
    public void addJugador(Jugador jugador) {
        this.jugadores.add(jugador);
    }

    public void addCompeticion(Competicion competicion) {
        this.competiciones.add(competicion);
    }

    // ── Getters y Setters ──────────────────────────────────────────
    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }

    public String getNombre()                      { return nombre; }
    public void setNombre(String nombre)           { this.nombre = nombre; }

    public Entrenador getEntrenador()              { return entrenador; }
    public void setEntrenador(Entrenador e)        { this.entrenador = e; }

    public List<Jugador> getJugadores()            { return jugadores; }
    public void setJugadores(List<Jugador> j)      { this.jugadores = j; }

    public Asociacion getAsociacion()              { return asociacion; }
    public void setAsociacion(Asociacion a)        { this.asociacion = a; }

    public List<Competicion> getCompeticiones()        { return competiciones; }
    public void setCompeticiones(List<Competicion> c)  { this.competiciones = c; }

    @Override
    public String toString() {
        return nombre;
    }
}
