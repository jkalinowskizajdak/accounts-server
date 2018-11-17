package accounts.domain.entity;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Jakub Kalinowski-Zajdak
 */
public class Account {
	
	private final UUID id;
	private final String owner;
	private final double singleWithdrawLimit;
	private final double balance;
	private final Collection<AccountHistory> histories = Lists.newLinkedList();

	private Account(Builder builder) {
		id = builder.id;
		owner = builder.owner;
		singleWithdrawLimit = builder.singleWithdrawLimit;
		balance = builder.balance;
        histories.addAll(builder.histories);
	}
	
	public UUID getId() {
		return id;
	}

	public String getOwner() {
		return owner;
	}
	
	public double getSingleWithdrawLimit() {
	    return singleWithdrawLimit;
	}
	
	public double getBalance() {
	    return balance;
	}

    public Collection<AccountHistory> getHistories() {
        return histories;
    }

    public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		
		private UUID id;
		private String owner;
		private double singleWithdrawLimit;
		private double balance;
        private Collection<AccountHistory> histories = Lists.newLinkedList();

        public Builder copy(Account account) {
            id = account.id;
            owner = account.owner;
            singleWithdrawLimit = account.singleWithdrawLimit;
            balance = account.balance;
            histories.addAll(account.histories);
            return this;
        }
		
		public Builder id(UUID pId) {
			id = pId;
			return this;
		}
		
		public Builder generateId() {
			id = UUID.randomUUID();
			return this;
		}

		public Builder owner(String pOwner) {
        	owner = pOwner;
        	return this;
		}

		public Builder singleWithdrawLimit(double pSingleWithdrawLimit) {
			singleWithdrawLimit = pSingleWithdrawLimit;
			return this;
		}
		
		public Builder balacne(double pBalance) {
			balance = pBalance;
			return this;
		}

		public Builder histories(Collection<AccountHistory> pHistories) {
		    histories.addAll(histories);
		    return this;
        }

        public Builder history(AccountHistory history) {
            histories.add(history);
            return this;
        }
		
		public Account build() {
			return new Account(this);
		}
		
	}
	
}
