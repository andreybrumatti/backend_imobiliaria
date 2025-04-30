package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM Endereco e")
    Integer findMaxCodigo();

    Optional<Endereco> findByIdAndAtivoTrue(UUID id);

    List<Endereco> findAllByAtivoTrue();

}