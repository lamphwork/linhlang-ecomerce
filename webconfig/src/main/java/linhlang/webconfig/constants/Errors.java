package linhlang.webconfig.constants;

import linhlang.commons.exceptions.ExceptionInfo;

public enum Errors implements ExceptionInfo {

    BANNER_NOTFOUND("BANNER_NOT_FOUND", "Banner not found"),
    STICKY_NOTFOUND("STICKY_NOT_FOUND", "Sticky not found")

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
