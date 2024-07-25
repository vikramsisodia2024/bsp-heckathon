package com.bsp.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreditScoreRequest {

    //    @JsonProperty("total_income")
    private String totalIncome;

    //    @JsonProperty("total_expenses")
    private String totalExpenses;

    //    @JsonProperty("credit_line")
    private String creditLine;

    //    @JsonProperty("existing_balance")
    private String existingBalance;

    //    @JsonProperty("total_credits")
    private String totalCredits;

    //    @JsonProperty("total_debits")
    private String totalDebits;

    //    @JsonProperty("total_debt_obligations")
    private String totalDebtObligations;

}
