package linhlang.product.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Provider {

    private String id;

    @NotBlank
    private String name;
}
