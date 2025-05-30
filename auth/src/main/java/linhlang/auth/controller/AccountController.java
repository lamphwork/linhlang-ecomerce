package linhlang.auth.controller;

import jakarta.validation.Valid;
import linhlang.auth.controller.request.CreateAccountReq;
import linhlang.auth.controller.response.TokenRes;
import linhlang.auth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> create(@Valid @RequestBody CreateAccountReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(req).getId());
    }
}
