package linhlang.webconfig.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.commons.persistence.CommonRepository;
import linhlang.webconfig.model.Sticky;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.WebContentRecord;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.Tables.WEB_CONTENT;

@Repository
@RequiredArgsConstructor
public class StickyRepository implements CommonRepository<Sticky, String> {

    private final DSLContext dsl;
    private final String WEB_CONTENT_TYPE = "STICKY";

    /**
     * write model into db record
     *
     * @param sticky model
     * @return db record
     */
    private WebContentRecord writeDB(Sticky sticky) {
        return new WebContentRecord(
                sticky.getId(),
                WEB_CONTENT_TYPE,
                sticky.getTitle(),
                sticky.getContent(),
                null,
                null,
                sticky.getVisible()
        );
    }

    /**
     * load model from db record
     *
     * @param record db record
     * @return model
     */
    private Sticky readDB(WebContentRecord record) {
        if (record == null) {
            return null;
        }
        return new Sticky(
                record.getId(),
                record.getTitle(),
                record.getContent(),
                record.getVisible()
        );
    }

    @Override
    public void save(Sticky data) {
        dsl.insertInto(WEB_CONTENT)
                .set(writeDB(data))
                .onDuplicateKeyUpdate()
                .set(writeDB(data))
                .where(WEB_CONTENT.ID.eq(data.getId()))
                .execute();
    }

    @Override
    public Sticky load(String s) {
        WebContentRecord record = dsl.select()
                .from(WEB_CONTENT)
                .where(WEB_CONTENT.TYPE.eq(WEB_CONTENT_TYPE))
                .and(WEB_CONTENT.ID.eq(s))
                .fetchOneInto(WebContentRecord.class);
        return readDB(record);
    }

    @Override
    public void delete(String s) {
        dsl.deleteFrom(WEB_CONTENT)
                .where(WEB_CONTENT.ID.eq(s))
                .execute();
    }

    public PageData<Sticky> queryAll(QueryRequest request) {
        var query = dsl.select()
                .from(WEB_CONTENT)
                .where(WEB_CONTENT.TYPE.eq(WEB_CONTENT_TYPE));
        return queryPage(query, request.getPage(), request.getLimit(), dsl)
                .map(record -> record.into(WebContentRecord.class))
                .map(this::readDB);
    }
}
