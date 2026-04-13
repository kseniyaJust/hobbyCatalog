package org.example.hobbycatalog.exceptions;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ControllerErrorHandler {
    //400 Bad Request
    @ExceptionHandler({
            BadRequestException.class,
            NullPointerException.class,
            IOException.class,
            IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequestException(Exception ex)
    {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
    //401 Wrong data
    @ExceptionHandler({
         //   BadCredentialException.class, - with auth
            IllegalStateException.class

    })
    public ResponseEntity<?> handleBadCredentials(Exception ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }
    //403 Access Denied
    @ExceptionHandler({
            AccessDeniedException.class,
           // JwtException.class - with auth
    })
    public ResponseEntity<?> handleAccessDeniedException(Exception ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
    }
    //404 Not Found
    @ExceptionHandler({
           // UsernameNotFoundException.class - with auth,
            ItemNotFoundException.class,
            TypeHobbyNotFoundException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    //409 Conflict Exception
    @ExceptionHandler({
            ConflictException.class,
    })
    public ResponseEntity<?> handleConflictException(Exception ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }
    //500 Internal server error
    @ExceptionHandler({
            RuntimeException.class
    })
    public ResponseEntity<?> handleRuntimeException(Exception ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
