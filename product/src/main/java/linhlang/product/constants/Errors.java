package linhlang.product.constants;

import linhlang.commons.exceptions.ExceptionInfo;

public enum Errors implements ExceptionInfo {

    PRODUCT_NOTFOUND("PROD_404", "Product not found"),

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
