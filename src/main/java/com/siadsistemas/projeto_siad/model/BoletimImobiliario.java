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
public class BoletimImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer codigo;

    private String matricula;

    @ManyToOne
    @JoinColumn(name = "responsavel_legal_id")
    private ResponsavelLegal responsavelLegal;

    @ManyToOne
    @JoinColumn(name = "endereco_fisico_id")
    private Endereco enderecoFisico;

    @ManyToOne
    @JoinColumn(name = "endereco_correspondencia_id")
    private Endereco enderecoCorrespondencia;

    private boolean ativo;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}