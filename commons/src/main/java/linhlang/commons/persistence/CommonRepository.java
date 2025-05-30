package linhlang.commons.persistence;

import linhlang.commons.model.PageData;
import org.jooq.*;
import org.jooq.Record;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface CommonRepository<T, ID> {

    void save(T data);

    default void saveAll(Iterable<T> data) {
        throw new UnsupportedOperationException();
    }

    T load(ID id);

    void delete(ID id);

    default List<T> findAllById(Iterable<ID> ids) {
        throw new UnsupportedOperationException();
    }

    default PageData<Record> queryPage(SelectConditionStep<Record> query, int page, int limit, DSLContext dsl) {
        var count = dsl.fetchCount(query);
        var pagedQuery = query.offset(page * limit).limit(limit);
        var dbRecords = pagedQuery.fetch();
        return new PageData<>(page, limit, count, dbRecords);
    }

    default PageData<Record> queryPage(SelectQuery<Record> query, SelectConditionStep<Record1<Integer>> countQuery, int page, int limit, Collection<? extends OrderField<?>> orderFields) {
        var count = Objects.requireNonNull(countQuery.fetchOne()).getValue(0, Integer.class);
        var dbRecords = query.fetch();
        return new PageData<>(page, limit, count, dbRecords);
    }

    default PageData<Record> queryPage(SelectQuery<Record> query, SelectConditionStep<Record1<Integer>> countQuery, int page, int limit) {
        return queryPage(query, countQuery, page, limit, Collections.emptyList());
    }

}
