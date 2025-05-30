package linhlang.webconfig.service;

import linhlang.commons.model.PageData;
import linhlang.webconfig.controller.request.PageContentRequest;
import linhlang.webconfig.controller.request.PageContentSearch;
import linhlang.webconfig.model.PageContent;

public interface PageContentService {
    PageContent create(PageContentRequest request);

    PageData<PageContent> searchPageContent(PageContentSearch request);

    PageContent update(PageContentRequest request);
}
