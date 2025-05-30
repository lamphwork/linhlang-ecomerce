package linhlang.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void upload(String bucket, String object, MultipartFile file);
}
