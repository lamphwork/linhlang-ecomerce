package linhlang.auth.service;

import linhlang.auth.controller.request.CreateAccountReq;
import linhlang.auth.model.Account;

public interface AccountService {

    Account create(CreateAccountReq req);
}
