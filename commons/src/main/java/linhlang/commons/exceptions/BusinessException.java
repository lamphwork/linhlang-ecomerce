package linhlang.commons.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class BusinessException extends RuntimeException {

    protected String code;

    public BusinessException(String code, String message) {
        super(message);
        setCode(code);
    }

    public BusinessException(ExceptionInfo exceptionInfo) {
        this(exceptionInfo.getCode(), exceptionInfo.getMessage());
    }
}
