package com.siadsistemas.projeto_siad.model;

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
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer codigo;

    private String nome;
    private String nome_anterior;

    @ManyToOne
    @JoinColumn(name = "tipo_logradouro_id")
    private TipoLogradouro tipo_logradouro;

    private boolean ativo;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
