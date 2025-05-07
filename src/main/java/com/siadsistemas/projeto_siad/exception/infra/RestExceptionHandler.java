package com.siadsistemas.projeto_siad.exception.infra;

import com.siadsistemas.projeto_siad.exception.domain.boletimImobiliario.BoletimImobiliarioException;
import com.siadsistemas.projeto_siad.exception.domain.boletimImobiliario.BoletimImobiliarioNotFoundException;
import com.siadsistemas.projeto_siad.exception.domain.endereco.EnderecoException;
import com.siadsistemas.projeto_siad.exception.domain.endereco.EnderecoNotFoundException;
import com.siadsistemas.projeto_siad.exception.domain.logradouro.LogradouroException;
import com.siadsistemas.projeto_siad.exception.domain.logradouro.LogradouroNotFoundException;
import com.siadsistemas.projeto_siad.exception.domain.responsavelLegal.ResponsavelLegalException;
import com.siadsistemas.projeto_siad.exception.domain.responsavelLegal.ResponsavelLegalNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EnderecoException.class)
    private ResponseEntity<RestErrorMessage> enderecoException(EnderecoException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(EnderecoNotFoundException.class)
    private ResponseEntity<RestErrorMessage> enderecoNotFoundException(EnderecoNotFoundException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(LogradouroException.class)
    private ResponseEntity<RestErrorMessage> logradouroException(LogradouroException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(LogradouroNotFoundException.class)
    private ResponseEntity<RestErrorMessage> logradouroNotFoundException(LogradouroNotFoundException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(ResponsavelLegalException.class)
    private ResponseEntity<RestErrorMessage> responsavelLegalException(ResponsavelLegalException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ResponsavelLegalNotFoundException.class)
    private ResponseEntity<RestErrorMessage> responsavelLegalNotFoundException(ResponsavelLegalNotFoundException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(BoletimImobiliarioException.class)
    private ResponseEntity<RestErrorMessage> boletimImobiliarioException(BoletimImobiliarioException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(BoletimImobiliarioNotFoundException.class)
    private ResponseEntity<RestErrorMessage> boletimImobiliarioNotFoundException(BoletimImobiliarioNotFoundException exc) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}