package linhlang.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private Provider provider;
    private Category category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Set<Image> images = new HashSet<>();
    private Set<Collection> collections = new HashSet<>();

}
