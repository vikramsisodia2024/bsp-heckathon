package com.bsp.demo.controller;

import com.bsp.api.FinancialServicesApi;
import com.bsp.demo.pojo.CreditScoreRequest;
import com.bsp.demo.service.AWSService;
import com.bsp.demo.service.CreditService;
import com.bsp.model.FinancialServices200Response;
import com.bsp.model.FinancialServicesDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FinancialServiceController implements FinancialServicesApi {
    private static final Logger logger = LoggerFactory.getLogger(FinancialServiceController.class);


    @Autowired
    private CreditService creditService;

    @Autowired
    private AWSService awsService;



    @Override
    public ResponseEntity<FinancialServices200Response> financialServices(String xFapiFinancialId, String xFapiInteractionId, String authorization) {
        FinancialServicesDetails financialServicesDetails = creditService.getAllAccountForUser(xFapiFinancialId,xFapiInteractionId, authorization);

        CreditScoreRequest creditScoreRequest = new CreditScoreRequest();
        creditScoreRequest.setTotalCredits(financialServicesDetails.getTotalCredits());
        creditScoreRequest.setCreditLine(financialServicesDetails.getCreditLine());
        creditScoreRequest.setTotalExpenses(financialServicesDetails.getTotalExpense());
        creditScoreRequest.setTotalDebtObligations(financialServicesDetails.getTotalDebtObligations());
        creditScoreRequest.setTotalIncome(financialServicesDetails.getTotalIncome());
        creditScoreRequest.setExistingBalance(financialServicesDetails.getExistingBalance());
        creditScoreRequest.setTotalDebtObligations(financialServicesDetails.getTotalDebtObligations());

        FinancialServices200Response financialServices200Response = awsService.postExample(creditScoreRequest);
        return new ResponseEntity<>(financialServices200Response, HttpStatus.OK);
    }
}

