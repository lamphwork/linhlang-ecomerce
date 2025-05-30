package linhlang.product.repository;

import linhlang.commons.persistence.CommonRepository;
import linhlang.product.model.Property;
import linhlang.product.model.Variant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jooq.generated.Tables.*;

@Component
@RequiredArgsConstructor
public class VariantRepository implements CommonRepository<Variant, String> {

    private final DSLContext dsl;

    @Override
    public void save(Variant data) {
        List<Query> batch = new LinkedList<>();
        batch.add(saveVariantQuery(data));
        batch.addAll(savePropertiesQuery(data));
        dsl.batch(batch).execute();
    }


    public void saveAll(String productId, Collection<Variant> variants) {
        List<Query> batch = new LinkedList<>();
        for (Variant data : variants) {
            data.setProductId(productId);
            batch.add(saveVariantQuery(data));
            batch.addAll(savePropertiesQuery(data));
        }

        // remove variant not in request
        List<String> variantIds = variants.stream().map(Variant::getId)
                .filter(StringUtils::isNotBlank)
                .toList();
        batch.add(
                dsl.delete(VARIANT).where(VARIANT.PRODUCT_ID.eq(productId)).and(VARIANT.ID.notIn(variantIds))
        );
        dsl.batch(batch).execute();
    }

    private Query saveVariantQuery(Variant variant) {
        if (StringUtils.isBlank(variant.getId())) {
            variant.setId(UUID.randomUUID().toString());
            return dsl.insertInto(VARIANT)
                    .set(VARIANT.ID, variant.getId())
                    .set(VARIANT.NAME, variant.getTitle())
                    .set(VARIANT.PRODUCT_ID, variant.getProductId())
                    .set(VARIANT.PRICE, variant.getPrice())
                    .set(VARIANT.SKU, variant.getSku())
                    .set(VARIANT.BARCODE, variant.getBarCode())
                    .set(VARIANT.ACTIVE, variant.getActive() ? (short) 1 : (short) 0);
        } else {
            return dsl.update(VARIANT)
                    .set(VARIANT.NAME, variant.getTitle())
                    .set(VARIANT.PRODUCT_ID, variant.getProductId())
                    .set(VARIANT.PRICE, variant.getPrice())
                    .set(VARIANT.SKU, variant.getSku())
                    .set(VARIANT.BARCODE, variant.getBarCode())
                    .set(VARIANT.ACTIVE, variant.getActive() ? (short) 1 : (short) 0)
                    .where(VARIANT.ID.eq(variant.getId()));
        }
    }

    private List<Query> savePropertiesQuery(Variant variant) {
        Set<Property> properties = variant.getProperties();
        List<Query> batch = new LinkedList<>();

        Set<String> finalAttr = properties.stream()
                .map(Property::getAttribute)
                .collect(Collectors.toSet());

        // query to delete props not in request
        batch.add(
                dsl.delete(PROPERTIES).where(PROPERTIES.ATTRIBUTE.notIn(finalAttr))
        );

        // query to create or update attribute in request
        for (Property property : variant.getProperties()) {
            if (StringUtils.isBlank(property.getAttribute())) {
                String newAttrId = UUID.randomUUID().toString();
                batch.add(
                        dsl.insertInto(ATTRIBUTE)
                                .set(ATTRIBUTE.ID, newAttrId)
                                .set(ATTRIBUTE.NAME, property.getAttributeName())
                                .set(ATTRIBUTE.SYSTEM_ATTR, (short) 0)
                );
                batch.add(
                        dsl.insertInto(PROPERTIES)
                                .set(PROPERTIES.ID, UUID.randomUUID().toString())
                                .set(PROPERTIES.ATTRIBUTE, newAttrId)
                                .set(PROPERTIES.VALUE, property.getValue())
                                .set(PROPERTIES.VARIANT_ID, variant.getId())
                );
            } else {
                batch.add(
                        dsl.insertInto(PROPERTIES)
                                .set(PROPERTIES.ID, UUID.randomUUID().toString())
                                .set(PROPERTIES.VALUE, property.getValue())
                                .set(PROPERTIES.ATTRIBUTE, property.getAttribute())
                                .set(PROPERTIES.VARIANT_ID, variant.getId())
                                .onDuplicateKeyUpdate()
                                .set(PROPERTIES.VALUE, property.getValue())
                );
            }
        }
        return batch;
    }

    @Override
    public Variant load(String s) {
        Record record = dsl.select()
                .from(VARIANT)
                .where(VARIANT.ID.eq(s))
                .fetchOne();

        if (record == null) {
            return null;
        }

        return record.into(Variant.class);
    }

    @Override
    public void delete(String s) {
        dsl.delete(VARIANT).where(VARIANT.ID.eq(s)).execute();
    }

    public Set<Variant> loadAll(String productId) {
        Result<Record> records = dsl.select()
                .from(VARIANT)
                .leftJoin(PROPERTIES).on(VARIANT.ID.eq(PROPERTIES.VARIANT_ID))
                .leftJoin(ATTRIBUTE).on(ATTRIBUTE.ID.eq(PROPERTIES.ATTRIBUTE))
                .where(VARIANT.PRODUCT_ID.eq(productId))
                .orderBy(VARIANT.ID)
                .fetch();

        Map<String, Variant> mapIdVariants = new HashMap<>();
        Set<Variant> variants = new LinkedHashSet<>();
        for (Record record : records) {
            Variant variant = mapIdVariants.get(record.getValue(VARIANT.ID));
            if (variant == null) {
                variant = new Variant();
                variant.setId(record.getValue(VARIANT.ID));
                variant.setTitle(record.getValue(VARIANT.NAME));
                variant.setProductId(record.getValue(VARIANT.PRODUCT_ID));
                variant.setImage(record.getValue(VARIANT.IMAGE));
                variant.setPrice(record.getValue(VARIANT.PRICE));
                variant.setActive(record.getValue(VARIANT.ACTIVE) == 1);
                variant.setBarCode(record.getValue(VARIANT.BARCODE));
                variant.setSku(record.getValue(VARIANT.SKU));
                mapIdVariants.put(variant.getId(), variant);
            }

            Property property = new Property(
                    record.get(ATTRIBUTE.ID),
                    record.get(ATTRIBUTE.NAME),
                    record.get(PROPERTIES.VALUE)
            );

            variant.getProperties().add(property);
            variants.add(variant);
        }

        return variants;
    }

}

