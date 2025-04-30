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
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer codigo;

    @ManyToOne
    @JoinColumn(name = "logradouro_id")
    private Logradouro logradouro;

    @ManyToOne
    @JoinColumn(name = "bairro_id")
    private Bairro bairro;

    private String numero;
    private String complemento;
    private String cep;
    private boolean ativo;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}