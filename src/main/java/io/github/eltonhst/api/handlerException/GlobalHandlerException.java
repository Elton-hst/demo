package io.github.eltonhst.api.handlerException;

import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.domain.exception.InternalServerException;
import io.github.eltonhst.domain.exception.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ApiError> validationException(Exception ex) {
        log.error(">> Error (1) >> {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status("Desculpe, não foi possivel concluir o processamento. (1)")
                .errors(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequestException(Exception ex) {
        log.error(">> Error (2) >> {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status("Desculpe, não foi possivel concluir o processamento. (2)")
                .errors(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiError> internalServerException(Exception ex) {
        log.error(">> Error (3) >> {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status("Desculpe, não foi possivel concluir o processamento. (3)")
                .errors(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.internalServerError().body(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> runtimeException(Exception ex) {
        log.error(">> Error (4) >> {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status("Desculpe, não foi possivel concluir o processamento. (4)")
                .errors(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(apiError);
    }

}
