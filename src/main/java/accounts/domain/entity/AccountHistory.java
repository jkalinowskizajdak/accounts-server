package accounts.domain.entity;

import java.util.UUID;

public class AccountHistory {

    private final UUID accountId;
    private final OperationType operationType;
    private final String fromTo;
    private final double beforeBalance;
    private final double afterBalance;

    private AccountHistory(Builder builder) {
        accountId = builder.accountId;
        operationType = builder.operationType;
        fromTo = builder.fromTo;
        beforeBalance = builder.beforeBalance;
        afterBalance = builder.afterBalance;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String getFromTo() {
        return fromTo;
    }

    public double getBeforeBalance() {
        return beforeBalance;
    }

    public double getAfterBalance() {
        return afterBalance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UUID accountId;
        private OperationType operationType;
        private String fromTo;
        private double beforeBalance;
        private double afterBalance;

        public Builder accountId(UUID pAccountId) {
            accountId = pAccountId;
            return this;
        }

        public Builder operationType(OperationType pOperationType) {
            operationType = pOperationType;
            return this;
        }

        public Builder fromTo(String pFromTo) {
            fromTo = pFromTo;
            return this;
        }

        public Builder beforeBalacne(double pBeforeBalance) {
            beforeBalance = pBeforeBalance;
            return this;
        }

        public Builder afterBalacne(double pAfterBalance) {
            afterBalance = pAfterBalance;
            return this;
        }

        public AccountHistory build() {
            return new AccountHistory(this);
        }

    }

}
