package linhlang.product.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.commons.storage.StorageProperties;
import linhlang.commons.utils.UrlUtils;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.model.Collection;
import linhlang.product.model.SEOData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.jooq.generated.Tables.COLLECTION;

@Repository
@RequiredArgsConstructor
public class CollectionRepository implements CommonRepository<Collection, String> {

    private final DSLContext dsl;
    private final StorageProperties properties;

    @Override
    public void save(Collection data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            dsl.insertInto(COLLECTION)
                    .set(dbDataAsMap(data))
                    .execute();
            return;
        }

        dsl.update(COLLECTION)
                .set(dbDataAsMap(data))
                .where(COLLECTION.ID.eq(data.getId()))
                .execute();
    }

    @Override
    public Collection load(String s) {
        var dbRecord = dsl.select()
                .from(COLLECTION)
                .where(COLLECTION.ID.eq(s))
                .fetchOne();

        if (dbRecord == null) {
            return null;
        }

        return readDB(dbRecord);
    }

    @Override
    public void delete(String s) {
        dsl.delete(COLLECTION).where(COLLECTION.ID.eq(s)).execute();
    }

    private Map<Field<?>, Object> dbDataAsMap(Collection collection) {
        Map<Field<?>, Object> data = new LinkedHashMap<>();
        data.put(COLLECTION.ID, collection.getId());
        data.put(COLLECTION.NAME, collection.getName());
        data.put(COLLECTION.IMAGE, collection.getImage());
        data.put(COLLECTION.DESCRIPTION, collection.getDescription());
        data.put(COLLECTION.UI, collection.getUi());

        if (collection.getSeo() != null) {
            data.put(COLLECTION.SEO_TITLE, collection.getSeo().getTitle());
            data.put(COLLECTION.SEO_LINK, collection.getSeo().getLink());
            data.put(COLLECTION.SEO_DESCRIPTION, collection.getSeo().getDescription());
        } else {
            data.put(COLLECTION.SEO_TITLE, null);
            data.put(COLLECTION.SEO_LINK, null);
            data.put(COLLECTION.SEO_DESCRIPTION, null);
        }
        return data;
    }

    private Collection readDB(Record dbRecord) {
        Collection collection = new Collection();
        collection.setId(dbRecord.getValue(COLLECTION.ID));
        collection.setName(dbRecord.getValue(COLLECTION.NAME));
        collection.setImage(UrlUtils.appendDomain(dbRecord.getValue(COLLECTION.IMAGE), properties.getPublicUrl()));
        collection.setDescription(dbRecord.getValue(COLLECTION.DESCRIPTION));
        collection.setUi(dbRecord.getValue(COLLECTION.UI));

        SEOData seoData = new SEOData();
        seoData.setTitle(dbRecord.getValue(COLLECTION.SEO_TITLE));
        seoData.setDescription(dbRecord.getValue(COLLECTION.SEO_DESCRIPTION));
        seoData.setLink(dbRecord.getValue(COLLECTION.SEO_LINK));
        collection.setSeo(seoData);
        return collection;
    }

    public PageData<Collection> query(CommonSearchReq req) {
        List<Condition> conditions = new LinkedList<>();
        if (StringUtils.isNotBlank(req.getQuery())) {
            conditions.add(
                    COLLECTION.NAME.containsIgnoreCase(StringUtils.trim(req.getQuery()))
            );
        }

        var selectQuery = dsl.select()
                .from(COLLECTION)
                .where(conditions)
                .limit(req.getLimit())
                .offset(req.getLimit() * req.getPage())
                .getQuery();
        var countQuery = dsl.select(DSL.count(DSL.value(1)))
                .from(COLLECTION)
                .where(conditions);

        return queryPage(selectQuery, countQuery, req.getPage(), req.getLimit())
                .map(this::readDB);
    }
}
