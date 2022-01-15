package account.data.remote.enitity;

import account.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemotePayment {

    @NotNull
    private String period;

    @NotNull
    private long salary;

    @Email
    @NotNull
    @Pattern(regexp="^([a-zA-Z0-9\\-\\.\\_]+)@acme\\.com$")
    private String employee;
}