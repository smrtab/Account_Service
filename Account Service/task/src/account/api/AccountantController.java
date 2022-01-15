package account.api;

import account.data.entity.Payment;
import account.data.remote.enitity.RemotePayment;
import account.data.remote.response.SignupResponse;
import account.data.remote.response.StatusResponse;
import account.service.AccountantService;
import account.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountantController {

    private final AccountantService accountantService;

    @Autowired
    public AccountantController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping("api/acct/payments")
    public StatusResponse createPayments(@RequestBody List<RemotePayment> remotePayments) {
        return accountantService.createPayments(remotePayments);
    }

    @PutMapping("api/acct/payments")
    public StatusResponse updatePayments(@RequestBody RemotePayment remotePayment) {
        return accountantService.updatePayments(remotePayment);
    }
}
