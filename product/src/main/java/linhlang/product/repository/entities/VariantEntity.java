package linhlang.product.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "variant", indexes = {
        @Index(name = "idx_variant_price", columnList = "price"),
        @Index(name = "idx_variant_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 300)
    private String image;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "compare_price", precision = 15, scale = 2)
    private BigDecimal comparePrice;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}