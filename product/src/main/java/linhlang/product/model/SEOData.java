package linhlang.product.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class SEOData implements Serializable {

//    @NotBlank
    @Length(max = 255)
    private String title;

    @Length(max = 500)
    private String description;

    @Length(max = 255)
    private String link;
}
