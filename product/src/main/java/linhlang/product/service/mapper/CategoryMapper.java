package linhlang.product.service.mapper;

import linhlang.product.controller.request.SaveCategoryReq;
import linhlang.product.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper extends BaseMapper<Category, SaveCategoryReq> {
}
