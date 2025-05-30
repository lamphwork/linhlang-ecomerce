package linhlang.webconfig.service.impl;

import linhlang.commons.model.PageData;
import linhlang.webconfig.constants.Constants;
import linhlang.webconfig.controller.request.BlogRequest;
import linhlang.webconfig.controller.request.BlogSearch;
import linhlang.webconfig.controller.request.PageContentSearch;
import linhlang.webconfig.model.Blog;
import linhlang.webconfig.model.PageContent;
import linhlang.webconfig.repository.BlogRepository;
import linhlang.webconfig.service.BlogService;
import linhlang.webconfig.service.mapper.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogMapper blogMapper;
    private final BlogRepository blogRepository;

    @Override
    public Blog create(BlogRequest request) {
        Blog blog = blogMapper.toEntity(request);
        blog.setId(UUID.randomUUID().toString());
        blog.setStatus(BigDecimal.valueOf(Constants.STATUS_ACTIIVE));
        blogRepository.save(blog);
        return blog;
    }

    @Override
    public Blog update(BlogRequest request) {
        Blog blog = blogRepository.getById(request.getId());
        if (blog == null) {
            return null;
        }
        Blog blogs = blogMapper.toEntity(request);
        blogRepository.save(blogs);
        return blog;
    }

    @Override
    public Blog getById(String id) {
        Blog blog = blogRepository.getById(id);
        if (blog == null) {
            return null;
        }
        return blog;
    }

    @Override
    public PageData<Blog> search(BlogSearch request) {
        return blogRepository.search(request);
    }
}
