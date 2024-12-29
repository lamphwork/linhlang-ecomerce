package linhlang.product.service.mapper;

import linhlang.product.model.Collection;
import linhlang.product.model.Image;
import linhlang.product.repository.entities.ProductImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductImageMapper extends BaseMapper<ProductImageEntity, Image> {

    List<Image> toModel(List<ProductImageEntity> entities);
}
