package accounts.domain.entity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Jakub Kalinowski-Zajdak
 */
public class AccountInMemoryRepository implements AccountsRepository {

    private static final Cache<UUID, Account> accounts = CacheBuilder.newBuilder().build();

    @Override
    public Optional<Account> getAccountById(UUID id) {
        return Optional.ofNullable(accounts.getIfPresent(id));
    }

    @Override
    public Collection<AccountHistory> getAccountHistoryById(UUID id) {
        Optional<Account> account = getAccountById(id);
        if (account.isPresent()) {
            return account.get().getHistories();
        }
        return Lists.newArrayList();
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accounts.asMap().values();
    }

    @Override
    public Collection<Account> getOwnerAccounts(String owner) {
        return accounts.asMap().values().stream().filter(account -> account.getOwner().equals(owner)).collect(Collectors.toList());
    }

    @Override
    public void addAccount(Account newAccount) {
        accounts.put(newAccount.getId(), newAccount);
    }

    @Override
    public void updateAccount(Account account, OperationType operationType, String fromTO) {
        Optional<Account> oldAccount = getAccountById(account.getId());
        AccountHistory history = AccountHistory.builder()
                .accountId(account.getId())
                .beforeBalacne(oldAccount.get().getBalance())
                .fromTo(fromTO)
                .afterBalacne(account.getBalance())
                .operationType(operationType)
                .build();
        Account newAccount = Account.builder()
                .copy(account)
                .history(history)
                .build();
        accounts.put(newAccount.getId(), newAccount);
    }

    @Override
    public void deleteAccount(UUID id) {
        accounts.invalidate(id);
    }

}
