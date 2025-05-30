package linhlang.product.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveCategoryReq;
import linhlang.product.model.Category;
import linhlang.product.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageData<Category>>> query(CommonSearchReq req) {
        return ResponseUtils.success(categoryService.query(req));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid SaveCategoryReq req) {
        return ResponseUtils.created(categoryService.create(req).getId());
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Category>> update(@PathVariable String id, @RequestBody @Valid SaveCategoryReq req) {
        return ResponseUtils.success(categoryService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseUtils.success(id);
    }

}
