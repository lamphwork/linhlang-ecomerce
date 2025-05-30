package linhlang.product.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import linhlang.product.model.SEOData;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SaveCollectionReq {

    @NotBlank
    @Length(max = 255)
    private String name;

    @Length(max = 5000)
    private String description;

    @Length(max = 255)
    private String image;

    @NotNull
    private Boolean autoSelect;

    @Valid
    private SEOData seo;

    @Length(max = 200)
    private String ui;

}
