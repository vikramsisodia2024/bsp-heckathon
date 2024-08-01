package com.bsp.demo.service;

import com.detecttext.model.DetectTextRequest;
import com.detecttext.model.S3UploadPost200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class DetectTextService {


    private final RestTemplate restTemplate;

    @Autowired
    public DetectTextService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public S3UploadPost200Response postDetectText(DetectTextRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<DetectTextRequest> entity = new HttpEntity<>(request, headers);

        String url = "https://w5ng4dt8ai.execute-api.us-east-1.amazonaws.com/hackathonDevStage/detectText";
        System.out.println("Request: " + entity);
        S3UploadPost200Response response = restTemplate.exchange(url, HttpMethod.POST, entity, S3UploadPost200Response.class).getBody();
        System.out.println("Response: " + response);
        return response;
    }

}

