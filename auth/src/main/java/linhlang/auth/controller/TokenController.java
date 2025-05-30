package linhlang.auth.controller;

import jakarta.validation.Valid;
import linhlang.auth.controller.request.LoginReq;
import linhlang.auth.controller.request.RefreshTokenReq;
import linhlang.auth.controller.response.TokenRes;
import linhlang.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenRes> login(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(tokenService.login(loginReq));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRes> refresh(@Valid @RequestBody RefreshTokenReq req) {
        return ResponseEntity.ok(tokenService.refresh(req));
    }
}
