package linhlang.product.service.mapper;

import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper extends BaseMapper<Product, ProductSaveReq> {

}
