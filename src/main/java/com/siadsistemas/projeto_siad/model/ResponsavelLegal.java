package com.siadsistemas.projeto_siad.model;

import com.siadsistemas.projeto_siad.enums.TipoPessoaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer codigo;

    @Enumerated(EnumType.STRING)
    private TipoPessoaEnum tipoPessoa;

    private String nome;
    private String telefoneFixo;
    private String telefoneCelular;
    private String email;
    private String numeroDocumento;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    private boolean ativo;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
        this.ativo = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}