package linhlang.product.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product", indexes = {
        @Index(name = "idx_product_price", columnList = "price"),
        @Index(name = "idx_product_provider", columnList = "provider_id"),
        @Index(name = "idx_product_category", columnList = "category_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 300)
    private String image;

    @Lob
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "compare_price", precision = 15, scale = 2)
    private BigDecimal comparePrice;

    @Column(name = "provider_id", nullable = false, length = 100)
    private String providerId;

    @Column(name = "category_id", nullable = false, length = 100)
    private String categoryId;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}