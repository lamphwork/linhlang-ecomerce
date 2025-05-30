package linhlang.product.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property implements Serializable {

    private String attribute;

    @NotBlank
    private String attributeName;

    @NotBlank
    private String value;
}
