package linhlang.product.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "properties", indexes = {
        @Index(name = "idx_properties_variant", columnList = "variant_id"),
        @Index(name = "idx_properties_attr", columnList = "attribute"),
        @Index(name = "idx_properties_value", columnList = "value")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyEntity {

    @Id
    private String id;

    @Column(name = "variant_id", nullable = false, length = 100)
    private String variantId;

    @Column(nullable = false, length = 100)
    private String attribute;

    @Column(nullable = false, length = 255)
    private String value;
}