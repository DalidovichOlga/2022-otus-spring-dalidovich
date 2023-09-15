package ru.otus.spring.booklib.rest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.Locale;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;


    public RestExceptionHandler( MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
    @ExceptionHandler(LibraryError.class)
    protected ResponseEntity<Object> handleConflict(LibraryError ex, WebRequest request){

        return handleExceptionInternal(ex,messageSource.getMessage(ex.getCode(),
                new String[]{ex.getDetails()}, getLocale()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }

}
