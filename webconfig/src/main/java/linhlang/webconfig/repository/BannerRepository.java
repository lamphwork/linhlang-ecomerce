package linhlang.webconfig.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.commons.persistence.CommonRepository;
import linhlang.webconfig.model.Banner;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.WebContentRecord;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.Tables.WEB_CONTENT;

@Repository
@RequiredArgsConstructor
public class BannerRepository implements CommonRepository<Banner, String> {

    private final DSLContext dsl;
    private final String WEB_CONTENT_TYPE = "BANNER";

    /**
     * write model into db record
     *
     * @param banner model
     * @return db record
     */
    private WebContentRecord writeDB(Banner banner) {
        return new WebContentRecord(
                banner.getId(),
                WEB_CONTENT_TYPE,
                null,
                null,
                banner.getImageUrl(),
                banner.getOrderIndex(),
                true
        );
    }

    /**
     * read db record into model
     *
     * @param record db record
     * @return model
     */
    private Banner readDB(WebContentRecord record) {
        if (record == null) {
            return null;
        }
        return new Banner(
                record.getId(),
                record.getImageUrl(),
                record.getOrderIndex()
        );
    }

    @Override
    public void save(Banner data) {
        dsl.insertInto(WEB_CONTENT)
                .set(writeDB(data))
                .onDuplicateKeyUpdate()
                .set(writeDB(data))
                .where(WEB_CONTENT.ID.eq(data.getId()))
                .execute();
    }

    @Override
    public Banner load(String s) {
        WebContentRecord record = dsl.select()
                .from(WEB_CONTENT)
                .where(WEB_CONTENT.ID.eq(s))
                .and(WEB_CONTENT.TYPE.eq(WEB_CONTENT_TYPE))
                .fetchOneInto(WebContentRecord.class);

        return readDB(record);
    }

    @Override
    public void delete(String s) {
        dsl.deleteFrom(WEB_CONTENT)
                .where(WEB_CONTENT.ID.eq(s))
                .and(WEB_CONTENT.TYPE.eq(WEB_CONTENT_TYPE))
                .execute();
    }


    public PageData<Banner> queryAll(QueryRequest request) {
        var query = dsl.select()
                .from(WEB_CONTENT)
                .where(WEB_CONTENT.TYPE.eq(WEB_CONTENT_TYPE));
        return queryPage(query, request.getPage(), request.getLimit(), dsl)
                .map(record -> record.into(WebContentRecord.class))
                .map(this::readDB);
    }
}
