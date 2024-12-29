package linhlang.product.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_collection", uniqueConstraints = {
        @UniqueConstraint(name = "idx_product_collection_uq", columnNames = {"product_id", "collection_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCollectionEntity {

    @Id
    private String id;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(name = "collection_id", nullable = false, length = 100)
    private String collectionId;
}