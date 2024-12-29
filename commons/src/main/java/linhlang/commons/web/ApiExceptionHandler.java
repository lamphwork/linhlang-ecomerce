package linhlang.commons.web;

import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleError(MethodArgumentNotValidException e) {
        Map<String, String> details = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getCode()).orElse(fieldError.getClass().getName())
                ));

        return ResponseUtils.wrap(HttpStatus.BAD_REQUEST, "FAILED", "validation failure", details);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleError(BusinessException e) {
        return ResponseUtils.wrap(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnknownError(Exception e) {
        log.error("Unknown error", e);
        return ResponseUtils.wrap(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", "Unknown error, please contact your admin", null);
    }
}
