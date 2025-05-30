package linhlang.webconfig.service.mapper;

import linhlang.webconfig.controller.request.PageContentRequest;
import linhlang.webconfig.model.PageContent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PageContentMapper extends BaseMapper<PageContent, PageContentRequest>{
}
