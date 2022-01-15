package account.service;

import account.data.exception.IncorrectSalaryPeriodException;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilsService {

    public LocalDate getPeriodAsLocalDate(String period) {

        List<Integer> periodParts = Arrays.stream(period.split("-"))
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        try {
            LocalDate periodAsLocalDate = LocalDate.of(
                periodParts.get(1),
                periodParts.get(0),
                1
            );
            return periodAsLocalDate;
        } catch (DateTimeException exception) {
            throw new IncorrectSalaryPeriodException();
        }
    }

    public String getFormattedPeriod(LocalDate period) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM-YYYY");
        return period.format(dateTimeFormatter);
    }

    public String getFormattedSalary(long salary) {
        return  String.format(
            "%d dollar(s) %d cent(s)",
            salary / 100,
            salary - (salary / 100) * 100
        );
    }
}
