package linhlang.webconfig.controller.request;

import linhlang.webconfig.model.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageContentSearch extends QueryRequest {
    private String title;
    private BigDecimal isDisplay;
}
