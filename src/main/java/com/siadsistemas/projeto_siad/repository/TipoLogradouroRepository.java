package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.TipoLogradouro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoLogradouroRepository extends JpaRepository<TipoLogradouro, UUID> {
    Optional<TipoLogradouro> findByDescricaoIgnoreCase(String descricao);

    Optional<TipoLogradouro> findByCodigo(Integer codigo);

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM TipoLogradouro e")
    Integer findMaxCodigo();

    List<TipoLogradouro> findAllByAtivoTrue();
}