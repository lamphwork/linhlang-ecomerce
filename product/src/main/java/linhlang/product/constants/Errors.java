package linhlang.product.constants;

import linhlang.commons.exceptions.ExceptionInfo;

public enum Errors implements ExceptionInfo {

    PRODUCT_NOTFOUND("PRODUCT_NOTFOUND", "Product not found"),
    PRODUCT_EXISTED("PRODUCT_EXISTED", "Product existed"),
    PROVIDER_NOTFOUND("PROVIDER_NOTFOUND", "Provider not found"),
    CATEGORY_NOTFOUND("CATEGORY_NOTFOUND", "Category not found"),
    CATEGORY_EXISTED("CATEGORY_EXISTED", "Category not existed"),
    COLLECTION_NOTFOUND("COLLECTION_NOTFOUND", "Collection not found"),

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
