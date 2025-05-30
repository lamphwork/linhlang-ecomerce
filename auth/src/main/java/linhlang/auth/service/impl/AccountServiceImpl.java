package linhlang.auth.service.impl;

import jakarta.annotation.PostConstruct;
import linhlang.auth.constants.Errors;
import linhlang.auth.controller.request.CreateAccountReq;
import linhlang.auth.model.Account;
import linhlang.auth.repository.AccountRepository;
import linhlang.auth.service.AccountService;
import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.security.Roles;
import linhlang.commons.security.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final SecurityProperties properties;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void initAccount() {
        if (!accountRepository.isExistedUsername(properties.getAdminUser())) {
            log.info("init admin user");
            CreateAccountReq createAccountReq = new CreateAccountReq(
                    properties.getAdminUser(),
                    properties.getAdminPassword(),
                    true
            );
            create(createAccountReq);
        }
    }

    @Override
    public Account create(CreateAccountReq req) {
        String username = StringUtils.lowerCase(req.getUsername()).trim();
        String password = StringUtils.trim(req.getPassword());

        if (accountRepository.isExistedUsername(username)) {
            throw new BusinessException(Errors.ACCOUNT_EXISTED);
        }

        Account account = Account.newAccount(
                username,
                passwordEncoder.encode(password),
                req.getIsAdmin() ? Roles.ADMIN : Roles.USER
        );
        accountRepository.save(account);
        return account;
    }
}
