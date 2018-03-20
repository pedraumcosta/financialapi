package com.pedraumcosta.controller;

import com.pedraumcosta.exceptions.BusinessException;
import com.pedraumcosta.model.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@RestController
public class GeneralExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomErrorResponse handleConflict(IllegalArgumentException e, HttpServletResponse response)
            throws IOException {
        return new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomErrorResponse handleConflict(BusinessException e, HttpServletResponse response)
            throws IOException {
        return new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}