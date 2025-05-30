package linhlang.product.service;

import linhlang.commons.model.PageData;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveCollectionReq;
import linhlang.product.model.Collection;

public interface CollectionService {

    Collection create(SaveCollectionReq request);

    Collection update(String id, SaveCollectionReq request);

    Collection detail(String id);

    PageData<Collection> search(CommonSearchReq req);

    Collection delete(String id);
}
