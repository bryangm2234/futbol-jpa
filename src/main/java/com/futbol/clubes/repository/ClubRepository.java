package com.futbol.clubes.repository;

import com.futbol.clubes.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    // Primera query: carga clubes con entrenador y asociacion
    @Query("SELECT DISTINCT c FROM Club c " +
           "LEFT JOIN FETCH c.entrenador " +
           "LEFT JOIN FETCH c.asociacion " +
           "LEFT JOIN FETCH c.jugadores")
    List<Club> findAllWithJugadores();

    // Segunda query: carga las competiciones por separado
    @Query("SELECT DISTINCT c FROM Club c " +
           "LEFT JOIN FETCH c.competiciones " +
           "WHERE c IN :clubes")
    List<Club> findCompeticionesForClubes(
        @org.springframework.data.repository.query.Param("clubes") List<Club> clubes
    );
}