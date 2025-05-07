package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM Endereco e")
    Integer findMaxCodigo();

    Optional<Endereco> findByIdAndAtivoTrue(UUID id);

    List<Endereco> findAllByAtivoTrueOrderByCodigoAsc();

    @Query("""
    SELECT e FROM Endereco e
    WHERE e.numero = :numero
    AND e.complemento = :complemento
    AND e.cep = :cep
    AND e.logradouro.nome = :nomeLogradouro
    AND e.logradouro.tipo_logradouro.codigo = :codigoTipoLogradouro
    AND e.bairro.nome = :nomeBairro
    AND e.bairro.cidade.codigo = :codigoCidade
    AND e.ativo = true
    """)
    Optional<Endereco> findByCampos(
            @Param("numero") String numero,
            @Param("complemento") String complemento,
            @Param("cep") String cep,
            @Param("nomeLogradouro") String nomeLogradouro,
            @Param("codigoTipoLogradouro") Integer codigoTipoLogradouro,
            @Param("nomeBairro") String nomeBairro,
            @Param("codigoCidade") Integer codigoCidade
    );
}