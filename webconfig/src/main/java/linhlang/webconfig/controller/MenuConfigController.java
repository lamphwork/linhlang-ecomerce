package linhlang.webconfig.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.webconfig.controller.request.MenuConfigRequest;
import linhlang.webconfig.controller.request.MenuConfigSearch;
import linhlang.webconfig.model.MenuConfig;
import linhlang.webconfig.service.MenuConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu/config")
@RequiredArgsConstructor
public class MenuConfigController {
    private final MenuConfigService menuConfigService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody MenuConfigRequest request) {
        return ResponseUtils.created(menuConfigService.createMenuConfig(request).toString());
    }

    @PostMapping("/update")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> update(@Valid @RequestBody MenuConfigRequest request) {
        return ResponseUtils.created(menuConfigService.updateMenuConfig(request).toString());
    }

    @GetMapping("/parentMenu")
    public ResponseEntity<ApiResponse<PageData<MenuConfig>>> getParentMenu(MenuConfigSearch request) {
        return ResponseUtils.success(menuConfigService.getParentMenu(request));
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<ApiResponse<MenuConfigRequest>> getMenuWithChildren(@PathVariable String parentId) {
        return ResponseUtils.success(menuConfigService.getMenuWithChildren(parentId));
    }
}
