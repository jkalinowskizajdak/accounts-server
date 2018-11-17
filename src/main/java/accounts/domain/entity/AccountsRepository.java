package accounts.domain.entity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Jakub Kalinowski-Zajdak
 */
public interface AccountsRepository {

	Optional<Account> getAccountById(UUID id);
	Collection<AccountHistory> getAccountHistoryById(UUID id);
	Collection<Account> getAllAccounts();
	Collection<Account> getOwnerAccounts(String owner);
	void addAccount(Account account);
	void updateAccount(Account account, OperationType operationType, String fromTo);
	void deleteAccount(UUID id);
}
