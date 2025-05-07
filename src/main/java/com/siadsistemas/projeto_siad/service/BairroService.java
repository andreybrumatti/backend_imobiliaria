package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.repository.BairroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeService cidadeService;

    public Bairro buscarOuCriar(String nomeBairro, Integer cidade_codigo) {

        Optional<Bairro> existente = bairroRepository.findByNomeAndAtivoTrueIgnoreCase(nomeBairro);

        if(existente.isPresent()) {
            Bairro existenteEditado = existente.get();

            existenteEditado.setNome(nomeBairro);
            existenteEditado.setCidade(cidadeService.findByCodigo(cidade_codigo));
            return bairroRepository.save(existenteEditado);
        }

        Bairro bairro = new Bairro();
        bairro.setNome(nomeBairro);
        bairro.setCidade(cidadeService.findByCodigo(cidade_codigo));
        Integer novoCodigo = bairroRepository.findMaxCodigo() + 1;
        bairro.setCodigo(novoCodigo);

        return bairroRepository.save(bairro);
    }
}