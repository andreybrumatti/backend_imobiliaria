package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.Logradouro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LogradouroRepository extends JpaRepository<Logradouro, UUID> {

    List<Logradouro> findAllByAtivoTrue();

    Optional<Logradouro> findByIdAndAtivoTrue(UUID id);

    Optional<Logradouro> findByNomeAndAtivoTrueIgnoreCase(String nome);

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM Logradouro e")
    Integer findMaxCodigo();
}