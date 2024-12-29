package linhlang.product.repository;

import linhlang.product.repository.entities.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariantRepository extends JpaRepository<VariantEntity, String>, JpaSpecificationExecutor<VariantEntity> {
}