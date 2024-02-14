package transactions;

import common.date.SimpleDate;
import common.money.MonetaryAmount;


/**
 * A transaction event that occurred, representing an input made to an account by the account holder on a specific date.
 * 
 * The transaction should map to an account and include the "Type"
 * 
 * A value object. Immutable.
 */
public class Transaction {

    private MonetaryAmount amount;

    private SimpleDate date;

    private String type;

    public Transaction(MonetaryAmount amount, SimpleDate date, String type) {
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public static Transaction createTransacton(String amount, SimpleDate date, String type) {
        return new Transaction(MonetaryAmount.valueOf(amount), date, type);
    }
    
}
