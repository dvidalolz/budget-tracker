package input;

import input.internal.inputs.Input;

/**
 * Receives a transaction from the user and stores as persistent data
 * 
 * A transaction takes the form of a monetary input to an account. The account to which the 
 * transaction is stored is retrieved through user detail services spring security.
 * 
 * This is the central application-bounder for the transaction application, the entry-point into 
 * the Application Layer.
 */

public interface InputService {
    
    /**
     * Input transaction into user account
     * 
     * @param transaction an object representing the notable details of a transaction
     * (amount, date, type)
     */
    public void inputToAccount(Input input);
}
