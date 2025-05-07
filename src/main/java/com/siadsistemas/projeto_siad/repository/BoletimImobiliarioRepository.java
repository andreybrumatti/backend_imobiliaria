package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.BoletimImobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoletimImobiliarioRepository extends JpaRepository<BoletimImobiliario, UUID> {

    boolean existsByMatricula(String matricula);

    Optional<BoletimImobiliario> findByIdAndAtivoTrue(UUID id);

    List<BoletimImobiliario> findAllByAtivoTrueOrderByCodigoAsc();

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM BoletimImobiliario e")
    Integer findMaxCodigo();
}