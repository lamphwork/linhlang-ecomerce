package linhlang.product.service.mapper;

import linhlang.product.model.Provider;
import linhlang.product.repository.entities.ProviderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProviderMapper {

    Provider toModel(ProviderEntity entity);
}
