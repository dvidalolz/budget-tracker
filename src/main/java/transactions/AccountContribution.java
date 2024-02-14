package transactions;

import common.money.MonetaryAmount;

public class AccountContribution {

    private String accountNumber;

    private MonetaryAmount amount;

    public AccountContribution(String accountNumber, MonetaryAmount amount) {
        this.accountNumber  = accountNumber;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }
}
