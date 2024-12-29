package linhlang.product.service;

import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductSaveReq product);

    Product updateProduct(String productId, ProductSaveReq product);

    Product detail(String productId);

    List<Product> query();

}
