package linhlang.auth.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.Keys;
import linhlang.auth.constants.Errors;
import linhlang.auth.controller.request.LoginReq;
import linhlang.auth.controller.request.RefreshTokenReq;
import linhlang.auth.controller.response.TokenRes;
import linhlang.auth.model.Account;
import linhlang.auth.repository.AccountRepository;
import linhlang.auth.service.TokenService;
import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.security.SecurityProperties;
import linhlang.commons.security.TokenUser;
import linhlang.commons.security.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenUtils tokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Override
    public TokenRes login(LoginReq req) {
        String username = StringUtils.lowerCase(req.getUsername()).trim();
        String password = StringUtils.trim(req.getPassword());

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new BusinessException(Errors.CREDENTIAL_INVALID);
        }

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new BusinessException(Errors.CREDENTIAL_INVALID);
        }

        return getTokenRes(account);
    }

    @NotNull
    private TokenRes getTokenRes(Account account) {
        TokenUser tokenUser = new TokenUser(
                account.getId(),
                account.getUsername(),
                account.getRoles().stream().toList()
        );

        String token = tokenUtils.generateToken(tokenUser, 5 * 60);
        tokenUser.setRoles(Collections.emptyList());
        String refreshToken = tokenUtils.generateToken(tokenUser, 24 * 60 * 60);
        return new TokenRes(
                token,
                refreshToken
        );
    }

    @Override
    public TokenRes refresh(RefreshTokenReq req) {
        TokenUser tokenUser = tokenUtils.parseToken(req.getRefreshToken());
        if (tokenUser == null) {
            throw new BusinessException(Errors.TOKEN_INVALID);
        }

        Account account = accountRepository.findByUsername(tokenUser.getUsername());
        if (account == null) {
            throw new BusinessException(Errors.ACCOUNT_NOTFOUND);
        }

        return getTokenRes(account);
    }
}
