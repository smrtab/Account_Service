package account.data.remote.enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteEmployee {

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotNull
    private String period;

    @NotNull
    private String salary;
}