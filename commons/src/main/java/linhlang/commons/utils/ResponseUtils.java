package linhlang.commons.utils;

import linhlang.commons.model.ApiResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@UtilityClass
public class ResponseUtils {

    public static <T> ResponseEntity<ApiResponse<T>> wrap(HttpStatus status, String code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setResponseCode(code);
        response.setResponseMessage(message);
        response.setPayload(data);
        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return wrap(HttpStatus.CREATED, "CREATED", "created", data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return wrap(HttpStatus.OK, "OK", "success", data);
    }

}
