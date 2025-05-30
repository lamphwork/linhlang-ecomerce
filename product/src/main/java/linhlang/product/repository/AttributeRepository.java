package linhlang.product.repository;

import linhlang.commons.persistence.CommonRepository;
import linhlang.product.model.Attribute;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.jooq.generated.Tables.ATTRIBUTE;

@Repository
@RequiredArgsConstructor
public class AttributeRepository implements CommonRepository<Attribute, String> {

    private final DSLContext dsl;

    @Override
    public void save(Attribute data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            dsl.insertInto(ATTRIBUTE)
                    .set(ATTRIBUTE.ID, data.getId())
                    .set(ATTRIBUTE.NAME, data.getName())
                    .set(ATTRIBUTE.SYSTEM_ATTR, data.getSystemAttr() ? (short) 1 : (short) 0)
                    .execute();
        } else {
            dsl.update(ATTRIBUTE)
                    .set(ATTRIBUTE.NAME, data.getName())
                    .set(ATTRIBUTE.SYSTEM_ATTR, data.getSystemAttr() ? (short) 1 : (short) 0)
                    .where(ATTRIBUTE.ID.eq(data.getId()))
                    .execute();
        }
    }

    @Override
    public Attribute load(String s) {
        Record record = dsl.select()
                .from(ATTRIBUTE)
                .where(ATTRIBUTE.ID.eq(s))
                .fetchOne();

        if (record == null) {
            return null;
        }

        return record.into(Attribute.class);
    }

    @Override
    public void delete(String s) {
        dsl.delete(ATTRIBUTE).where(ATTRIBUTE.ID.eq(s)).execute();
    }
}
