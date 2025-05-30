package linhlang.product.controller.request;

import linhlang.commons.model.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonSearchReq extends QueryRequest {

    private String query;
}
