package linhlang.product.service.impl;

import linhlang.commons.exceptions.BusinessException;
import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.model.*;
import linhlang.product.model.Collection;
import linhlang.product.repository.CollectionRepository;
import linhlang.product.repository.ProductCollectionRepository;
import linhlang.product.repository.ProductImageRepository;
import linhlang.product.repository.entities.*;
import linhlang.product.repository.jpa.CategoryRepository;
import linhlang.product.repository.jpa.ProviderRepository;
import linhlang.product.repository.ProductRepository;
import linhlang.product.service.ProductService;
import linhlang.product.service.mapper.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static linhlang.product.constants.Errors.PRODUCT_NOTFOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProviderMapper providerMapper;
    private final CategoryMapper categoryMapper;
    private final CollectionMapper collectionMapper;
    private final ProductImageMapper productImageMapper;

    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final CollectionRepository collectionRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductCollectionRepository productCollectionRepository;

    @Override
    public Product createProduct(ProductSaveReq request) {
        ProductEntity productEntity = productMapper.toEntity(request);

        var provider = findOrCreateProvider(request.getProvider());
        productEntity.setProviderId(provider.getId());

        var category = findOrCreateCategory(request.getCategory());
        productEntity.setCategoryId(category.getId());

        productEntity.setId(UUID.randomUUID().toString());
        productEntity.setCreateTime(LocalDateTime.now());
        productRepository.save(productEntity);

        //save collections for product
        var collections = setCollections(productEntity, request.getCollections());
        // save images for product
        var images = setImages(productEntity, request.getImages());

        return aggregate(productEntity, provider, category, images, collections);
    }

    @Override
    public Product updateProduct(String productId, ProductSaveReq request) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(PRODUCT_NOTFOUND));

        productMapper.update(productEntity, request);

        var provider = findOrCreateProvider(request.getProvider());
        productEntity.setProviderId(provider.getId());

        var category = findOrCreateCategory(request.getCategory());
        productEntity.setCategoryId(category.getId());

        productEntity.setUpdateTime(LocalDateTime.now());
        productRepository.save(productEntity);

        //save collections for product
        var collections = setCollections(productEntity, request.getCollections());
        // save images for product
        var images = setImages(productEntity, request.getImages());

        return aggregate(productEntity, provider, category, images, collections);
    }

    @Override
    @Transactional(readOnly = true)
    public Product detail(String productId) {
        Optional<ProductEntity> optProduct = productRepository.findById(productId);
        if (optProduct.isEmpty()) {
            throw new BusinessException(PRODUCT_NOTFOUND);
        }

        ProductEntity productEntity = optProduct.get();
        ProviderEntity providerEntity = providerRepository.findById(productEntity.getProviderId()).orElse(null);
        CategoryEntity categoryEntity = categoryRepository.findById(productEntity.getCategoryId()).orElse(null);
        List<ProductImageEntity> images = productImageRepository.findAllByProductId(productEntity.getId());
        List<CollectionEntity> collections = collectionRepository.findAllByProductId(productEntity.getId());
        return aggregate(productEntity, providerEntity, categoryEntity, images, collections);
    }

    @Override
    public List<Product> query() {
        return List.of();
    }

    /**
     * return provider or new provider if not existed
     *
     * @param provider provider received from request
     * @return provider
     */
    public ProviderEntity findOrCreateProvider(@NonNull Provider provider) {
        return Optional.ofNullable(provider.getId())
                .flatMap(providerRepository::findById)
                .orElseGet(() -> {
                    ProviderEntity newProvider = new ProviderEntity();
                    newProvider.setId(UUID.randomUUID().toString());
                    newProvider.setName(provider.getName());
                    return providerRepository.save(newProvider);
                });
    }

    /**
     * return category or new category if not existed
     *
     * @param category category received from request
     * @return category
     */
    public CategoryEntity findOrCreateCategory(@NonNull Category category) {
        return Optional.ofNullable(category.getId())
                .flatMap(categoryRepository::findById)
                .orElseGet(() -> {
                    CategoryEntity newCategory = new CategoryEntity();
                    newCategory.setId(UUID.randomUUID().toString());
                    newCategory.setName(category.getName());
                    return categoryRepository.save(newCategory);
                });
    }

    /**
     * set collections for product
     *
     * @param product     product
     * @param collections collections
     * @return joined collections
     */
    public List<CollectionEntity> setCollections(ProductEntity product, Set<Collection> collections) {
        if (collections == null || collections.isEmpty()) {
            return Collections.emptyList();
        }

        var collectionIds = collections.stream().map(Collection::getId).collect(Collectors.toSet());
        var validCollection = collectionRepository.findAllById(collectionIds);
        List<ProductCollectionEntity> productCollectionEntities = validCollection.stream()
                .map(collectionEntity -> {
                    var productMapCollection = new ProductCollectionEntity();
                    productMapCollection.setId(UUID.randomUUID().toString());
                    productMapCollection.setCollectionId(collectionEntity.getId());
                    productMapCollection.setProductId(product.getId());
                    return productMapCollection;
                })
                .toList();
        productCollectionRepository.saveAll(productCollectionEntities);
        return validCollection;
    }

    /**
     * save images for product
     *
     * @param product product
     * @param images  images
     * @return saved images
     */
    public List<ProductImageEntity> setImages(ProductEntity product, Set<Image> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        // get current image for product as map {key: image id, value: image}
        Map<String, ProductImageEntity> currentImgMap = productImageRepository.findAllByProductId(product.getId())
                .stream()
                .collect(Collectors.toMap(
                        ProductImageEntity::getId,
                        Function.identity()
                ));

        int index = 0;
        List<ProductImageEntity> saveList = new LinkedList<>();
        for (Image image : images) {
            ProductImageEntity productImgEntity = currentImgMap.get(image.getId());
            if (productImgEntity != null) { // case update image
                productImageMapper.update(productImgEntity, image);
            } else { // case create image
                productImgEntity = productImageMapper.toEntity(image);
                productImgEntity.setId(UUID.randomUUID().toString());
                productImgEntity.setProductId(product.getId());
            }
            productImgEntity.setImageOrder(index++);
            saveList.add(productImgEntity);
        }

        Set<String> savedId = saveList.stream().map(ProductImageEntity::getId).collect(Collectors.toSet());
        List<String> deleteList = currentImgMap.keySet().stream()
                .filter(element -> !savedId.contains(element))
                .toList();
        productImageRepository.deleteAllById(deleteList);
        return productImageRepository.saveAll(saveList);
    }

    public Product aggregate(ProductEntity product, ProviderEntity provider, CategoryEntity category, List<ProductImageEntity> productImages, List<CollectionEntity> collections) {
        Product result = productMapper.toModel(product);
        result.setProvider(providerMapper.toModel(provider));
        result.setCategory(categoryMapper.toModel(category));
        result.setImages(new HashSet<>(productImageMapper.toModel(productImages)));
        result.setCollections(new HashSet<>(collectionMapper.toModel(collections)));
        return result;
    }
}
