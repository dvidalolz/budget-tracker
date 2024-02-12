package transactions;

import java.util.Date;


/**
 * A transaction event that occurred, representing an input made to an account by the account holder on a specific date.
 * 
 * The transaction should map to an account and include the "Type"
 * 
 * A value object. Immutable.
 */
public class Transaction {

    private String userID;

    private String transactionID;

    private Date date;

    private String transactionType;
    
    private double amount;

    
}
