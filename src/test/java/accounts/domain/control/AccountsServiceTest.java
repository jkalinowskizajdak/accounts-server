package accounts.domain.control;

import accounts.domain.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class AccountsServiceTest {

    private static final double balance_1 = 50000;
    private static final double balance_2 = 80000;
    private static final double limit_1 = 500;
    private static final double limit_2 = 1000;
    private static final String owner_1 = UUID.randomUUID().toString();
    private static final String owner_2 = UUID.randomUUID().toString();
    private static final Account testAccount_1 = Account.builder()
            .owner(owner_1)
            .balacne(balance_1)
            .singleWithdrawLimit(limit_1)
            .generateId()
            .build();
    private static final Account testAccount_2 = Account.builder()
            .owner(owner_2)
            .balacne(balance_2)
            .singleWithdrawLimit(limit_2)
            .generateId()
            .build();


    private static UUID accountId_1 = testAccount_1.getId();
    private static UUID accountId_2 = testAccount_2.getId();

    private static final AccountsService accountsService = AccountsService.getInstance();

    @BeforeClass
    public static void addAccounts() {
        accountsService.addAccount(testAccount_1);
        accountsService.addAccount(testAccount_2);
    }

    @AfterClass
    public static void deleteAccounts() {
        try {
            accountsService.deleteAccount(testAccount_1.getId());
            accountsService.deleteAccount(testAccount_2.getId());
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkAccounts() {
        Collection<Account> accounts = accountsService.geAllAccounts();
        Assertions.assertThat(accounts).extracting(Account::getId).contains(accountId_1, accountId_2);
    }

    @Test
    public void checkOwnerAccounts() {
        Collection<Account> accounts = accountsService.geOwnerAccounts(owner_1);
        Assertions.assertThat(accounts).extracting(Account::getId).contains(accountId_1);
        Assertions.assertThat(accounts).extracting(Account::getId).doesNotContain(accountId_2);
    }

    @Test(expected = AccountNotFoundException.class)
    public void checkNotFoundAccount() throws AccountNotFoundException {
        Account accountTest1 = accountsService.getAccount(UUID.randomUUID());
    }

    @Test
    public void checkLimits() throws AccountNotFoundException {
        Account accountTest1 = accountsService.getAccount(accountId_1);
        Assertions.assertThat(accountTest1.getSingleWithdrawLimit()).isEqualTo(limit_1);

        Account accountTest2 = accountsService.getAccount(accountId_2);
        Assertions.assertThat(accountTest2.getSingleWithdrawLimit()).isEqualTo(limit_2);
    }

    @Test(expected = RuntimeException.class)
    public void checkFoundsExceededOperation() throws AccountNotFoundException {
        double value = balance_1 + 100;
        accountsService.performOperation(accountId_1, new Operation(accountId_2.toString(), value));
    }

    @Test(expected = RuntimeException.class)
    public void checkLimitExceededOperation() throws AccountNotFoundException {
        double value = limit_1 + 100;
        accountsService.performOperation(accountId_1, new Operation(accountId_2.toString(), value));
    }

    @Test
    public void checkWithdrawOperation() throws AccountNotFoundException {
        double value = limit_1 - 100;

        double beforeBalanceTest1 = accountsService.getAccount(accountId_1).getBalance();
        double beforeBalanceTest2 = accountsService.getAccount(accountId_2).getBalance();

        accountsService.performOperation(accountId_1, new Operation(accountId_2.toString(), value));

        double balanceTest1 = accountsService.getAccount(accountId_1).getBalance();
        Assertions.assertThat(balanceTest1).isEqualTo(beforeBalanceTest1 - value);

        double balanceTest2 = accountsService.getAccount(accountId_2).getBalance();
        Assertions.assertThat(balanceTest2).isEqualTo(beforeBalanceTest2 + value);
    }


    @Test
    public void checkHistories() throws AccountNotFoundException {
        double beforeBalanceTest1 = accountsService.getAccount(accountId_1).getBalance();
        double beforeBalanceTest2 = accountsService.getAccount(accountId_2).getBalance();

        double value = limit_1 - 100;
        double afterBalanceTest1 = beforeBalanceTest1 - value;
        double afterBalanceTest2 = beforeBalanceTest2 + value;

        accountsService.performOperation(accountId_1, new Operation(accountId_2.toString(), value));

        Collection<AccountHistory> histories1 = accountsService.getAccountHistory(accountId_1);
        Optional<AccountHistory> history1 = histories1.stream().filter(history ->
                historyFilter(history, beforeBalanceTest1, afterBalanceTest1, OperationType.WITHDRAW)).findAny();
        Assertions.assertThat(history1).isPresent();
        Assertions.assertThat(history1.get().getFromTo()).isEqualTo(owner_2);

        Collection<AccountHistory> histories2 = accountsService.getAccountHistory(accountId_2);
        Optional<AccountHistory> history2 = histories2.stream().filter(history ->
                historyFilter(history, beforeBalanceTest2, afterBalanceTest2, OperationType.DEPOSIT)).findAny();
        Assertions.assertThat(history2).isPresent();
        Assertions.assertThat(history2.get().getFromTo()).isEqualTo(owner_1);
    }

    private boolean historyFilter(AccountHistory history, double beforeBalance, double afterBalance, OperationType operationType) {
        return history.getOperationType().equals(operationType)
                && history.getBeforeBalance() == beforeBalance
                && history.getAfterBalance() == afterBalance;
    }

}
