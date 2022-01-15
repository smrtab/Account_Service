package account.data.remote.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDeleteResponse {

    @NotNull
    private final String user;

    @NotNull
    private final String status;
}