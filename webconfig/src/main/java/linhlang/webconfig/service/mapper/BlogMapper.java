package linhlang.webconfig.service.mapper;

import linhlang.webconfig.controller.request.BlogRequest;
import linhlang.webconfig.model.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlogMapper  extends BaseMapper<Blog, BlogRequest>{
}
