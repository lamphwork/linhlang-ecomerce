package linhlang.webconfig.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.webconfig.controller.request.SaveBannerRequest;
import linhlang.webconfig.model.Banner;
import linhlang.webconfig.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> create(@Valid @RequestBody SaveBannerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerService.createBanner(req));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> update(@PathVariable String id, @Valid @RequestBody SaveBannerRequest req) {
        return ResponseEntity.ok(bannerService.updateBanner(id, req));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(bannerService.deleteBanner(id));
    }

    @GetMapping
    ResponseEntity<PageData<Banner>> query(QueryRequest request) {
        return ResponseEntity.ok(bannerService.query(request));
    }
}
