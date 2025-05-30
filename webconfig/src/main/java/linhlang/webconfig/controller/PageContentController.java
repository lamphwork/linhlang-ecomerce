package linhlang.webconfig.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.webconfig.controller.request.PageContentRequest;
import linhlang.webconfig.controller.request.PageContentSearch;
import linhlang.webconfig.model.PageContent;
import linhlang.webconfig.service.PageContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menu/page/content")
@RequiredArgsConstructor
public class PageContentController {
    private final PageContentService pageContentService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody PageContentRequest request) {
        return ResponseUtils.created(pageContentService.create(request).toString());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageData<PageContent>>> searchPageContent(@RequestBody PageContentSearch request) {
        return ResponseUtils.success(pageContentService.searchPageContent(request));
    }

    @PostMapping("/update")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> update(@Valid @RequestBody PageContentRequest request) {
        return ResponseUtils.created(pageContentService.update(request).toString());
    }
}
