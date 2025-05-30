package linhlang.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginReq {

    @NotBlank
    @Length(max = 250)
    private String username;

    @NotBlank
    @Length(max = 100)
    private String password;
}
