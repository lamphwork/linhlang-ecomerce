package linhlang.product.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.commons.storage.StorageProperties;
import linhlang.product.controller.request.QueryProductReq;
import linhlang.product.model.*;
import linhlang.product.model.Collection;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jooq.generated.Tables.*;

@Repository
@RequiredArgsConstructor
public class ProductRepository implements CommonRepository<Product, String> {

    private final DSLContext dsl;
    private final StorageProperties storageProperties;

    @Override
    public void save(Product data) {
        Map<Field<?>, Object> setData = new HashMap<>();
        setData.put(PRODUCT.ID, data.getId());
        setData.put(PRODUCT.NAME, data.getName());
        setData.put(PRODUCT.IMAGE, data.getImage());
        setData.put(PRODUCT.DESCRIPTION, data.getDescription());
        setData.put(PRODUCT.QUOTE, data.getQuote());
        setData.put(PRODUCT.PRICE, data.getPrice());
        setData.put(PRODUCT.COMPARE_PRICE, data.getComparePrice());
        setData.put(PRODUCT.PROVIDER_ID, data.getProvider().getId());
        setData.put(PRODUCT.CATEGORY_ID, data.getCategory().getId());
        setData.put(PRODUCT.SIZE, data.getSize());
        setData.put(PRODUCT.WEIGHT, data.getWeight());

        if (data.getSeo() != null) {
            setData.put(PRODUCT.SEO_CUSTOM, 1);
            setData.put(PRODUCT.SEO_TITLE, data.getSeo().getTitle());
            setData.put(PRODUCT.SEO_LINK, data.getSeo().getLink());
            setData.put(PRODUCT.SEO_DESCRIPTION, data.getSeo().getDescription());
        } else {
            setData.put(PRODUCT.SEO_CUSTOM, 0);
            setData.put(PRODUCT.SEO_TITLE, null);
            setData.put(PRODUCT.SEO_LINK, null);
            setData.put(PRODUCT.SEO_DESCRIPTION, null);
        }

        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            setData.put(PRODUCT.ID, data.getId());
            dsl.insertInto(PRODUCT)
                    .set(setData)
                    .set(PRODUCT.CREATE_TIME, DSL.currentLocalDateTime())
                    .execute();
        } else {
            dsl.update(PRODUCT)
                    .set(setData)
                    .set(PRODUCT.UPDATE_TIME, DSL.currentLocalDateTime())
                    .where(PRODUCT.ID.eq(data.getId()))
                    .execute();
        }

        saveImages(data.getId(), data.getImages());
        saveCollections(data.getId(), data.getCollections());
    }

    @Override
    public Product load(String s) {
        Record result = dsl.select()
                .from(PRODUCT)
                .leftJoin(PROVIDER).on(PROVIDER.ID.eq(PRODUCT.PROVIDER_ID))
                .leftJoin(CATEGORY).on(CATEGORY.ID.eq(PRODUCT.CATEGORY_ID))
                .leftJoin(PRODUCT_IMAGE).on(PRODUCT_IMAGE.PRODUCT_ID.eq(PRODUCT.ID).and(PRODUCT_IMAGE.IMAGE_ORDER.eq(0)))
                .where(PRODUCT.ID.eq(s))
                .fetchOne();

        return readProductFromDB(result);
    }

    @Override
    public void delete(String s) {
        dsl.delete(PRODUCT).where(PRODUCT.ID.eq(s)).execute();
    }

    public PageData<Product> query(QueryProductReq request) {
        List<Condition> conditions = queryConditions(request);

        var filteredProduct = dsl.select()
                .from(PRODUCT)
                .where(conditions)
                .orderBy(PRODUCT.CREATE_TIME)
                .offset(request.getPage() * request.getLimit())
                .limit(request.getLimit());

        var selectQuery = dsl.select()
                .from(filteredProduct)
                .leftJoin(PROVIDER).on(PROVIDER.ID.eq(filteredProduct.field(PRODUCT.PROVIDER_ID)))
                .leftJoin(CATEGORY).on(CATEGORY.ID.eq(filteredProduct.field(PRODUCT.CATEGORY_ID)))
                .leftJoin(PRODUCT_IMAGE)
                    .on(PRODUCT_IMAGE.PRODUCT_ID.eq(filteredProduct.field(PRODUCT.ID)))
                .getQuery();

        var countQuery = dsl.select(DSL.count(DSL.value(1)))
                .from(PRODUCT)
                .where(conditions);

        Function<Record, Product> readProductFullImage = getProductFullImage();

        var page =  queryPage(
                selectQuery,
                countQuery,
                request.getPage(),
                request.getLimit(),
                List.of(PRODUCT.CREATE_TIME)
        ).map(readProductFullImage);

        page.setContent(page.getContent().stream().distinct().collect(Collectors.toList()));

        return page;
    }

    @NotNull
    private Function<Record, Product> getProductFullImage() {
        Map<String, Product> loadedProduct = new LinkedHashMap<>();
        return (record) -> {
            Product product = readBasicInfo(record);
            if (!loadedProduct.containsKey(product.getId())) {
                loadedProduct.put(product.getId(), product);
            } else {
                product = loadedProduct.get(product.getId());
            }

            Image image = new Image(
                    record.getValue(PRODUCT_IMAGE.ID),
                    storageProperties.getPublicUrl() + "/" + record.getValue(PRODUCT_IMAGE.URL),
                    record.getValue(PRODUCT_IMAGE.IMAGE_ORDER)
            );

            product.getImages().add(image);
            return product;
        };
    }

    private List<Condition> queryConditions(QueryProductReq request) {
        List<Condition> conditions = new LinkedList<>();
        if (!StringUtils.isBlank(request.getSearch())) {
            conditions.add(
                    PRODUCT.NAME.likeIgnoreCase(request.getSearch())
            );
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            conditions.add(
                    PRODUCT.PROVIDER_ID.in(request.getProvider())
            );
        }
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            conditions.add(
                    PRODUCT.CATEGORY_ID.in(request.getCategory())
            );
        }
        if (request.getMinPrice() != null) {
            conditions.add(
                    PRODUCT.PRICE.greaterOrEqual(request.getMinPrice())
            );
        }
        if (request.getMaxPrice() != null) {
            conditions.add(
                    PRODUCT.PRICE.lessOrEqual(request.getMaxPrice())
            );
        }

        return conditions;
    }

    /**
     * read database value into model
     *
     * @param result database value
     * @return product model
     */
    private Product readBasicInfo(Record result) {
        if (result == null) {
            return null;
        }

        Product product = new Product();
        product.setId(result.get(PRODUCT.ID));
        product.setName(result.get(PRODUCT.NAME));
        if (result.get(PRODUCT_IMAGE.URL) != null) {
            product.setImage(
                    storageProperties.getPublicUrl() + "/" + result.get(PRODUCT_IMAGE.URL)
            );
        }
        product.setDescription(result.get(PRODUCT.DESCRIPTION));
        product.setPrice(result.get(PRODUCT.PRICE));
        product.setComparePrice(result.get(PRODUCT.COMPARE_PRICE));
        product.setCreateTime(result.get(PRODUCT.CREATE_TIME));
        product.setUpdateTime(result.get(PRODUCT.UPDATE_TIME));
        product.setWeight(result.get(PRODUCT.WEIGHT));
        product.setSize(result.get(PRODUCT.SIZE));

        if (result.get(PRODUCT.PROVIDER_ID) != null) {
            Provider provider = new Provider();
            provider.setId(result.get(PROVIDER.ID));
            provider.setName(result.get(PROVIDER.NAME));
            product.setProvider(provider);
        }

        if (result.get(PRODUCT.CATEGORY_ID) != null) {
            Category category = new Category();
            category.setId(result.get(CATEGORY.ID));
            category.setName(result.get(CATEGORY.NAME));
            product.setCategory(category);
        }

        if (Objects.equals((short) 1, result.get(PRODUCT.SEO_CUSTOM))) {
            SEOData seo = new SEOData();
            seo.setTitle(result.get(PRODUCT.SEO_TITLE));
            seo.setLink(result.get(PRODUCT.SEO_LINK));
            seo.setDescription(result.get(PRODUCT.SEO_DESCRIPTION));
            product.setSeo(seo);
        }

        return product;
    }

    /**
     * read database value into model
     *
     * @param result database value
     * @return product model
     */
    private Product readProductFromDB(Record result) {
        if (result == null) {
            return null;
        }

        Product product = readBasicInfo(result);

        product.setImages(loadImageForProduct(product.getId()));
        product.setCollections(loadCollectionForProduct(product.getId()));
        return product;
    }

    /**
     * load image for product
     *
     * @param productId product id
     * @return product images
     */
    private Set<Image> loadImageForProduct(String productId) {
        Result<Record> results = dsl.select()
                .from(PRODUCT_IMAGE)
                .where(PRODUCT_IMAGE.PRODUCT_ID.eq(productId))
                .orderBy(PRODUCT_IMAGE.IMAGE_ORDER.asc())
                .fetch();

        return results.stream().map(record -> {
                    Image image = new Image();
                    image.setId(record.getValue(PRODUCT_IMAGE.ID));
                    image.setUrl(
                            storageProperties.getPublicUrl() + "/" +
                                    record.getValue(PRODUCT_IMAGE.URL)
                    );
                    image.setOrderIndex(record.getValue(PRODUCT_IMAGE.IMAGE_ORDER));
                    return image;
                })
                .sorted(Comparator.comparing(Image::getOrderIndex))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * load joined collection
     *
     * @param productId product id
     * @return joined collection
     */
    private Set<Collection> loadCollectionForProduct(String productId) {
        Result<Record> results = dsl.select()
                .from(COLLECTION)
                .innerJoin(PRODUCT_COLLECTION).on(COLLECTION.ID.eq(PRODUCT_COLLECTION.COLLECTION_ID))
                .where(PRODUCT_COLLECTION.PRODUCT_ID.eq(productId))
                .fetch();

        return results.stream().map(record -> {
            Collection collection = new Collection();
            collection.setId(record.getValue(COLLECTION.ID));
            collection.setName(record.getValue(COLLECTION.NAME));
            return collection;
        }).collect(Collectors.toSet());
    }

    private void saveImages(String productId, Set<Image> images) {
        if (images == null) {
            return;
        }

        List<Query> batch = new LinkedList<>();
        int index = 0;
        for (Image image : images) {
            if (StringUtils.isBlank(image.getId())) {
                image.setId(UUID.randomUUID().toString());
                batch.add(
                        dsl.insertInto(PRODUCT_IMAGE)
                                .set(PRODUCT_IMAGE.PRODUCT_ID, productId)
                                .set(PRODUCT_IMAGE.IMAGE_ORDER, index++)
                                .set(PRODUCT_IMAGE.ID, image.getId())
                                .set(PRODUCT_IMAGE.URL, image.getUrl())
                );
            } else {
                batch.add(
                        dsl.update(PRODUCT_IMAGE)
                                .set(PRODUCT_IMAGE.IMAGE_ORDER, index++)
                                .where(PRODUCT_IMAGE.PRODUCT_ID.eq(productId))
                                .and(PRODUCT_IMAGE.ID.eq(image.getId()))
                );
            }
        }

        List<String> imageId = images.stream().map(Image::getId)
                .toList();
        batch.add(
                dsl.delete(PRODUCT_IMAGE)
                        .where(PRODUCT_IMAGE.PRODUCT_ID.eq(productId))
                        .and(PRODUCT_IMAGE.ID.notIn(imageId))
        );
        dsl.batch(batch).execute();
    }

    public void saveCollections(String productId, Set<Collection> collections) {
        dsl.delete(PRODUCT_COLLECTION).where(PRODUCT_COLLECTION.PRODUCT_ID.eq(productId)).execute();
        if (collections == null) {
            return;
        }

        List<Query> batch = new LinkedList<>();
        for (Collection collection : collections) {
            Query query = dsl.insertInto(PRODUCT_COLLECTION)
                    .set(PRODUCT_COLLECTION.PRODUCT_ID, productId)
                    .set(PRODUCT_COLLECTION.COLLECTION_ID, collection.getId())
                    .set(PRODUCT_COLLECTION.ID, UUID.randomUUID().toString());
            batch.add(query);
        }
        dsl.batch(batch).execute();
    }
}
