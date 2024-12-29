package linhlang.product.service.mapper;

import linhlang.product.model.Collection;
import linhlang.product.repository.entities.CollectionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CollectionMapper {

    Collection toModel(CollectionEntity entity);

    List<Collection> toModel(List<CollectionEntity> entities);
}
