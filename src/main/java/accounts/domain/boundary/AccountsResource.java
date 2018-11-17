package accounts.domain.boundary;

import accounts.domain.boundary.dto.AccountCreateDTO;
import accounts.domain.boundary.dto.AccountDTO;
import accounts.domain.boundary.dto.AccountHistoryDTO;
import accounts.domain.boundary.dto.OperationDTO;
import accounts.domain.control.AccountNotFoundException;
import accounts.domain.control.AccountsMapper;
import accounts.domain.control.AccountsService;
import com.google.common.base.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Jakub Kalinowski-Zajdak
 */

@Produces(MediaType.APPLICATION_JSON)
@Path("/accounts")
public class AccountsResource {

    private static final String ACCOUNT_ID_PARAM = "accountId";
    private static final String OWNER_PARAM = "owner";
    private static final double ZERO = 0d;

    private final AccountsService accountsService = AccountsService.getInstance();

    @GET
    @Path("/all")
    public Collection<AccountDTO> getAllAccounts() {
        return accountsService.geAllAccounts().stream().map(AccountsMapper::mapAccountEntityToAccountDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/owner/{"+ OWNER_PARAM + "}")
    public Collection<AccountDTO> getAllAccounts(@PathParam(OWNER_PARAM) String owner) {
        return accountsService.geOwnerAccounts(owner).stream().map(AccountsMapper::mapAccountEntityToAccountDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/{"+ ACCOUNT_ID_PARAM + "}")
    public AccountDTO getAccount(@PathParam(ACCOUNT_ID_PARAM) UUID accountId) {
        try {
            return AccountsMapper.mapAccountEntityToAccountDTO(accountsService.getAccount(accountId));
        } catch (AccountNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @GET
    @Path("/{"+ ACCOUNT_ID_PARAM + "}/history")
    public List<AccountHistoryDTO> getAccountAllHistory(@PathParam(ACCOUNT_ID_PARAM) UUID accountId) {
        return accountsService.getAccountHistory(accountId).stream().map(AccountsMapper::mapAccountHistoryToAccountHistoryDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/{"+ ACCOUNT_ID_PARAM + "}/balance")
    public double getBalance(@PathParam(ACCOUNT_ID_PARAM) UUID accountId) {
        try {
            return accountsService.getAccount(accountId).getBalance();
        } catch (AccountNotFoundException e) {
            throw new NotFoundException(e);
        }
    }


    @POST
    @Path("/add")
    public Response addAccount(AccountCreateDTO accountDTO) {
        validateNewAccount(accountDTO);
        String accountId = accountsService.addAccount(AccountsMapper.mapAccountDTOtoAccountEntity(accountDTO));
        return Response.ok()
                .entity(accountId)
                .build();
    }

    @PUT
    @Path("/{"+ ACCOUNT_ID_PARAM + "}")
    public void operation(@PathParam(ACCOUNT_ID_PARAM) UUID accountId, OperationDTO operationDTO) {
        validateOperation(operationDTO);
        try {
            accountsService.performOperation(accountId, AccountsMapper.mapOperationDTOtoOperationEntity(operationDTO));
        } catch (AccountNotFoundException e) {
            throw new NotFoundException(e);
        }
    }


    @DELETE
    @Path("/{"+ ACCOUNT_ID_PARAM + "}")
    public void deleteAccount(@PathParam(ACCOUNT_ID_PARAM) UUID accountId) {
        try {
            accountsService.deleteAccount(accountId);
        } catch (AccountNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    private static void validateNewAccount(AccountCreateDTO newAccount) {
        if (Strings.isNullOrEmpty(newAccount.owner) || newAccount .balance < ZERO || newAccount.singleWithdrawLimit <= ZERO) {
            throw new BadRequestException("Wrong account properties!");
        }
    }

    private static void validateOperation(OperationDTO operation) {
        if (Strings.isNullOrEmpty(operation.targetAccountId) || operation.value <= ZERO) {
            throw new BadRequestException("Wrong operation properties!");
        }
    }
}
