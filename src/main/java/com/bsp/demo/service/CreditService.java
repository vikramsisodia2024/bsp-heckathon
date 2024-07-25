package com.bsp.demo.service;

import ch.qos.logback.core.util.StringUtil;

import com.bsp.model.FinancialServicesDetails;
import com.credit.api.AccountsApi;
import com.credit.api.BalancesApi;
import com.credit.api.DirectDebitsApi;
import com.credit.api.ScheduledPaymentsApi;
import com.credit.invoker.ApiClient;
import com.credit.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CreditService {

    private final BalancesApi balancesApi;

    private final AccountsApi accountsApi;

    private final DirectDebitsApi directDebitsApi;

    private final ScheduledPaymentsApi scheduledPaymentsApi;

    @Autowired
    public CreditService(ApiClient apiClient) {
        this.balancesApi = new BalancesApi(apiClient);
        this.accountsApi = new AccountsApi(apiClient);
        this.directDebitsApi = new DirectDebitsApi(apiClient);
        this.scheduledPaymentsApi = new ScheduledPaymentsApi(apiClient);
    }

    public FinancialServicesDetails getAllAccountForUser(String xFapiFinancialId, String xFapiInteractionId, String authorization) {
        FinancialServicesDetails financialServicesDetails = new FinancialServicesDetails();
        ArrayList<String> accountList = new ArrayList<>();
        accountsApi.getApiClient();

        OBReadAccount6 obReadAccount6 = accountsApi.getAccounts(authorization, xFapiFinancialId, xFapiInteractionId);

        // invoke account balance api
        financialServicesDetails = getAccountBalance(authorization, xFapiFinancialId, xFapiInteractionId, obReadAccount6, financialServicesDetails );

        // invoke direct debit api
        double directDebtAmount = fetchDirectDebitAmount(authorization, xFapiFinancialId, xFapiInteractionId, obReadAccount6.getData().getAccount());

        // invoke scheduled payment api
        double scheduledPaymentAmount = fetchScheduledPaymentAmount( authorization, xFapiFinancialId, xFapiInteractionId , obReadAccount6.getData().getAccount());

        double totalDebtAmount = 0.0;
        totalDebtAmount = directDebtAmount + scheduledPaymentAmount;
        financialServicesDetails.setTotalDebtObligations(String.valueOf(totalDebtAmount));

        return financialServicesDetails;

    }
        public FinancialServicesDetails getAccountBalance(String authorization, String xFapiFinancialId , String xFapiInteractionId, OBReadAccount6 obReadAccount6, FinancialServicesDetails financialServicesDetails) {
        List<OBAccount6> obAccount6s = obReadAccount6.getData().getAccount();
        for (OBAccount6 obAccount6 : obAccount6s) {

            // Getting Account Balance for Account ID
            OBReadBalance1 obReadBalance = balancesApi.getAccountsAccountIdBalances(obAccount6.getAccountId(),
                    authorization,
                    xFapiFinancialId,
                    xFapiInteractionId);

            List<OBReadBalance1DataBalanceInner> balanceList = obReadBalance.getData().getBalance();

            for (OBReadBalance1DataBalanceInner obReadBalance1DataBalanceInner : balanceList) {
                // Account ID
                String accountID = obReadBalance1DataBalanceInner.getAccountId();

                // Credit Line
                List<OBReadBalance1DataBalanceInnerCreditLineInner> obReadBalance1DataBalanceInnerCreditLineInnersList = obReadBalance1DataBalanceInner.getCreditLine();
                for (OBReadBalance1DataBalanceInnerCreditLineInner obReadBalance1DataBalanceInnerCreditLineInner : obReadBalance1DataBalanceInnerCreditLineInnersList) {
                    String creditLineAmount = obReadBalance1DataBalanceInnerCreditLineInner.getAmount().getAmount();
                    double totalCreditLine = 0.0;
                    totalCreditLine = Double.parseDouble(creditLineAmount);
                    double newCreditLine = 0.0;
                    if(financialServicesDetails.getCreditLine() != null) {
                        newCreditLine = Double.parseDouble(financialServicesDetails.getCreditLine());
                    }

                    totalCreditLine = totalCreditLine + newCreditLine;
                    financialServicesDetails.setCreditLine(String.valueOf(totalCreditLine));
                }

                // Total Debit
                if ("Debit".equalsIgnoreCase(obReadBalance1DataBalanceInner.getCreditDebitIndicator().getValue())) {
                    double totalDebits = 0.0;
                    if(financialServicesDetails.getCreditLine() != null) {
                        totalDebits = Double.parseDouble(financialServicesDetails.getTotalDebits());
                    }

                    double newDebit = 0.0;
                    newDebit = Double.parseDouble(obReadBalance1DataBalanceInner.getAmount().getAmount());

                    totalDebits = totalDebits + newDebit;
                    financialServicesDetails.setTotalDebits(String.valueOf(totalDebits));
                }
                // Total Credit
                if ("Credit".equalsIgnoreCase(obReadBalance1DataBalanceInner.getCreditDebitIndicator().getValue())) {
                    double totalCredit = 0.0;
                    if(!StringUtil.isNullOrEmpty(financialServicesDetails.getCreditLine())) {
                        totalCredit = Double.parseDouble(financialServicesDetails.getCreditLine());
                    }

                    double newCredit = 0.0;
                    newCredit = Double.parseDouble(obReadBalance1DataBalanceInner.getAmount().getAmount());

                    totalCredit = totalCredit + newCredit;
                    financialServicesDetails.setTotalCredits(String.valueOf(totalCredit));
                }
            }
        }

            financialServicesDetails.setTotalIncome(financialServicesDetails.getTotalCredits());
            financialServicesDetails.setTotalExpense(financialServicesDetails.getTotalDebits());

        double totalCredits = 0.0;
        totalCredits = Double.parseDouble(financialServicesDetails.getTotalCredits());

        double totalDebits = 0.0;
        if(!StringUtil.isNullOrEmpty(financialServicesDetails.getTotalDebits()))
        totalDebits = Double.parseDouble(financialServicesDetails.getTotalDebits());

        double accountBalance = 0.0;
        accountBalance = totalCredits - totalDebits;

            financialServicesDetails.setExistingBalance(String.valueOf(accountBalance));

        return financialServicesDetails;

    }

    public Double fetchDirectDebitAmount(String authorization , String xFapiFinancialId , String xFapiInteractionId, List<OBAccount6> obAccount6s) {
        double directDebitAmount = 0.0;
        for (OBAccount6 obAccount6 : obAccount6s) {

            // Getting Account Balance for Account ID
            OBReadDirectDebit2 obReadDirectDebit2 = directDebitsApi.getAccountsAccountIdDirectDebits(obAccount6.getAccountId(),
                    authorization,
                    xFapiFinancialId,
                    xFapiInteractionId);

             directDebitAmount = Double.valueOf(obReadDirectDebit2.getData().getDirectDebit().get(0).getPreviousPaymentAmount().getAmount());
        }
        return directDebitAmount;
    }

    public Double fetchScheduledPaymentAmount(String authorization , String xFapiFinancialId , String xFapiInteractionId, List<OBAccount6> obAccount6s) {
        double scheduledPaymentAmount = 0.0;
        for (OBAccount6 obAccount6 : obAccount6s) {

            // Getting Account Balance for Account ID
            OBReadScheduledPayment3 obReadScheduledPayment3 = scheduledPaymentsApi.getAccountsAccountIdScheduledPayments(obAccount6.getAccountId(),
                    authorization,
                    xFapiFinancialId,
                    xFapiInteractionId);

            scheduledPaymentAmount = Double.parseDouble(obReadScheduledPayment3.getData().getScheduledPayment().get(0).getInstructedAmount().getAmount());
        }
        return scheduledPaymentAmount;
    }
}

