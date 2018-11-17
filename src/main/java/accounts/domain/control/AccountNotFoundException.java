package accounts.domain.control;

public class AccountNotFoundException extends  Exception {

    public AccountNotFoundException(String id) {
        super("Account " + id + " does not exist!!!");
    }
}
