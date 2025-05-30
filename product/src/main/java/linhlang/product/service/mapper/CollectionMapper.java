package linhlang.product.service.mapper;

import linhlang.product.controller.request.SaveCollectionReq;
import linhlang.product.model.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CollectionMapper extends BaseMapper<Collection, SaveCollectionReq> {
}
