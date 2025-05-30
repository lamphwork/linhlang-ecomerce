package linhlang.webconfig.service;

import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.webconfig.controller.request.SaveStickyRequest;
import linhlang.webconfig.model.Sticky;

public interface StickyService {

    String create(SaveStickyRequest req);

    String update(String id, SaveStickyRequest req);

    String delete(String id);

    PageData<Sticky> query(QueryRequest request);
}
