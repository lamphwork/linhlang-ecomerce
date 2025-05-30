package linhlang.webconfig.service.impl;

import linhlang.commons.model.PageData;
import linhlang.webconfig.constants.Constants;
import linhlang.webconfig.controller.request.MenuConfigRequest;
import linhlang.webconfig.controller.request.MenuConfigSearch;
import linhlang.webconfig.model.MenuConfig;
import linhlang.webconfig.repository.MenuConfigRepository;
import linhlang.webconfig.service.MenuConfigService;
import linhlang.webconfig.service.mapper.MenuConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuConfigServiceImpl implements MenuConfigService {

    private final MenuConfigMapper menuConfigMapper;
    private final MenuConfigRepository menuConfigRepository;



    @Override
    public MenuConfigRequest createMenuConfig(MenuConfigRequest request) {
        MenuConfig parentMenu = convertToEntity(request);
        menuConfigRepository.save(parentMenu);

        if (request.getMenuChild() != null) {
            for (MenuConfigRequest childRequest : request.getMenuChild()) {
                childRequest.setParentId(parentMenu.getId());
                createMenuConfig(childRequest);
            }
        }
        return request;
    }

    private MenuConfig convertToEntity(MenuConfigRequest request) {
        return MenuConfig.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .parentId(request.getParentId())
                .pathRoot(request.getPathRoot())
                .pathType(request.getPathType())
                .pathLink(request.getPathLink())
                .tag(request.getTag())
                .status(1L)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }


    @Override
    public MenuConfigRequest updateMenuConfig(MenuConfigRequest request) {
        MenuConfig menuParent = menuConfigRepository.getById(request.getId());
        if (menuParent == null) {
            throw new IllegalArgumentException("Menu cha không tồn tại với ID: " + request.getId());
        }

        // Cập nhật thông tin menu cha
        menuParent.setName(request.getName());
        menuParent.setPathRoot(request.getPathRoot());
        menuParent.setPathType(request.getPathType());
        menuParent.setPathLink(request.getPathLink());
        menuParent.setTag(request.getTag());
        menuParent.setStatus(request.getStatus());
        menuParent.setUpdateTime(LocalDateTime.now());
        menuConfigRepository.save(menuParent);

        // Thu thập tất cả các ID từ request
        Set<String> allRequestIds = new HashSet<>();
        collectMenuIds(request.getMenuChild(), allRequestIds);

        // Tìm và xử lý tất cả menu con trong database
        processChildMenus(request.getId(), allRequestIds, request.getMenuChild());

        return request;
    }

    private void processChildMenus(String parentId, Set<String> allRequestIds, Set<MenuConfigRequest> requestMenuChildren) {
        // Lấy danh sách menu con từ database
        List<MenuConfig> existingMenuConfigs = menuConfigRepository.getMenusByParentId(parentId);

        // So sánh và cập nhật trạng thái của các menu con không có trong request
        for (MenuConfig existingMenu : existingMenuConfigs) {
            if (!allRequestIds.contains(existingMenu.getId())) {
                existingMenu.setStatus(Constants.STATUS_INACTIIVE); // Cập nhật trạng thái về 0
                menuConfigRepository.save(existingMenu);
            }
        }

        // Xử lý từng menu con từ request
        for (MenuConfigRequest menuChildRequest : requestMenuChildren) {
            saveOrUpdateMenu(menuChildRequest, parentId);

            // Đệ quy xử lý các menu con cấp sâu hơn
            processChildMenus(menuChildRequest.getId(), allRequestIds, menuChildRequest.getMenuChild());
        }
    }

    private void saveOrUpdateMenu(MenuConfigRequest menuChildRequest, String parentId) {
        String menuId = (menuChildRequest.getId() != null) ? menuChildRequest.getId() : UUID.randomUUID().toString();

        // Tìm menu con trong database
        MenuConfig existingMenu = menuConfigRepository.getById(menuId);

        if (existingMenu != null) {
            // Cập nhật menu con đã tồn tại
            existingMenu.setName(menuChildRequest.getName());
            existingMenu.setPathRoot(menuChildRequest.getPathRoot());
            existingMenu.setPathType(menuChildRequest.getPathType());
            existingMenu.setPathLink(menuChildRequest.getPathLink());
            existingMenu.setTag(menuChildRequest.getTag());
            existingMenu.setUpdateTime(LocalDateTime.now());
            existingMenu.setParentId(parentId);
            existingMenu.setStatus(menuChildRequest.getStatus());
            menuConfigRepository.save(existingMenu);
        } else {
            // Thêm mới menu con
            MenuConfig newMenuConfig = new MenuConfig();
            newMenuConfig.setId(menuId);
            newMenuConfig.setName(menuChildRequest.getName());
            newMenuConfig.setPathRoot(menuChildRequest.getPathRoot());
            newMenuConfig.setPathType(menuChildRequest.getPathType());
            newMenuConfig.setPathLink(menuChildRequest.getPathLink());
            newMenuConfig.setTag(menuChildRequest.getTag());
            newMenuConfig.setParentId(parentId);
            newMenuConfig.setCreateTime(LocalDateTime.now());
            newMenuConfig.setStatus(1L); // 1 là trạng thái active
            menuConfigRepository.save(newMenuConfig);
        }

        // Cập nhật lại ID vào request để tránh mất dữ liệu khi đệ quy
        menuChildRequest.setId(menuId);
    }

    private void collectMenuIds(Set<MenuConfigRequest> menuChildren, Set<String> allRequestIds) {
        if (menuChildren != null) {
            for (MenuConfigRequest menuChild : menuChildren) {
                if (menuChild.getId() == null) {
                    menuChild.setId(UUID.randomUUID().toString()); // Tạo ID nếu chưa có
                }
                allRequestIds.add(menuChild.getId());
                collectMenuIds(menuChild.getMenuChild(), allRequestIds);
            }
        }
    }

    @Override
    public PageData<MenuConfig> getParentMenu(MenuConfigSearch request) {
        return menuConfigRepository.getParentMenu(request);
    }

    @Override
    public MenuConfigRequest getMenuWithChildren(String parentId) {
        MenuConfig parentMenu = menuConfigRepository.getById(parentId);
        if (parentMenu == null) {
            return null;
        }

        MenuConfigRequest parentMenuRequest = mapToRequest(parentMenu);
        parentMenuRequest.setMenuChild(getChildren(parentId));
        return parentMenuRequest;
    }

    private Set<MenuConfigRequest> getChildren(String parentId) {
        List<MenuConfig> childMenus = menuConfigRepository.getMenusByParentId(parentId);
        return childMenus.stream()
                .map(menu -> {
                    MenuConfigRequest childRequest = mapToRequest(menu);
                    childRequest.setMenuChild(getChildren(menu.getId()));
                    return childRequest;
                })
                .collect(Collectors.toSet());
    }

    private MenuConfigRequest mapToRequest(MenuConfig menuConfig) {
        return new MenuConfigRequest(
                menuConfig.getId(),
                menuConfig.getName(),
                menuConfig.getParentId(),
                menuConfig.getPathRoot(),
                menuConfig.getPathType(),
                menuConfig.getPathLink(),
                menuConfig.getTag(),
                menuConfig.getStatus(),
                new HashSet<>()
        );
    }

}
