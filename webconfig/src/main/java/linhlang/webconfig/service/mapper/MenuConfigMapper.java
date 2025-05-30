package linhlang.webconfig.service.mapper;

import linhlang.webconfig.controller.request.MenuConfigRequest;
import linhlang.webconfig.model.MenuConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuConfigMapper extends BaseMapper<MenuConfig, MenuConfigRequest>{

}
