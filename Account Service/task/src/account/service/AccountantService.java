package account.service;

import account.data.entity.Payment;
import account.data.entity.User;
import account.data.exception.NoUserExistsException;
import account.data.remote.enitity.RemotePayment;
import account.data.remote.response.StatusResponse;
import account.data.repository.PaymentRepository;
import account.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountantService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final UtilsService utilsService;

    @Autowired
    public AccountantService(
        UserRepository userRepository,
        PaymentRepository paymentRepository,
        UtilsService utilsService
    ) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.utilsService = utilsService;
    }

    @Transactional
    public StatusResponse createPayments(List<RemotePayment> remotePayments) {

        PaymentsValidatorService paymentsValidatorService = new PaymentsValidatorService();
        for (RemotePayment remotePayment : remotePayments) {
            paymentsValidatorService.validate(remotePayment);

            Optional<User> optionalUser = userRepository.findByEmail(remotePayment.getEmployee());
            if (optionalUser.isEmpty()) {
                throw  new NoUserExistsException();
            }

            User user = optionalUser.get();

            LocalDate period = utilsService.getPeriodAsLocalDate(remotePayment.getPeriod());

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setSalary(remotePayment.getSalary());
            payment.setPeriod(period);

            paymentRepository.save(payment);
        }

        return new StatusResponse(
            "Added successfully!"
        );
    }

    public StatusResponse updatePayments(RemotePayment remotePayment) {

        PaymentsValidatorService paymentsValidatorService = new PaymentsValidatorService();
        paymentsValidatorService.validate(remotePayment);

        Optional<User> optionalUser = userRepository.findByEmail(remotePayment.getEmployee());
        if (optionalUser.isEmpty()) {
            throw new NoUserExistsException();
        }

        User user = optionalUser.get();
        LocalDate period = utilsService.getPeriodAsLocalDate(remotePayment.getPeriod());
        Optional<Payment> optionalPayment = paymentRepository.findByUserIdAndPeriod(user.getId(), period);

        Payment payment;
        if (optionalPayment.isPresent()) {
            payment = optionalPayment.get();
        } else {
            payment = new Payment();
        }

        payment.setUser(user);
        payment.setSalary(remotePayment.getSalary());
        payment.setPeriod(period);

        paymentRepository.save(payment);

        return new StatusResponse(
            "Updated successfully!"
        );
    }
}
