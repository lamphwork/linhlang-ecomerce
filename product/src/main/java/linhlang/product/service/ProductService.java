package linhlang.product.service;

import linhlang.commons.model.PageData;
import linhlang.product.controller.request.ProductSaveReq;
import linhlang.product.controller.request.QueryProductReq;
import linhlang.product.model.Image;
import linhlang.product.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product createProduct(ProductSaveReq product);

    Product updateProduct(String productId, ProductSaveReq product);

    Product detail(String productId);

    Product delete(String productId);

    PageData<Product> query(QueryProductReq request);

    List<Image> uploadImages(String product, MultipartFile[] files) throws IOException;

}
