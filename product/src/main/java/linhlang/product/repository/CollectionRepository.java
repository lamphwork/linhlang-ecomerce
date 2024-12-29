package linhlang.product.repository;

import linhlang.product.repository.entities.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<CollectionEntity, String>, JpaSpecificationExecutor<CollectionEntity> {

    @Query("select c from CollectionEntity c inner join ProductCollectionEntity pc on c.id = pc.collectionId where pc.productId = :productId")
    List<CollectionEntity> findAllByProductId(String productId);
}