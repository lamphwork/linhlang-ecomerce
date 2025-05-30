package linhlang.auth.constants;

import linhlang.commons.exceptions.ExceptionInfo;

public enum Errors implements ExceptionInfo {

    ACCOUNT_EXISTED("ACCOUNT_EXISTED", "account existed"),
    ACCOUNT_NOTFOUND("ACCOUNT_NOTFOUND", "Account not found"),
    CREDENTIAL_INVALID("CREDENTIAL_INVALID", "Username or password invalid"),
    TOKEN_INVALID("TOKEN_INVALID", "Token invalid"),

    ;
    public final String code;
    public final String message;

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
