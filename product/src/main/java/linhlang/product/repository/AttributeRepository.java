package linhlang.product.repository;

import linhlang.product.repository.entities.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttributeRepository extends JpaRepository<AttributeEntity, String>, JpaSpecificationExecutor<AttributeEntity> {
}