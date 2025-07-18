package linhlang.commons.storage;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import linhlang.commons.utils.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    public final String PUBLIC_BUCKET = "public";
    private final MinioClient minioClient;
    private final StorageProperties properties;

    public List<String> uploadFile(String folder, MultipartFile[] files) {
        List<String> uploadedUrls = new LinkedList<>();
        for (MultipartFile file : files) {
            try {
                String objectName = UrlUtils.joinPath(folder, UUID.randomUUID().toString());
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(PUBLIC_BUCKET)
                                .object(objectName)
                                .contentType(file.getContentType())
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .build()
                );
                uploadedUrls.add(UrlUtils.appendDomain(UrlUtils.joinPath(PUBLIC_BUCKET, objectName), properties.getPublicUrl()));
            } catch (Exception e) {
                log.error("Upload file failure: [{}]", file.getOriginalFilename(), e);
            }
        }

        return uploadedUrls;
    }
}
