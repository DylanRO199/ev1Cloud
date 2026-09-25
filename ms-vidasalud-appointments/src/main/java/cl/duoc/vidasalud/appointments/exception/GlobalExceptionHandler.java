package cl.duoc.vidasalud.appointments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppointmentNotFoundException.class)
    ResponseEntity<?> notFound(AppointmentNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({InvalidStatusTransitionException.class, IllegalArgumentException.class})
    ResponseEntity<?> badRequest(RuntimeException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Datos inválidos.");
        return response(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> downstream(ResponseStatusException ex) {
        return response(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason());
    }

    private ResponseEntity<?> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message == null ? status.getReasonPhrase() : message
        ));
    }
}
