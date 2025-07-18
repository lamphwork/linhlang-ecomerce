package linhlang.product.controller.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SaveCategoryReq(
        @NotBlank @Length(max = 200) String name) {
}
