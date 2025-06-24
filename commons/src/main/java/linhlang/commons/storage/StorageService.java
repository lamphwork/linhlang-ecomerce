package linhlang.commons.storage;

import io.minio.*;
import io.minio.errors.*;
import linhlang.commons.utils.UrlUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class StorageService {

    public final String PUBLIC_BUCKET = "public";
    private final MinioClient minioClient;
    private final StorageProperties properties;

    public void createPrivateBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            return;
        }

        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
        minioClient.makeBucket(makeBucketArgs);

        String readOnlyPolicy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Action": ["s3:GetObject"],
                      "Effect": "Allow",
                      "Principal": "*",
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }
                """.formatted(bucketName);
        SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(readOnlyPolicy)
                .build();
        minioClient.setBucketPolicy(setBucketPolicyArgs);
    }

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
