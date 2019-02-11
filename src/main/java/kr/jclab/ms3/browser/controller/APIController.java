package kr.jclab.ms3.browser.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import kr.jclab.cloud.ms3.client.MS3ClientBuilder;
import kr.jclab.ms3.browser.controller.dto.ObjectSummaryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/")
public class APIController {
    @RequestMapping(path = "/buckets/list")
    public ResponseEntity<Map> bucketsGetList() {
        Map<String, Object> responseBody = new HashMap<>();
        AmazonS3 ms3Client = MS3ClientBuilder.defaultClient();
        List<Bucket> bucketList = ms3Client.listBuckets();

        responseBody.put("code", 200);
        responseBody.put("list", bucketList);

        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

    @RequestMapping(path = "/buckets/create/{bucketName}")
    public ResponseEntity<Bucket> objectUpload(@PathVariable("bucketName") String bucketName) throws IOException {
        AmazonS3 ms3Client = MS3ClientBuilder.defaultClient();
        Bucket bucket = ms3Client.createBucket(bucketName);
        if(bucket == null) {
            return new ResponseEntity(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity(bucket, HttpStatus.OK);
    }

    @RequestMapping(path = "/bucket/{bucketName}/list")
    public ResponseEntity<Map> bucketGetFileList(@PathVariable("bucketName") String bucketName) {
        Map<String, Object> responseBody = new HashMap<>();
        AmazonS3 ms3Client = MS3ClientBuilder.defaultClient();
        ObjectListing objectListing = ms3Client.listObjects(bucketName);
        List<ObjectSummaryDTO> responseList = new ArrayList<>();
        for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            ObjectSummaryDTO dto = new ObjectSummaryDTO();
            dto.bucketName = objectSummary.getBucketName();
            dto.key = objectSummary.getKey();
            dto.lastModified = objectSummary.getLastModified().getTime();
            dto.size = objectSummary.getSize();
            responseList.add(dto);
        }
        responseBody.put("list", responseList);
        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

    @RequestMapping(path = "/bucket/{bucketName}/object_metadata/{key:.+}")
    public ResponseEntity<Map> objectGetMetadata(@PathVariable("bucketName") String bucketName, @PathVariable("key") String key) {
        Map<String, Object> responseBody = new HashMap<>();
        AmazonS3 ms3Client = MS3ClientBuilder.defaultClient();
        ObjectMetadata objectMetadata = ms3Client.getObjectMetadata(bucketName, key);
        responseBody.put("metadata", objectMetadata);
        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

    @RequestMapping(path = "/bucket/{bucketName}/object_upload/{key:.+}")
    public ResponseEntity<Map> objectUpload(@PathVariable("bucketName") String bucketName, @PathVariable("key") String key, @RequestPart("file") MultipartFile file) throws IOException {
        AmazonS3 ms3Client = MS3ClientBuilder.defaultClient();
        ms3Client.putObject(bucketName, key, file.getInputStream(), null);
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
