package linhlang.webconfig.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.webconfig.constants.Constants;
import linhlang.webconfig.controller.request.MenuConfigSearch;
import linhlang.webconfig.controller.request.PageContentRequest;
import linhlang.webconfig.controller.request.PageContentSearch;
import linhlang.webconfig.model.MenuConfig;
import linhlang.webconfig.model.PageContent;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.jooq.generated.Tables.PAGE_CONTENT;

@Repository
@RequiredArgsConstructor
public class PageContentRepositoty implements CommonRepository<PageContent, String> {

    private final DSLContext dsl;

    @Override
    public void save(PageContent data) {
        Map<Field<?>, Object> setData = new HashMap<>();
        setData.put(PAGE_CONTENT.ID, data.getId());
        setData.put(PAGE_CONTENT.TITLE, data.getTitle());
        setData.put(PAGE_CONTENT.CONTENT, data.getContent());
        setData.put(PAGE_CONTENT.IMAGE, data.getImage());
        setData.put(PAGE_CONTENT.SEO_TITLE, data.getSeoTitle());
        setData.put(PAGE_CONTENT.SEO_LINK, data.getSeoLink());
        setData.put(PAGE_CONTENT.SEO_DESCRIPTION, data.getSeoDescription());
        setData.put(PAGE_CONTENT.PAGE_INTERFACE, data.getPageInterface());
        setData.put(PAGE_CONTENT.MENU_LINK, data.getMenuLink());
        setData.put(PAGE_CONTENT.DISPLAY_TIME, data.getDisplayTime());
        setData.put(PAGE_CONTENT.IS_DISPLAY, data.getIsDisplay());
        setData.put(PAGE_CONTENT.STATUS, data.getStatus());
        dsl.insertInto(PAGE_CONTENT)
                .set(setData)
                .set(PAGE_CONTENT.CREATE_TIME, DSL.currentLocalDateTime())
                .onDuplicateKeyUpdate()
                .set(setData)
                .set(PAGE_CONTENT.UPDATE_TIME, DSL.currentLocalDateTime())
                .returningResult()
                .execute();
    }

    @Override
    public PageContent load(String s) {
        return null;
    }

    @Override
    public void delete(String s) {

    }

    public PageData<PageContent> searchPageContent(PageContentSearch request) {
        var selectQuery = dsl.select()
                .from(PAGE_CONTENT)
                .where(PAGE_CONTENT.STATUS.eq(BigDecimal.valueOf(Constants.STATUS_ACTIIVE)));
        if (!StringUtils.isEmpty(request.getTitle())) {
            selectQuery.and(PAGE_CONTENT.TITLE.eq(request.getTitle()));
        }

        if (request.getIsDisplay() != null) {
            selectQuery.and(PAGE_CONTENT.IS_DISPLAY.eq(request.getIsDisplay()));
        }

        return queryPage(selectQuery, request.getPage(), request.getLimit(), dsl)
                .map(this::readBasicInfo);
    }

    private PageContent readBasicInfo(Record result) {
        if (result == null) {
            return null;
        }
        PageContent rs = new PageContent();
        rs.setId(result.get(PAGE_CONTENT.ID));
        rs.setTitle(result.get(PAGE_CONTENT.TITLE));
        rs.setContent(result.get(PAGE_CONTENT.CONTENT));
        rs.setImage(result.get(PAGE_CONTENT.IMAGE));
        rs.setSeoTitle(result.get(PAGE_CONTENT.SEO_TITLE));
        rs.setSeoDescription(result.get(PAGE_CONTENT.SEO_DESCRIPTION));
        rs.setPageInterface(result.get(PAGE_CONTENT.PAGE_INTERFACE));
        rs.setMenuLink(result.get(PAGE_CONTENT.MENU_LINK));
        rs.setSeoLink(result.get(PAGE_CONTENT.SEO_LINK));
        rs.setDisplayTime(result.get(PAGE_CONTENT.DISPLAY_TIME));
        rs.setIsDisplay(result.get(PAGE_CONTENT.IS_DISPLAY));
        rs.setCreateTime(result.get(PAGE_CONTENT.CREATE_TIME));
        rs.setUpdateTime(result.get(PAGE_CONTENT.UPDATE_TIME));
        rs.setStatus(result.get(PAGE_CONTENT.STATUS));
        return rs;
    }
}
