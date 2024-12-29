package linhlang.product.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.utils.ResponseUtils;
import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.model.Product;
import linhlang.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody ProductSaveReq request) {
        return ResponseUtils.created(productService.createProduct(request).getId());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable String productId,
                                         @Valid @RequestBody ProductSaveReq product) {
        return ResponseUtils.success(productService.updateProduct(productId, product).getId());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> detail(@PathVariable String productId) {
        return ResponseUtils.success(productService.detail(productId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> query() {
        return ResponseUtils.success(productService.query());
    }

}
