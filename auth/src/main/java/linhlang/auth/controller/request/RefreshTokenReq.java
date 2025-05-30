package linhlang.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenReq {

    @NotBlank
    private String refreshToken;
}
