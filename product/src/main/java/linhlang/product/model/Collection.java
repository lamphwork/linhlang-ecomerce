package linhlang.product.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Collection {

    private String id;

    @NotBlank
    private String name;

    @Valid
    private Image image;
}
