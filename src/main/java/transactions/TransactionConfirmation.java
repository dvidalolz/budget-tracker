package transactions;

public class TransactionConfirmation {
    
    private String confirmationNumber;

    private AccountContribution accountContribution;

    public TransactionConfirmation(String confirmationNumber, AccountContribution accountContribution) {
        this.confirmationNumber = confirmationNumber;
        this.accountContribution = accountContribution;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public AccountContribution getAccountContribution() {
        return accountContribution;
    }

    public String toString() {
        return confirmationNumber;
    }
}
