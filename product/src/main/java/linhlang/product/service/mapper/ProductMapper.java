package linhlang.product.service.mapper;

import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.model.Product;
import linhlang.product.repository.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product toModel(ProductSaveReq request);

    void update(@MappingTarget ProductEntity productEntity, ProductSaveReq request);

    Product toModel(ProductEntity entity);

    ProductEntity toEntity(Product product);

    ProductEntity toEntity(ProductSaveReq request);
}
