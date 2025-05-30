package linhlang.product.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class Collection implements Serializable {

    private String id;

    @NotBlank
    private String name;

    @Length(max = 5000)
    private String description;

    @Length(max = 255)
    private String image;

    @NotNull
    private Boolean autoSelect;

    private SEOData seo;

    private String ui;
}
