package linhlang.product.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.model.Provider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.generated.Tables.*;

@Repository
@RequiredArgsConstructor
public class ProviderRepository implements CommonRepository<Provider, String> {

    private final DSLContext dsl;

    public PageData<Provider> query(CommonSearchReq req) {
        String input = StringUtils.trim(req.getQuery());
        List<Condition> conditions = new LinkedList<>();

        if (StringUtils.isNotBlank(input)) {
            conditions.add(DSL.lower(PROVIDER.NAME).containsIgnoreCase(input));
        }

        var selectQuery = dsl.select()
                .from(PROVIDER)
                .where(conditions)
                .limit(req.getLimit())
                .offset(req.getLimit() * req.getPage())
                .getQuery();
        var countQuery = dsl.select(DSL.count())
                .from(PROVIDER)
                .where(conditions);

        return queryPage(selectQuery, countQuery, req.getPage(), req.getLimit())
                .map(record -> {
                    Provider provider = new Provider();
                    provider.setId(record.getValue(PROVIDER.ID));
                    provider.setName(record.getValue(PROVIDER.NAME));
                    return provider;
                });
    }

    public Provider findSameName(String name) {
        Record record = dsl.select()
                .from(PROVIDER)
                .where(PROVIDER.NAME.likeIgnoreCase(StringUtils.trim(name)))
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(record)
                .map(record1 -> record1.into(Provider.class))
                .orElse(null);
    }

    @Override
    public void save(Provider data) {
        if (StringUtils.isNotBlank(data.getId())) {
            dsl.update(PROVIDER)
                    .set(PROVIDER.NAME, data.getName())
                    .where(PROVIDER.ID.eq(data.getId()))
                    .execute();
            return;
        }

        data.setId(UUID.randomUUID().toString());
        dsl.insertInto(PROVIDER)
                .set(PROVIDER.ID, data.getId())
                .set(PROVIDER.NAME, StringUtils.trim(data.getName()))
                .execute();
    }

    @Override
    public Provider load(String s) {
        Record dbRecord = dsl.select()
                .from(PROVIDER)
                .where(PROVIDER.ID.eq(s))
                .fetchOne();

        if (dbRecord != null) {
            return dbRecord.into(Provider.class);
        }

        return null;
    }

    @Override
    public void delete(String s) {
        dsl.delete(PROVIDER).where(PROVIDER.ID.eq(s)).execute();
    }
}