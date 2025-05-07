package com.siadsistemas.projeto_siad.exception.domain.boletimImobiliario;

public class BoletimImobiliarioException extends RuntimeException{

    public BoletimImobiliarioException(String message) {
        super(message);
    }

    public static BoletimImobiliarioException boletimNaoEncontrado(){
        return new BoletimImobiliarioException("Boletim Imobiliário não encontrado");
    }

}
