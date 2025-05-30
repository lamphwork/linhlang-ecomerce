package linhlang.product.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class Provider implements Serializable {

    private String id;

    @NotBlank
    private String name;
}
