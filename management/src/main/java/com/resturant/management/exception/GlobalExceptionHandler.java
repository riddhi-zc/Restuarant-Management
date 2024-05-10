package com.resturant.management.exception;

import com.resturant.management.constants.ErrorConstants;
import com.resturant.management.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNoDataFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorDto(404,ErrorConstants.DATA_NOT_FOUND), HttpStatus.NOT_FOUND);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WebPageNotFoundException.class)
    public ResponseEntity<ErrorDto> handlingWebPageNotFoundException(WebPageNotFoundException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorDTO.setErrorMessage(ErrorConstants.NOT_FOUND);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex) {
            ErrorDto errorDTO = new ErrorDto();
            errorDTO.setErrorCode(HttpStatus.NOT_FOUND.value());
            errorDTO.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenHandleException.class)
    public ResponseEntity<ErrorDto> handlingAccessForbiddenException(ForbiddenHandleException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setErrorCode(HttpStatus.FORBIDDEN.value());
        errorDTO.setErrorMessage(ErrorConstants.ACCESS_DENIED);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleInValidArgument(MethodArgumentNotValidException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorDTO.setErrorMessage(ErrorConstants.INVALID_ARGUMENT);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
