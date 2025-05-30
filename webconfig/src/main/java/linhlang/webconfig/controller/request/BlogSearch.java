package linhlang.webconfig.controller.request;

import linhlang.webconfig.model.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlogSearch extends QueryRequest {
    private String title;
    private String blogCategory;
    private String tag;
    private String contain;
    private String createUser;
}
