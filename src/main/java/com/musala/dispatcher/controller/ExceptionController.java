package com.musala.dispatcher.controller;

import com.musala.dispatcher.data.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            JpaSystemException.class,
            TransactionSystemException.class,
            DataIntegrityViolationException.class,
            InvalidDataAccessApiUsageException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    private ExceptionResponse badRequest(EntityNotFoundException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    private ExceptionResponse notFound(EntityNotFoundException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    private ExceptionResponse error(RuntimeException ex) {
        ex.printStackTrace();
        return new ExceptionResponse(ex.getMessage());
    }
}
