package br.gov.sp.fatec.springbootapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegistroJaExisteException extends RuntimeException{

    public RegistroJaExisteException() {
        super();
    }

    public RegistroJaExisteException(String message){
        super(message);
    }

    public RegistroJaExisteException(Throwable cause){
        super(cause);
    }

    public RegistroJaExisteException(String message, Throwable cause){
        super(message, cause);
    }
    
}
