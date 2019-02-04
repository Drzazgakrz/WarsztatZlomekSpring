package pl.warsztat.zlomek.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.warsztat.zlomek.exceptions.CouldNotAuthorizeException;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.UserExistException;
import pl.warsztat.zlomek.exceptions.UserNotFoundException;
import pl.warsztat.zlomek.model.Error;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error userNotFoundHandler(UserNotFoundException e){
        return new Error(e.getReason());
    }

    @ExceptionHandler(CouldNotAuthorizeException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error couldNotAuthorizeHandler(CouldNotAuthorizeException e){
        return new Error(e.getReason());
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error userExistHandler(UserExistException e){
        return new Error(e.getReason());
    }

    @ExceptionHandler(FieldsNotCorrect.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error fieldsNotCorrectHandler(FieldsNotCorrect e){
        return new Error(e.getFields());
    }
}
