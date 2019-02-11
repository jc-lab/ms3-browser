package kr.jclab.ms3.browser.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import kr.jclab.cloud.ms3.client.MS3ClientBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/")
public class ObjectDataController {
    @RequestMapping(path = "/bucket/{bucketName}/object_data/{key:.+}", produces = "application/octet-stream")
    public ResponseEntity<InputStreamResource> objectGetData(@PathVariable("bucketName") String bucket, @PathVariable("key") String key) {
        AmazonS3 ms3Client = MS3ClientBuilder.defaultClient();
        S3Object s3Object = ms3Client.getObject(bucket, key);
        ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
        HttpHeaders httpHeaders = new HttpHeaders();
        if(objectMetadata != null) {
            String contentType = objectMetadata.getContentType();
            String contentEncoding = objectMetadata.getContentEncoding();
            if(contentType != null)
                httpHeaders.set("Content-Type", contentType);
            if(contentEncoding != null)
                httpHeaders.set("Content-Encoding", contentEncoding);
        }
        return new ResponseEntity<>(new InputStreamResource(s3Object.getObjectContent()), httpHeaders, HttpStatus.OK);
    }
}
