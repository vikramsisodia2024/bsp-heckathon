package com.bsp.demo.service;


import com.aws.api.CreditScoreApi;
import com.aws.invoker.ApiClient;
import com.bsp.demo.model.CreditScoreRequest;
import com.bsp.model.FinancialServices200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class AWSService {

    private final CreditScoreApi creditScoreApi;
    private final RestTemplate restTemplate;

    @Autowired
    public AWSService(ApiClient apiClient, RestTemplate restTemplate) {
        this.creditScoreApi = new CreditScoreApi(apiClient);
        this.restTemplate = restTemplate;
    }

//    public CreditScore200Response getCreditScore(CreditScoreRequest creditScoreRequest) {
//        CreditScore200Response creditScore200Response = creditScoreApi.creditScore(creditScoreRequest);
//        return creditScore200Response;
//    }

    public FinancialServices200Response postExample(CreditScoreRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CreditScoreRequest> entity = new HttpEntity<>(request, headers);

        String url = "https://ckwl5eo409.execute-api.us-east-1.amazonaws.com/hackathonDevStage/calculate";
        System.out.println("Request: " + entity);
        FinancialServices200Response response = restTemplate.exchange(url, HttpMethod.POST, entity, FinancialServices200Response.class).getBody();
        System.out.println("Response: " + response);
        return response;
    }

}

