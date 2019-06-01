package pl.warsztat.zlomek.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.warsztat.zlomek.exceptions.CouldNotAuthorizeException;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.Error;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(ResourcesNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error userNotFoundHandler(ResourcesNotFoundException e){
        return new Error(e.getReason());
    }

    @ExceptionHandler(CouldNotAuthorizeException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error couldNotAuthorizeHandler(CouldNotAuthorizeException e){
        return new Error(e.getReason());
    }

    @ExceptionHandler(ResourcesExistException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ResponseBody
    public Error userExistHandler(ResourcesExistException e){
        return new Error(e.getReason());
    }

    @ExceptionHandler({FieldsNotCorrect.class, TransactionSystemException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error fieldsNotCorrectHandler(Exception e){
        return new Error("Błędne dane");
    }
}
