package linhlang.webconfig.service;


import linhlang.commons.model.PageData;
import linhlang.webconfig.controller.request.BlogRequest;
import linhlang.webconfig.controller.request.BlogSearch;
import linhlang.webconfig.model.Blog;
import linhlang.webconfig.model.PageContent;

public interface BlogService {
    Blog create(BlogRequest request);

    Blog update(BlogRequest request);

    Blog getById(String id);

    PageData<Blog> search(BlogSearch request);
}
