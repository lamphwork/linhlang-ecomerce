package linhlang.product.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.model.Category;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.jooq.generated.Tables.*;

@Repository
@RequiredArgsConstructor
public class CategoryRepository implements CommonRepository<Category, String> {

    private final DSLContext dsl;

    public Category findSameName(String name) {
        Record record = dsl.select()
                .from(CATEGORY)
                .where(CATEGORY.NAME.likeIgnoreCase(StringUtils.trim(name)))
                .fetchOne();

        if (record == null) {
            return null;
        }

        return record.into(Category.class);
    }

    public PageData<Category> query(CommonSearchReq req) {
        List<Condition> conditions = new LinkedList<>();
        if (StringUtils.isNotBlank(req.getQuery())) {
            conditions.add(CATEGORY.NAME.containsIgnoreCase(StringUtils.trim(req.getQuery())));
        }

        var selectQuery = dsl.select()
                .from(CATEGORY)
                .where(conditions)
                .limit(req.getLimit())
                .offset(req.getLimit() * req.getPage())
                .getQuery();
        var countQuery = dsl.select(DSL.count())
                .from(CATEGORY)
                .where(conditions);

        return queryPage(selectQuery, countQuery, req.getPage(), req.getLimit())
                .map(record -> {
                    Category category = new Category();
                    category.setId(record.getValue(CATEGORY.ID));
                    category.setName(record.getValue(CATEGORY.NAME));
                    return category;
                });
    }

    @Override
    public void save(Category data) {
        if (StringUtils.isNotBlank(data.getId())) {
            dsl.update(CATEGORY)
                    .set(CATEGORY.NAME, data.getName())
                    .where(CATEGORY.ID.eq(data.getId()))
                    .execute();
            return;
        }

        data.setId(UUID.randomUUID().toString());
        dsl.insertInto(CATEGORY)
                .set(CATEGORY.ID, data.getId())
                .set(CATEGORY.NAME, data.getName())
                .execute();
    }

    @Override
    public Category load(String s) {
        Record record = dsl.select()
                .from(CATEGORY)
                .where(CATEGORY.ID.eq(s))
                .fetchOne();

        if (record == null) {
            return null;
        }

        return record.into(Category.class);
    }

    @Override
    public void delete(String s) {
        dsl.delete(CATEGORY).where(CATEGORY.ID.eq(s)).execute();
    }
}