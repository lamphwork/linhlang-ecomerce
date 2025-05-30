package linhlang.webconfig.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.webconfig.controller.request.BlogRequest;
import linhlang.webconfig.controller.request.BlogSearch;
import linhlang.webconfig.model.Blog;
import linhlang.webconfig.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menu/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody BlogRequest request) {
        return ResponseUtils.created(blogService.create(request).toString());
    }

    @PostMapping("/update")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> update(@Valid @RequestBody BlogRequest request) {
        return ResponseUtils.created(blogService.update(request).toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Blog>> getBlogById(@PathVariable String id) {
        return ResponseUtils.success(blogService.getById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageData<Blog>>> searchPageContent(@RequestBody BlogSearch request) {
        return ResponseUtils.success(blogService.search(request));
    }
}
