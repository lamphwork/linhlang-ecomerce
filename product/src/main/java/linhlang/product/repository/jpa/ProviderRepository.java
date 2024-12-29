package linhlang.product.repository.jpa;

import linhlang.product.repository.entities.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<ProviderEntity, String> {
}