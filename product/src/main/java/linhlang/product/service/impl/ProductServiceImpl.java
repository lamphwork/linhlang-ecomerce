package linhlang.product.service.impl;

import linhlang.commons.aop.annotation.LockBusiness;
import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.PageData;
import linhlang.product.constants.CacheKey;
import linhlang.product.constants.Errors;
import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.controller.request.QueryProductReq;
import linhlang.product.controller.request.SaveVariantReq;
import linhlang.product.model.*;
import linhlang.product.repository.CategoryRepository;
import linhlang.product.repository.ProviderRepository;
import linhlang.product.repository.ProductRepository;
import linhlang.product.repository.VariantRepository;
import linhlang.product.service.FileService;
import linhlang.product.service.ProductService;
import linhlang.product.service.mapper.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final VariantMapper variantMapper;
    private final ProductMapper productMapper;

    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public static final String PRODUCT_BUCKET = "product";

    @Override
    public Product createProduct(ProductSaveReq request) {
        Provider provider = createIfNotExist(request.getProvider());
        Category category = createIfNotExist(request.getCategory());
        Set<Image> images = request.getImages();

        String mainImg = images.stream()
                .findFirst()
                .map(Image::getUrl)
                .orElse(null);

        Product product = productMapper.toEntity(request);
        product.setImage(mainImg);
        product.setProvider(provider);
        product.setCategory(category);
        productRepository.save(product);

        product.setVariants(saveVariants(product, request.getVariants()));

        return product;
    }

    @Override
    @LockBusiness(business = "update_product", key = "#productId", timeout = 60 * 2)
    @CacheEvict(cacheNames = CacheKey.PRODUCT, key = "#productId")
    public Product updateProduct(String productId, ProductSaveReq request) {
        Product product = productRepository.load(productId);
        productMapper.update(product, request);

        Provider provider = createIfNotExist(request.getProvider());
        Category category = createIfNotExist(request.getCategory());
        product.setProvider(provider);
        product.setCategory(category);

        productRepository.save(product);

        saveVariants(product, request.getVariants());

        return product;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheKey.PRODUCT, key = "#productId", sync = true)
    public Product detail(String productId) {
        Product product = productRepository.load(productId);

        if (product == null) {
            throw new BusinessException(Errors.PRODUCT_NOTFOUND);
        }

        product.setVariants(variantRepository.loadAll(productId));
        return product;
    }

    @Override
    @CacheEvict(cacheNames = CacheKey.PRODUCT, key = "#productId")
    public Product delete(String productId) {
        Product product = productRepository.load(productId);

        if (product == null) {
            throw new BusinessException(Errors.PRODUCT_NOTFOUND);
        }

        productRepository.delete(product.getId());
        return product;
    }

    @Override
    public PageData<Product> query(QueryProductReq request) {
        return productRepository.query(request);
    }

    @Override
    @CacheEvict(cacheNames = CacheKey.PRODUCT, key = "#productId")
    public List<Image> uploadImages(String productId, MultipartFile[] files) throws IOException {
        Product product = detail(productId);
        if (product == null) {
            throw new BusinessException(Errors.PRODUCT_NOTFOUND);
        }

        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String objectName = product.getId() + "/" + file.getOriginalFilename();
            fileService.upload(PRODUCT_BUCKET, objectName, file);

            Image image = new Image();
            image.setUrl(PRODUCT_BUCKET + "/" + objectName);
            images.add(image);
        }

        product.getImages().addAll(images);
        productRepository.save(product);
        return images;
    }

    /**
     * return provider or new provider if not existed
     *
     * @param provider provider received from request
     * @return provider
     */
    public Provider createIfNotExist(@NonNull Provider provider) {
        if (StringUtils.isBlank(provider.getId())) {
            providerRepository.save(provider);
            return provider;
        }

        Provider existed = providerRepository.load(provider.getId());
        if (existed == null) {
            throw new BusinessException(Errors.PROVIDER_NOTFOUND);
        }
        return existed;
    }

    /**
     * return category or new category if not existed
     *
     * @param category category received from request
     * @return category
     */
    public Category createIfNotExist(@NonNull Category category) {
        if (StringUtils.isBlank(category.getId())) {
            categoryRepository.save(category);
            return category;
        }

        Category existed = categoryRepository.load(category.getId());
        if (existed == null) {
            throw new BusinessException(Errors.CATEGORY_NOTFOUND);
        }
        return existed;
    }

    /**
     * create variants
     *
     * @param product     product
     * @param variantReqs variant request info
     * @return variants
     */
    private Set<Variant> saveVariants(Product product, Set<SaveVariantReq> variantReqs) {
        Set<Variant> variants = new LinkedHashSet<>();
        for (SaveVariantReq variantReq : variantReqs) {
            Variant variant = variantMapper.toEntity(variantReq);
            variant.setTitle(StringUtils.defaultString("DEFAULT"));
            variant.setProductId(product.getId());
            variants.add(variant);
        }
        variantRepository.saveAll(product.getId(), variants);
        return variants;
    }

}


