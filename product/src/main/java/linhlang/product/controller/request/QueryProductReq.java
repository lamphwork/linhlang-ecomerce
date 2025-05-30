package linhlang.product.controller.request;

import linhlang.commons.model.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryProductReq extends QueryRequest {

    private String search;
    private List<String> provider;
    private List<String> category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
