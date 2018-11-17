package accounts.domain.boundary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Jakub Kalinowski-Zajdak
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountHistoryDTO {

    public String accountId;
    public String operationType;
    public String fromTo;
    public double beforeBalance;
    public double afterBalance;

    public  AccountHistoryDTO() {

    }
    public AccountHistoryDTO(String pAccountId, String pOperationType, String pFromTo, double pBeforeBalance, double pAfterBalance) {
        accountId = pAccountId;
        operationType = pOperationType;
        fromTo = pFromTo;
        beforeBalance = pBeforeBalance;
        afterBalance = pAfterBalance;
    }

}
