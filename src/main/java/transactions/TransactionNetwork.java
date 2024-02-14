package transactions;

/**
 * Receives a transaction from the user and stores as persistent data
 * 
 * A transaction takes the form of a monetary input to an account. The account to which the 
 * transaction is stored is retrieved through user detail services spring security.
 * 
 * This is the central application-bounder for the transaction application, the entry-point into 
 * the Application Layer.
 */

public interface TransactionNetwork {
    
    public TransactionConfirmation inputToAccount(Transaction transaction);
}
