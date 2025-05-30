package linhlang.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private String id;
    private String name;
    private String image;
    private String description;
    private String quote;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private Provider provider;
    private Category category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private SEOData seo;
    private String size;
    private String weight;
    private Set<Image> images = new HashSet<>();
    private Set<Collection> collections = new HashSet<>();
    private Set<Variant> variants = new HashSet<>();

    public String getImage() {
        if (images != null && !images.isEmpty()) {
            return images.stream().findFirst().get().getUrl();
        }
        return image;
    }
}
