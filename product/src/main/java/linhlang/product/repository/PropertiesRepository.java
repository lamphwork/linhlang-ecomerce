package linhlang.product.repository;

import linhlang.product.repository.entities.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertiesRepository extends JpaRepository<PropertyEntity, String>, JpaSpecificationExecutor<PropertyEntity> {
}