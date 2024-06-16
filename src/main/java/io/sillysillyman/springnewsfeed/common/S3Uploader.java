package io.sillysillyman.springnewsfeed.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucketName = "springnewsfeed"; // 버킷 이름을 설정하세요

    @Autowired
    public S3Uploader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String upload(MultipartFile file, String dirName) throws IOException {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
