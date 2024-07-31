package com.bsp.demo.service;

import com.bsp.demo.pojo.CreditScoreRequest;
import com.bsp.model.FinancialServices200Response;
import com.detecttext.api.DetectTextApi;
import com.detecttext.invoker.ApiClient;
import com.detecttext.model.DetectText200Response;
import com.detecttext.model.DetectTextRequest;
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

    private final DetectTextApi detectTextApi;
    private final RestTemplate restTemplate;

    @Autowired
    public DetectTextService(ApiClient apiClient, RestTemplate restTemplate) {
        this.detectTextApi = new DetectTextApi(apiClient);
        this.restTemplate = restTemplate;
    }

//    public CreditScore200Response getCreditScore(CreditScoreRequest creditScoreRequest) {
//        CreditScore200Response creditScore200Response = creditScoreApi.creditScore(creditScoreRequest);
//        return creditScore200Response;
//    }

    public DetectText200Response postDetectText(DetectTextRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<DetectTextRequest> entity = new HttpEntity<>(request, headers);

        String url = "https://w5ng4dt8ai.execute-api.us-east-1.amazonaws.com/hackathonDevStage/detectText";
        System.out.println("Request: " + entity);
        DetectText200Response response = restTemplate.exchange(url, HttpMethod.POST, entity, DetectText200Response.class).getBody();
        System.out.println("Response: " + response);
        return response;
    }

}

