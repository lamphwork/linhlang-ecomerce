package linhlang.product.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.controller.request.QueryProductReq;
import linhlang.product.model.Image;
import linhlang.product.model.Product;
import linhlang.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody ProductSaveReq request) {
        return ResponseUtils.created(productService.createProduct(request).getId());
    }

    @PutMapping("/{productId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable String productId,
                                         @Valid @RequestBody ProductSaveReq product) {
        return ResponseUtils.success(productService.updateProduct(productId, product).getId());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> detail(@PathVariable String productId) {
        return ResponseUtils.success(productService.detail(productId));
    }

    @DeleteMapping("/{productId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Product>> delete(@PathVariable String productId) {
        return ResponseUtils.success(productService.delete(productId));
    }

    @PostMapping(value = "/{productId}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<List<Image>>> uploadImages(
            @PathVariable String productId,
            @RequestPart MultipartFile[] files) throws IOException {
        return ResponseUtils.success(productService.uploadImages(productId, files));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageData<Product>>> query(QueryProductReq req) {
        return ResponseUtils.success(productService.query(req));
    }

}
