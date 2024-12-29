package linhlang.product.service.mapper;

import linhlang.product.model.Category;
import linhlang.product.repository.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category toModel(CategoryEntity entity);
}
