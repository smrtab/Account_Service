package account.service;

import account.data.exception.BreachedPasswordException;
import account.data.exception.TooShortPasswordException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordValidationService {

    private final static int MIN_PASSWORD_LENGTH = 12;

    private final List<String> stopList = List.of(
        "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
        "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
        "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    );

    private boolean isBreached(String password) {
        return stopList.contains(password);
    }

    private boolean hasMinLength(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    public void validate(String password) {
        if (isBreached(password)) {
            throw new BreachedPasswordException();
        }
        if (!hasMinLength(password)) {
            throw new TooShortPasswordException();
        }
    }
}
