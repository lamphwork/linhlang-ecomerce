package linhlang.product.service.mapper;

import linhlang.product.controller.request.SaveVariantReq;
import linhlang.product.model.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VariantMapper extends BaseMapper<Variant, SaveVariantReq> {
}
