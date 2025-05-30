package linhlang.webconfig.service;

import linhlang.commons.model.PageData;
import linhlang.webconfig.controller.request.MenuConfigRequest;
import linhlang.webconfig.controller.request.MenuConfigSearch;
import linhlang.webconfig.model.MenuConfig;


public interface MenuConfigService {
    MenuConfigRequest createMenuConfig(MenuConfigRequest menu);

    MenuConfigRequest updateMenuConfig(MenuConfigRequest menu);

    PageData<MenuConfig> getParentMenu(MenuConfigSearch request);

    MenuConfigRequest getMenuWithChildren(String parentId);
}
