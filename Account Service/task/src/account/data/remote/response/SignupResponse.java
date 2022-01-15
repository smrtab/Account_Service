package account.data.remote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class SignupResponse {

    @NotNull
    private final long id;

    @NotBlank
    private final String name;

    @NotBlank
    private final String lastname;

    @Email
    private final String email;

    @NotEmpty
    private final List<String> roles;
}
