package com.isep.acme.adapters.controllers.http;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    @Data
    public static class ErrorPayloadMessage {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }

    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            return servletRequest.getRequestURI();
        }
        return "";
    }

    private ErrorPayloadMessage createPayload(String message, WebRequest request, HttpStatus status) {
        ErrorPayloadMessage errorResponse = new ErrorPayloadMessage();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(message);
        errorResponse.setPath(this.getPath(request));
        return errorResponse;
    }

    private ErrorPayloadMessage createPayload(Exception exception, WebRequest request, HttpStatus status) {
        return createPayload(exception.getMessage(), request, status);
    }

    private ErrorPayloadMessage createPayload(Exception exception, WebRequest request, String msg) {
        ErrorPayloadMessage errorResponse = this.createPayload(exception, request, HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(msg);
        return errorResponse;
    }

    private static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append(", ");
        }

        if (!map.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("}");
        return sb.toString();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidsArguments(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(this.createPayload(exception, request, mapToString(errorMap)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentExceptions(IllegalArgumentException exception, WebRequest request) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.createPayload(exception, request, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleIllegalArgumentExceptions(BadCredentialsException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Object> handleIllegalArgumentExceptions(DatabaseException exception, WebRequest request) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(this.createPayload(exception, request, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericExceptions(Exception exception, WebRequest request) {
        exception.printStackTrace();
        String genericMsg = "Some generic error occurred. See app logs please or contact support.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.createPayload(genericMsg, request, HttpStatus.BAD_REQUEST));
    }

}
