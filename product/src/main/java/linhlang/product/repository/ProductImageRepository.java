package linhlang.product.repository;

import linhlang.product.model.Image;
import linhlang.product.repository.entities.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, String>, JpaSpecificationExecutor<ProductImageEntity> {

    List<ProductImageEntity> findAllByProductId(String productId);
}