package linhlang.auth.service;

import linhlang.auth.controller.request.LoginReq;
import linhlang.auth.controller.request.RefreshTokenReq;
import linhlang.auth.controller.response.TokenRes;

public interface TokenService {

    TokenRes login(LoginReq req);

    TokenRes refresh(RefreshTokenReq req);
}
