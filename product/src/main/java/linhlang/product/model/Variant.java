package linhlang.product.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class Variant implements Serializable {

    private String id;
    private String title;
    private String productId;
    private String image;
    private BigDecimal price;
    private String sku;
    private String barCode;
    private Boolean active;
    private Set<Property> properties = new HashSet<>();
}
