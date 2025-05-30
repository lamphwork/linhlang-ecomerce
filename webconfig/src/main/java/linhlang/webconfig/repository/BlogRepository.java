package linhlang.webconfig.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.webconfig.constants.Constants;
import linhlang.webconfig.controller.request.BlogSearch;
import linhlang.webconfig.model.Blog;
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

import static org.jooq.generated.Tables.*;

@Repository
@RequiredArgsConstructor
public class BlogRepository implements CommonRepository<Blog, String> {
    private final DSLContext dsl;

    @Override
    public void save(Blog data) {
        Map<Field<?>, Object> setData = new HashMap<>();
        setData.put(BLOG.ID, data.getId());
        setData.put(BLOG.TITLE, data.getTitle());
        setData.put(BLOG.CONTENT, data.getContent());
        setData.put(BLOG.CREATE_USER, data.getCreateUser());
        setData.put(BLOG.BLOG_CATEGORY, data.getBlogCategory());
        setData.put(BLOG.BLOG_QUOTE, data.getBlogQuote());
        setData.put(BLOG.SEO_TITLE, data.getSeoTitle());
        setData.put(BLOG.SEO_DESCRIPTION, data.getSeoDescription());
        setData.put(BLOG.SEO_URL, data.getSeoUrl());
        setData.put(BLOG.CANONICAL_URL, data.getCanonicalUrl());
        setData.put(BLOG.META_INDEX, data.getMetaIndex());
        setData.put(BLOG.META_FOLLOW, data.getMetaFollow());
        setData.put(BLOG.DISPLAY_TIME, data.getDisplayTime());
        setData.put(BLOG.IS_DISPLAY, data.getIsDisplay());
        setData.put(BLOG.IMAGE, data.getImage());
        setData.put(BLOG.IMAGE_DESCRIPTION, data.getImageDescription());
        setData.put(BLOG.TAG, data.getTag());
        setData.put(BLOG.STATUS, data.getStatus());
        setData.put(BLOG.PAGE_INTERFACE, data.getPageInterface());
        dsl.insertInto(BLOG)
                .set(setData)
                .set(BLOG.CREATE_TIME, DSL.currentLocalDateTime())
                .onDuplicateKeyUpdate()
                .set(setData)
                .set(BLOG.UPDATE_TIME, DSL.currentLocalDateTime())
                .returningResult()
                .execute();
    }

    @Override
    public Blog load(String s) {
        return null;
    }

    @Override
    public void delete(String s) {

    }

    public Blog getById(String id) {
        Record result = dsl.select()
                .from(BLOG)
                .where(BLOG.ID.eq(id))
                .fetchOne();

        return readBasicInfo(result);
    }

    private Blog readBasicInfo(Record result) {
        if (result == null) {
            return null;
        }
        Blog rs = new Blog();
        rs.setId(result.get(BLOG.ID));
        rs.setTitle(result.get(BLOG.TITLE));
        rs.setContent(result.get(BLOG.CONTENT));
        rs.setCreateUser(result.get(BLOG.CREATE_USER));
        rs.setBlogCategory(result.get(BLOG.BLOG_CATEGORY));
        rs.setBlogQuote(result.get(BLOG.BLOG_QUOTE));
        rs.setSeoTitle(result.get(BLOG.SEO_TITLE));
        rs.setSeoUrl(result.get(BLOG.SEO_URL));
        rs.setSeoDescription(result.get(BLOG.SEO_DESCRIPTION));
        rs.setCanonicalUrl(result.get(BLOG.CANONICAL_URL));
        rs.setMetaIndex(result.get(BLOG.META_INDEX));
        rs.setMetaFollow(result.get(BLOG.META_FOLLOW));
        rs.setDisplayTime(result.get(BLOG.DISPLAY_TIME));
        rs.setIsDisplay(result.get(BLOG.IS_DISPLAY));
        rs.setImage(result.get(BLOG.IMAGE));
        rs.setImageDescription(result.get(BLOG.IMAGE_DESCRIPTION));
        rs.setTag(result.get(BLOG.TAG));
        rs.setStatus(result.get(BLOG.STATUS));
        rs.setPageInterface(result.get(BLOG.PAGE_INTERFACE));
        rs.setCreateTime(result.get(BLOG.CREATE_TIME));
        rs.setUpdateTime(result.get(BLOG.UPDATE_TIME));
        return rs;
    }

    public PageData<Blog> search(BlogSearch request) {
        var selectQuery = dsl.select()
                .from(BLOG)
                .where(BLOG.STATUS.eq(BigDecimal.valueOf(Constants.STATUS_ACTIIVE)));
                        //.and(BLOG.DISPLAY_TIME.le(DSL.currentLocalDateTime())));
        if (!StringUtils.isEmpty(request.getTitle())) {
            selectQuery.and(BLOG.TITLE.like("%" + request.getTitle() + "%"));
        }

        if (!StringUtils.isEmpty(request.getContain()) && !StringUtils.isEmpty(request.getTag())) {
            if (Constants.EQUAL.equals(request.getContain())) {
                selectQuery.and(DSL.lower(BLOG.TAG).like("%" + request.getTag().toLowerCase() + "%"));
            }
            if (Constants.NOT_EQUAL.equals(request.getContain())) {
                selectQuery.and(BLOG.TAG.notLike("%" + request.getTag() + "%"));
            }
        }

        if (!StringUtils.isEmpty(request.getCreateUser())) {
            selectQuery.and(BLOG.CREATE_USER.eq(request.getCreateUser()));
        }

        return queryPage(selectQuery, request.getPage(), request.getLimit(), dsl)
                .map(this::readBasicInfo);
    }
}
