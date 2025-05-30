package linhlang.product.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import linhlang.product.model.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProductSaveReq {

    @NotBlank
    @Length(max = 255)
    private String name;

    @NotNull
    @Valid
    private Provider provider;

    @NotNull
    @Valid
    private Category category;

    @Length(max = 5000)
    private String description;

    @Length(max = 255)
    private String quote;

    @Valid
    private Set<Image> images = new HashSet<>();

    @NotNull
    @Min(value = 0)
    private BigDecimal price;

    @Min(value = 0)
    private BigDecimal comparePrice;

    @Valid
    private SEOData seo;

    @Length(max = 100)
    private String size;

    @Length(max = 100)
    private String weight;

    @Valid
    private Set<Collection> collections = new HashSet<>();

    @Valid
    private Set<SaveVariantReq> variants = new HashSet<>();
}
