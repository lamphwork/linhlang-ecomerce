package linhlang.product.service.mapper;

import linhlang.product.controller.request.SaveProviderReq;
import linhlang.product.model.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProviderMapper extends BaseMapper<Provider, SaveProviderReq> {
}
