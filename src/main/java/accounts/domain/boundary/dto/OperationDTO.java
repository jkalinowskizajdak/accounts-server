package accounts.domain.boundary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Jakub Kalinowski-Zajdak
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OperationDTO {
	
    public String targetAccountId;
    public double value;
    
    public OperationDTO() {
    	
    }
    
    public OperationDTO(String pTargetAccountId, double pAmount) {
        targetAccountId = pTargetAccountId;
    	value = pAmount;
    }
}
