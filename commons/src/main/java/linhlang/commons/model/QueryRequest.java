package linhlang.commons.model;

import lombok.Data;

@Data
public class QueryRequest {

    protected Integer page;
    protected Integer limit;

    public Integer getPage() {
        return (page == null || page < 0) ? 0 : page;
    }

    public Integer getLimit() {
        return (limit == null || limit < 1) ? 100 : limit;
    }
}
