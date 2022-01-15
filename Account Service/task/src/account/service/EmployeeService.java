package account.service;

import account.data.entity.Payment;
import account.data.entity.User;
import account.data.exception.NoUserExistsException;
import account.data.remote.enitity.RemoteEmployee;
import account.data.remote.response.SignupResponse;
import account.data.repository.PaymentRepository;
import account.data.repository.UserRepository;
import io.micrometer.core.ipc.http.HttpSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final UtilsService utilsService;

    @Autowired
    public EmployeeService(
        UserRepository userRepository,
        PaymentRepository paymentRepository,
        UtilsService utilsService
    ) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.utilsService = utilsService;
    }

    public RemoteEmployee getPayment(String period) {

        User user = AuthService.currentUser();

        Optional<Payment> optionalPayment = paymentRepository.findByUserIdAndPeriod(
            user.getId(),
            utilsService.getPeriodAsLocalDate(period)
        );
        Payment payment = optionalPayment.get();

        return new RemoteEmployee(
            user.getName(),
            user.getLastname(),
            utilsService.getFormattedPeriod(payment.getPeriod()),
            utilsService.getFormattedSalary(payment.getSalary())
        );
    }

    public List<RemoteEmployee> getPayments() {
        User user = AuthService.currentUser();

        List<Payment> payments = user.getPayments().stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        List<RemoteEmployee> remoteEmployees = new ArrayList<>();

        for (Payment payment: payments) {
            remoteEmployees.add(
                new RemoteEmployee(
                    user.getName(),
                    user.getLastname(),
                    utilsService.getFormattedPeriod(payment.getPeriod()),
                    utilsService.getFormattedSalary(payment.getSalary())
                )
            );
        }

        return remoteEmployees;
    }
}
