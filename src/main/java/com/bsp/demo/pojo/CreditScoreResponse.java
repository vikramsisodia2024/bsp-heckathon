package com.bsp.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreditScoreResponse {

    //    @JsonProperty("credit_score")
    private double creditScore;

    //    @JsonProperty("loan_approval_status")
    private String loanApprovalStatus;

    //    @JsonProperty("loan_approval_amount")
    private double loanApprovalAmount;
}
