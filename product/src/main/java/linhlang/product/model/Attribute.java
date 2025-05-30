package linhlang.product.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Attribute implements Serializable {

    private String id;
    private String name;
    private Boolean systemAttr;
}
