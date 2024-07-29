package com.bsp.demo.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RestController
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(FinancialServiceController.class);

    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public DocumentController(AmazonS3 amazonS3, @Value("${cloud.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @PostMapping("/loan/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        try {
            String key = userId + "/" + file.getOriginalFilename();
            amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), new ObjectMetadata()));
            //amazonS3.putObject(bucketName, "", file.getInputStream(), null);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/loan/fetch")
    public ResponseEntity<byte[]> fetchDocument(@RequestParam("userId") String userId, @RequestParam("fileName") String fileName) {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, userId + "/" + fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            byte[] content = IOUtils.toByteArray(inputStream);

            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            convertBytesToPdf(content, pdfOutputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "output.pdf");

            return ResponseEntity.ok().headers(headers).body(pdfOutputStream.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private void convertBytesToPdf(byte[] pdfBytes, ByteArrayOutputStream outputStream) throws IOException {
        outputStream.write(pdfBytes);
    }
}
