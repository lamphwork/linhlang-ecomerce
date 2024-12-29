package linhlang.product.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Variant {

    private String id;
    private String name;
    private String productId;
    private String image;
    private Set<Property> properties = new HashSet<>();
}
