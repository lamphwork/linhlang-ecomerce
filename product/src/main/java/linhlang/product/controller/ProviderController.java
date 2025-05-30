package linhlang.product.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.ApiResponse;
import linhlang.commons.model.PageData;
import linhlang.commons.utils.ResponseUtils;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveProviderReq;
import linhlang.product.model.Provider;
import linhlang.product.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageData<Provider>>> query(CommonSearchReq req) {
        return ResponseUtils.success(providerService.query(req));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid SaveProviderReq req) {
        return ResponseUtils.created(providerService.create(req).getId());
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Provider>> update(@PathVariable String id, @RequestBody @Valid SaveProviderReq req) {
        return ResponseUtils.success(providerService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        providerService.delete(id);
        return ResponseUtils.success(id);
    }

}
