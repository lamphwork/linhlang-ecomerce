package linhlang.product.service;

import linhlang.commons.model.PageData;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveCategoryReq;
import linhlang.product.model.Category;

public interface CategoryService {

    PageData<Category> query(CommonSearchReq request);

    Category create(SaveCategoryReq req);

    Category update(String id, SaveCategoryReq req);

    void delete(String id);

}
