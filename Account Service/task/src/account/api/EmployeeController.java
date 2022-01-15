package account.api;

import account.data.remote.enitity.RemoteEmployee;
import account.data.remote.response.SignupResponse;
import account.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "api/empl/payment", params ="period", method = RequestMethod.GET)
    public RemoteEmployee getPayment(String period) {
        return employeeService.getPayment(period);
    }

    @RequestMapping(value = "api/empl/payment", method = RequestMethod.GET)
    public List<RemoteEmployee> getPayments() {
        return employeeService.getPayments();
    }
}
