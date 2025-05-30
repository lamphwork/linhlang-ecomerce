package linhlang.product.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import linhlang.product.model.Property;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class SaveVariantReq {

    private String id;

    private String name;

    @Min(0)
    private BigDecimal price;

    @Length(max = 200)
    private String sku;

    @Length(max = 200)
    private String barCode;

    @NotNull
    private Boolean active;

    @Valid
    private Set<Property> properties = new LinkedHashSet<>();
}
