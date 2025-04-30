package com.siadsistemas.projeto_siad.config;

import com.siadsistemas.projeto_siad.model.TipoLogradouro;
import com.siadsistemas.projeto_siad.repository.TipoLogradouroRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class TipoLogradouroDataInitializer {

    private final TipoLogradouroRepository tipoLogradouroRepository;

    @PostConstruct
    public void init() {
        List<String> tipos = List.of(
                "Rua", "Avenida", "Travessa", "Alameda", "Estrada",
                "Rodovia", "Viela", "Largo", "Praça", "Via",
                "Beco", "Passarela", "Passagem", "Parque", "Complexo",
                "Condomínio", "Setor", "Quadra", "Área", "Módulo", "Estação"
        );

        for (String tipo : tipos) {
            String tipoFormatado = tipo.trim();

            boolean existe = tipoLogradouroRepository
                    .findByDescricaoIgnoreCase(tipoFormatado)
                    .isPresent();

            if (!existe) {
                Integer novoCodigo = tipoLogradouroRepository.findMaxCodigo() + 1;

                TipoLogradouro tipoLogradouro = TipoLogradouro.builder()
                        .codigo(novoCodigo)
                        .descricao(tipoFormatado)
                        .ativo(true)
                        .created_at(LocalDateTime.now())
                        .updated_at(LocalDateTime.now())
                        .build();

                tipoLogradouroRepository.save(tipoLogradouro);
            }
        }
    }
}