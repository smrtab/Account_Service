package account.data.remote.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String lastname;

    @Email
    @NotNull
    @Pattern(regexp="^([a-zA-Z0-9\\-\\.\\_]+)@acme\\.com$")
    private final String email;

    @NotBlank
    private final String password;
}
