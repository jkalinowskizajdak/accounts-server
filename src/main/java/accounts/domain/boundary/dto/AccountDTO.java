package accounts.domain.boundary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Jakub Kalinowski-Zajdak
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountDTO {

	public String id;
	public String owner;
	public double singleWithdrawLimit;
	public double balance;
	
	public AccountDTO() {
		
	}
	
	public AccountDTO(String pId, String pOwner, double pSingleWithdrawLimit, double pBalance) {
		id = pId;
		owner = pOwner;
		singleWithdrawLimit = pSingleWithdrawLimit;
		balance = pBalance;
	}
}
