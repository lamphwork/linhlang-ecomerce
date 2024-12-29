package linhlang.product.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Category {

    private String id;

    @NotBlank
    private String name;

}
