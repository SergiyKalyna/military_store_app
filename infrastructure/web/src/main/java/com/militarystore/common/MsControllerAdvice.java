package com.militarystore.common;

import com.militarystore.exception.MilitaryStoreRuntimeException;
import com.militarystore.model.dto.error.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MsControllerAdvice {

    @ExceptionHandler(MilitaryStoreRuntimeException.class)
    public ResponseEntity<ErrorDto> handleMilitaryStoreException(MilitaryStoreRuntimeException e) {
        log.error(e.getMessage(), e);

        var errorDto = ErrorDto.builder()
            .code(e.statusCode.value())
            .error(e.getMessage())
            .reason(ExceptionUtils.getRootCauseMessage(e))
            .build();

        return ResponseEntity.status(e.statusCode).body(errorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleException(RuntimeException e) {
        log.error(e.getMessage(), e);
        String errorMessage = "Internal Server Error";

        var errorDto = ErrorDto.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(errorMessage)
            .reason(ExceptionUtils.getRootCauseMessage(e))
            .build();

        return ResponseEntity.internalServerError().body(errorDto);
    }
}
