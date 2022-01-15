package account.service;

import account.data.entity.Payment;
import account.data.exception.BreachedPasswordException;
import account.data.exception.DuplicatedPaymentException;
import account.data.exception.NegativeSalaryException;
import account.data.exception.TooShortPasswordException;
import account.data.remote.enitity.RemotePayment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentsValidatorService {

    private Map<String, Set<String>> paymentsCache = new HashMap<>();

    private boolean salaryIsNegative(long salary) {
        return salary < 0;
    }

    private boolean isDuplicated(String email, String period) {
        return paymentsCache.containsKey(email)
            && paymentsCache.get(email).contains(period);
    }

    public void validate(RemotePayment payment) {

        if (salaryIsNegative(payment.getSalary())) {
            throw new NegativeSalaryException();
        }
        if (isDuplicated(payment.getEmployee(), payment.getPeriod())) {
            throw new DuplicatedPaymentException();
        }

        String email = payment.getEmployee();
        String period = payment.getPeriod();
        if (paymentsCache.containsKey(email)) {
            paymentsCache.get(email).add(period);
        } else {
            Set<String> periods = new HashSet<>();
            periods.add(period);
            paymentsCache.put(email, periods);
        }
    }
}
