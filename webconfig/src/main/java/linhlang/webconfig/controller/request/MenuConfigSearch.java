package linhlang.webconfig.controller.request;

import linhlang.webconfig.model.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuConfigSearch extends QueryRequest {
    private String name;
}
