package linhlang.webconfig.service;

import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.webconfig.controller.request.SaveBannerRequest;
import linhlang.webconfig.model.Banner;

public interface BannerService {

    String createBanner(SaveBannerRequest req);

    String updateBanner(String id, SaveBannerRequest req);

    String deleteBanner(String id);

    PageData<Banner> query(QueryRequest request);
}
