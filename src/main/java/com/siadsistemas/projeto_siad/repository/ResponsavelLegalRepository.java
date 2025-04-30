package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.enums.TipoPessoaEnum;
import com.siadsistemas.projeto_siad.model.ResponsavelLegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResponsavelLegalRepository extends JpaRepository<ResponsavelLegal, UUID> {

    List<ResponsavelLegal> findAllByAtivoTrue();

    Optional<ResponsavelLegal> findByIdAndAtivoTrue(UUID id);

    boolean existsByEmail(String email);

    boolean existsByNumeroDocumentoAndTipoPessoa(String numeroDocumento, TipoPessoaEnum tipoPessoa);

    @Query("SELECT COALESCE(MAX(r.codigo), 0) FROM ResponsavelLegal r")
    Integer findMaxCodigo();
}