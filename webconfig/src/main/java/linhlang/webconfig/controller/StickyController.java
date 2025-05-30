package linhlang.webconfig.controller;

import jakarta.validation.Valid;
import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.webconfig.controller.request.SaveStickyRequest;
import linhlang.webconfig.model.Sticky;
import linhlang.webconfig.service.StickyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sticky")
@RequiredArgsConstructor
public class StickyController {

    private final StickyService stickyService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> create(@Valid @RequestBody SaveStickyRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stickyService.create(req));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> update(@PathVariable String id, @Valid @RequestBody SaveStickyRequest req) {
        return ResponseEntity.ok(stickyService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(stickyService.delete(id));
    }

    @GetMapping
    ResponseEntity<PageData<Sticky>> query(QueryRequest request) {
        return ResponseEntity.ok(stickyService.query(request));
    }
}
