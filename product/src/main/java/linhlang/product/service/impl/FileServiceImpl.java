package linhlang.product.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import linhlang.commons.storage.StorageService;
import linhlang.product.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final StorageService storageService;

    @Override
    public void upload(String bucket, String object, MultipartFile file) {
        try {
            storageService.getMinioClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
