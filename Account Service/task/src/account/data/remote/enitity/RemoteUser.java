package account.data.remote.enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteUser {

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotNull
    private String email;

    @NotEmpty
    private List<String> roles;
}
