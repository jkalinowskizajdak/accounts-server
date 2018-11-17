package accounts.domain.boundary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Jakub Kalinowski-Zajdak
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountCreateDTO {

	public String owner;
	public double singleWithdrawLimit;
	public double balance;
	
	public AccountCreateDTO() {
		
	}
	
	public AccountCreateDTO(String pOwner, double pSingleWithdrawLimit, double pBalance) {
		owner = pOwner;
		singleWithdrawLimit = pSingleWithdrawLimit;
		balance = pBalance;
	}
}
