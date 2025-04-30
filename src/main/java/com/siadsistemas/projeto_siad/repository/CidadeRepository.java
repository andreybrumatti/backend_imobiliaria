package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, UUID> {

    Optional<Cidade> findByNomeIgnoreCase(String nome);

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM Cidade e")
    Integer findMaxCodigo();
}
