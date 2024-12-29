package linhlang.product.repository;

import linhlang.product.repository.entities.ProductCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductCollectionRepository extends JpaRepository<ProductCollectionEntity, String>, JpaSpecificationExecutor<ProductCollectionEntity> {
}