package com.futbol.clubes.repository;

import com.futbol.clubes.model.Asociacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsociacionRepository extends JpaRepository<Asociacion, Long> {}
