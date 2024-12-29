package linhlang.product.repository.entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "product_image", indexes = {
        @Index(name = "idx_product_img", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 300)
    private String url;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(name = "image_order")
    private Integer imageOrder;
}