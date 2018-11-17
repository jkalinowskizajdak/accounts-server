package accounts.domain.control;

import accounts.domain.boundary.dto.AccountCreateDTO;
import accounts.domain.boundary.dto.AccountDTO;
import accounts.domain.boundary.dto.AccountHistoryDTO;
import accounts.domain.boundary.dto.OperationDTO;
import accounts.domain.entity.Account;
import accounts.domain.entity.AccountHistory;
import accounts.domain.entity.Operation;


/**
 * @author Jakub Kalinowski-Zajdak
 */
public class AccountsMapper {

	private AccountsMapper() {
		
	}
	
	public static Operation mapOperationDTOtoOperationEntity(OperationDTO operationDTO) {
		return new Operation(operationDTO.targetAccountId, operationDTO.value);
	}
	
	public static Account mapAccountDTOtoAccountEntity(AccountCreateDTO accountDTO) {
		return Account.builder()
				.generateId()
				.owner(accountDTO.owner)
				.balacne(accountDTO.balance)
				.singleWithdrawLimit(accountDTO.singleWithdrawLimit)
				.build();
	}
	
	public static AccountDTO mapAccountEntityToAccountDTO(Account account) {
		return new AccountDTO(account.getId().toString(), account.getOwner(), account.getSingleWithdrawLimit(), account.getBalance());
	}

	public static AccountHistoryDTO mapAccountHistoryToAccountHistoryDTO(AccountHistory account) {
		return new AccountHistoryDTO(account.getAccountId().toString(), account.getOperationType().getValue(), account.getFromTo(),
				account.getBeforeBalance(), account.getAfterBalance());
	}

}
