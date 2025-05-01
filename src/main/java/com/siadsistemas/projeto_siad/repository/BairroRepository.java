package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BairroRepository extends JpaRepository<Bairro, UUID> {
    Optional<Bairro> findByNomeIgnoreCase(String nome);

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM Bairro e")
    Integer findMaxCodigo();

    List<Bairro> findAllByAtivoTrue();

    Optional<Bairro> findByIdAndAtivoTrue(UUID id);

    Optional<Bairro> findByNomeAndAtivoTrue(String nome);
}
