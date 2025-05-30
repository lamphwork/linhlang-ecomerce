package linhlang.webconfig.service.impl;

import linhlang.commons.model.PageData;
import linhlang.webconfig.constants.Constants;
import linhlang.webconfig.controller.request.PageContentRequest;
import linhlang.webconfig.controller.request.PageContentSearch;
import linhlang.webconfig.model.PageContent;
import linhlang.webconfig.repository.PageContentRepositoty;
import linhlang.webconfig.service.PageContentService;
import linhlang.webconfig.service.mapper.PageContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PageContentServiceImpl implements PageContentService {
    private final PageContentRepositoty pageContentRepositoty;
    private final PageContentMapper pageContentMapper;

    @Override
    public PageContent create(PageContentRequest request) {
        PageContent pageContent = pageContentMapper.toEntity(request);
        pageContent.setId(UUID.randomUUID().toString());
        pageContent.setStatus(BigDecimal.valueOf(Constants.STATUS_ACTIIVE));
        pageContentRepositoty.save(pageContent);
        return pageContent;
    }

    @Override
    public PageData<PageContent> searchPageContent(PageContentSearch request) {
        return pageContentRepositoty.searchPageContent(request);
    }

    @Override
    public PageContent update(PageContentRequest request) {
        return null;
    }
}
