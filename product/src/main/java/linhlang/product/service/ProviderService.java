package linhlang.product.service;

import linhlang.commons.model.PageData;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveProviderReq;
import linhlang.product.model.Provider;

public interface ProviderService {

    PageData<Provider> query(CommonSearchReq request);

    Provider create(SaveProviderReq provider);

    Provider update(String id, SaveProviderReq provider);

    void delete(String id);

}
