package linhlang.product.repository.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provider")
public class ProviderEntity {

    @Id
    @Column(length = 100, nullable = false)
    private String id;

    @Column(length = 255, nullable = false)
    private String name;
}
