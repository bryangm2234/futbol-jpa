package com.futbol.clubes.controller;

import com.futbol.clubes.model.*;
import com.futbol.clubes.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador MVC para la gestión de Clubes.
 * Sin capa de Service: accede directamente a los repositorios JPA.
 */
@Controller
public class ClubController {

    // ── Repositorios ───────────────────────────────────────────────
    private final ClubRepository        clubRepository;
    private final EntrenadorRepository  entrenadorRepository;
    private final AsociacionRepository  asociacionRepository;
    private final CompeticionRepository competicionRepository;

    public ClubController(ClubRepository clubRepository,
                          EntrenadorRepository entrenadorRepository,
                          AsociacionRepository asociacionRepository,
                          CompeticionRepository competicionRepository) {
        this.clubRepository        = clubRepository;
        this.entrenadorRepository  = entrenadorRepository;
        this.asociacionRepository  = asociacionRepository;
        this.competicionRepository = competicionRepository;
    }

    // ── GET / → página de aterrizaje ──────────────────────────────
    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    // ── GET /clubes → redirige a /clubes/nuevo ────────────────────
    @GetMapping("/clubes")
    public String index() {
        return "redirect:/clubes/nuevo";
    }

    // ── GET /clubes/nuevo → formulario de registro ────────────────
    @GetMapping("/clubes/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("club",          new Club());
        model.addAttribute("entrenadores",  entrenadorRepository.findAll());
        model.addAttribute("asociaciones",  asociacionRepository.findAll());
        model.addAttribute("competiciones", competicionRepository.findAll());
        model.addAttribute("nuevoEntrenador",  new Entrenador());
        model.addAttribute("nuevaAsociacion",  new Asociacion());
        model.addAttribute("nuevaCompeticion", new Competicion());
        return "index";
    }

    // ── POST /clubes/guardar → guarda el club ─────────────────────
    @PostMapping("/clubes/guardar")
    public String guardarClub(
            @RequestParam("nombre")                                    String nombre,
            @RequestParam(value = "entrenadorId",   required = false)  Long entrenadorId,
            @RequestParam(value = "asociacionId",   required = false)  Long asociacionId,
            @RequestParam(value = "competicionIds", required = false)  List<Long> competicionIds,
            @RequestParam(value = "jugadorNombre",   required = false) List<String>  jugadorNombres,
            @RequestParam(value = "jugadorApellido", required = false) List<String>  jugadorApellidos,
            @RequestParam(value = "jugadorNumero",   required = false) List<Integer> jugadorNumeros,
            @RequestParam(value = "jugadorPosicion", required = false) List<String>  jugadorPosiciones,
            RedirectAttributes redirectAttributes) {

        Club club = new Club(nombre);

        if (entrenadorId != null)
            entrenadorRepository.findById(entrenadorId).ifPresent(club::setEntrenador);

        if (asociacionId != null)
            asociacionRepository.findById(asociacionId).ifPresent(club::setAsociacion);

        if (competicionIds != null && !competicionIds.isEmpty())
            club.setCompeticiones(competicionRepository.findAllById(competicionIds));

        if (jugadorNombres != null) {
            for (int i = 0; i < jugadorNombres.size(); i++) {
                String jNombre = jugadorNombres.get(i);
                if (jNombre == null || jNombre.isBlank()) continue;
                Jugador j = new Jugador();
                j.setNombre(jNombre);
                j.setApellido(safe(jugadorApellidos, i));
                j.setNumero(safeInt(jugadorNumeros, i));
                j.setPosicion(safe(jugadorPosiciones, i));
                club.addJugador(j);
            }
        }

        clubRepository.save(club);
        redirectAttributes.addFlashAttribute("exito", "Club \"" + nombre + "\" registrado correctamente.");
        return "redirect:/clubes/listar";
    }

    // ── POST /clubes/entrenador/nuevo ─────────────────────────────
    @PostMapping("/clubes/entrenador/nuevo")
    public String crearEntrenador(@ModelAttribute Entrenador nuevoEntrenador,
                                  RedirectAttributes redirectAttributes) {
        entrenadorRepository.save(nuevoEntrenador);
        redirectAttributes.addFlashAttribute("exito", "Entrenador creado correctamente.");
        return "redirect:/clubes/nuevo";
    }

    // ── POST /clubes/asociacion/nuevo ─────────────────────────────
    @PostMapping("/clubes/asociacion/nuevo")
    public String crearAsociacion(@ModelAttribute Asociacion nuevaAsociacion,
                                  RedirectAttributes redirectAttributes) {
        asociacionRepository.save(nuevaAsociacion);
        redirectAttributes.addFlashAttribute("exito", "Asociación creada correctamente.");
        return "redirect:/clubes/nuevo";
    }

    // ── POST /clubes/competicion/nuevo ────────────────────────────
    @PostMapping("/clubes/competicion/nuevo")
    public String crearCompeticion(@ModelAttribute Competicion nuevaCompeticion,
                                   RedirectAttributes redirectAttributes) {
        competicionRepository.save(nuevaCompeticion);
        redirectAttributes.addFlashAttribute("exito", "Competición creada correctamente.");
        return "redirect:/clubes/nuevo";
    }

    // ── GET /clubes/listar ────────────────────────────────────────
    @GetMapping("/clubes/listar")
    public String listarClubes(Model model) {
        List<Club> clubes = clubRepository.findAllWithJugadores();
        clubRepository.findCompeticionesForClubes(clubes);

        long totalJugadores    = clubes.stream().mapToLong(c -> c.getJugadores().size()).sum();
        long totalCompeticiones = clubes.stream().flatMap(c -> c.getCompeticiones().stream()).distinct().count();
        long totalAsociaciones  = clubes.stream().map(Club::getAsociacion).filter(a -> a != null).distinct().count();

        model.addAttribute("clubes",             clubes);
        model.addAttribute("totalJugadores",     totalJugadores);
        model.addAttribute("totalCompeticiones", totalCompeticiones);
        model.addAttribute("totalAsociaciones",  totalAsociaciones);
        return "listar";
    }

    // ── POST /clubes/eliminar/{id} ────────────────────────────────
    @PostMapping("/clubes/eliminar/{id}")
    public String eliminarClub(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        clubRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("exito", "Club eliminado correctamente.");
        return "redirect:/clubes/listar";
    }

    // ── GET /clubes/editar/{id} ───────────────────────────────────
    @GetMapping("/clubes/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        List<Club> lista = clubRepository.findAllWithJugadores();
        Club club = lista.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow();
        model.addAttribute("club", club);
        return "editar";
    }

    // ── POST /clubes/editar/{id} ──────────────────────────────────
    @PostMapping("/clubes/editar/{id}")
    public String guardarJugadores(
            @PathVariable Long id,
            @RequestParam(value = "jugadorNombre",   required = false) List<String>  jugadorNombres,
            @RequestParam(value = "jugadorApellido", required = false) List<String>  jugadorApellidos,
            @RequestParam(value = "jugadorNumero",   required = false) List<Integer> jugadorNumeros,
            @RequestParam(value = "jugadorPosicion", required = false) List<String>  jugadorPosiciones,
            RedirectAttributes redirectAttributes) {

        Club club = clubRepository.findById(id).orElseThrow();

        if (jugadorNombres != null) {
            for (int i = 0; i < jugadorNombres.size(); i++) {
                String nombre = jugadorNombres.get(i);
                if (nombre == null || nombre.isBlank()) continue;
                Jugador j = new Jugador();
                j.setNombre(nombre);
                j.setApellido(safe(jugadorApellidos, i));
                j.setNumero(safeInt(jugadorNumeros, i));
                j.setPosicion(safe(jugadorPosiciones, i));
                club.addJugador(j);
            }
        }

        clubRepository.save(club);
        redirectAttributes.addFlashAttribute("exito", "Jugadores agregados correctamente.");
        return "redirect:/clubes/listar";
    }

    // ── Utilidades ────────────────────────────────────────────────
    private String safe(List<String> list, int i) {
        return (list != null && i < list.size()) ? list.get(i) : "";
    }

    private int safeInt(List<Integer> list, int i) {
        return (list != null && i < list.size() && list.get(i) != null) ? list.get(i) : 0;
    }
}