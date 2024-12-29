package linhlang.commons.model;

import lombok.Data;

@Data
public class ApiResponse <T> {

    private String responseCode;
    private String responseMessage;
    private T payload;
}
