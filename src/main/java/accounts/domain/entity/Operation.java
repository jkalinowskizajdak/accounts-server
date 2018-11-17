package accounts.domain.entity;

import java.util.UUID;

/**
 * @author Jakub Kalinowski-Zajdak
 */
public class Operation {

    private UUID targetAccountId;
    private final double value;
    
    public Operation(String pTargetAccountId, double pValue) {
        targetAccountId = UUID.fromString(pTargetAccountId);
    	value = pValue;
    }
    
    public UUID getTargetAccountId() {
        return targetAccountId;
    }
    
    public double getValue() {
        return value;
    }
	
}
