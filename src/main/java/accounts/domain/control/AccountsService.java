package accounts.domain.control;

import accounts.domain.entity.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Jakub Kalinowski-Zajdak
 */
public class AccountsService {

    private static AccountsService instance;
    private final AccountsRepository accountsRepository;

    private AccountsService() {
        accountsRepository = new AccountInMemoryRepository();
    }

    public static synchronized AccountsService getInstance() {
        if (instance == null) {
            instance = new AccountsService();
        }
        return instance;
    }


    public Account getAccount(UUID accountId) throws AccountNotFoundException {
        Account account = getAccountFromRepository(accountId);
        System.out.println("Get account " + accountId + " from repository.");
        return account;
    }

    public Collection<Account> geAllAccounts() {
        Collection<Account> accounts =  accountsRepository.getAllAccounts();
        System.out.println("There are " + accounts.size() + " accounts in repository.");
        return accounts;
    }

    public Collection<Account> geOwnerAccounts(String owner) {
        Collection<Account> accounts =  accountsRepository.getOwnerAccounts(owner);
        System.out.println(owner + " has " + accounts.size() + " accounts in repository.");
        return accounts;
    }

    public synchronized String addAccount(Account account) {
        Optional<Account> oldAccount = accountsRepository.getAccountById(account.getId());
        if (oldAccount.isPresent()) {
            throw new RuntimeException("Account " + account.getId().toString() + " already exists!!!");
        }
        accountsRepository.addAccount(account);
        System.out.println("New account with id:" + account.getId().toString() + " has been created.");
        return account.getId().toString();
    }

    public synchronized void performOperation(UUID id, Operation operation) throws AccountNotFoundException {
        Account sourceAccount = getAccountFromRepository(id);
        validateAccountOperation(sourceAccount, operation.getValue());
        Account targetAccount = getAccountFromRepository(operation.getTargetAccountId());
        Account modifiedSource = Account.builder()
                .copy(sourceAccount)
                .balacne(sourceAccount.getBalance() - operation.getValue())
                .build();
        Account modifiedTarget = Account.builder()
                .copy(targetAccount)
                .balacne(targetAccount.getBalance() + operation.getValue())
                .build();
        accountsRepository.updateAccount(modifiedSource, OperationType.WITHDRAW, modifiedTarget.getOwner());
        System.out.println("Account with id:" + id.toString() + " has been updated. New balance: " + modifiedSource.getBalance());
        accountsRepository.updateAccount(modifiedTarget, OperationType.DEPOSIT, modifiedSource.getOwner());
        System.out.println("Account with id:" + operation.getTargetAccountId().toString() + " has been updated. New balance:" + modifiedTarget.getBalance());
    }

    public synchronized void deleteAccount(UUID id) throws AccountNotFoundException {
        getAccountFromRepository(id);
        accountsRepository.deleteAccount(id);
        System.out.println("Account with id:" + id.toString() + " has been deleted.");
    }

    public Collection<AccountHistory> getAccountHistory(UUID id) {
        Collection<AccountHistory> histories =  accountsRepository.getAccountHistoryById(id);
        System.out.println("Account " + id.toString() + " has " + histories.size() + " histories.");
        return histories;
    }

    private Account getAccountFromRepository(UUID accountId) throws AccountNotFoundException {
        Optional<Account> account = accountsRepository.getAccountById(accountId);
        if (!account.isPresent()) {
            throw new AccountNotFoundException(accountId.toString());
        }
        return account.get();
    }

    private void validateAccountOperation(Account account, double operationValue) {
        if (account.getBalance() < operationValue) {
            String errorMessage = "There is not enough funds(" + account.getBalance() + ") for operation value " + operationValue;
            throwException(errorMessage);
        }
        if (account.getSingleWithdrawLimit() < operationValue) {
            String errorMessage = "Account withdraw limit(" + account.getSingleWithdrawLimit() + ") has been exceeded " + operationValue;
            throwException(errorMessage);
        }
    }

    private void throwException(String errorMessage) {
        System.out.println(errorMessage);
        throw new RuntimeException(errorMessage);
    }

}
