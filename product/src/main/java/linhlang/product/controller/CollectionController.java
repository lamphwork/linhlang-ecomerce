package linhlang.product.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveCollectionReq;
import linhlang.product.model.Collection;
import linhlang.product.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageData<Collection>>> query(CommonSearchReq req) {
        return ResponseUtils.success(collectionService.search(req));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid SaveCollectionReq req) {
        return ResponseUtils.created(collectionService.create(req).getId());
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable String id, @RequestBody @Valid SaveCollectionReq req) {
        return ResponseUtils.success(collectionService.update(id, req).getId());
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Collection>> detail(@PathVariable String id) {
        return ResponseUtils.success(collectionService.detail(id));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Collection>> delete(@PathVariable String id) {
        return ResponseUtils.success(collectionService.delete(id));
    }
}
