package account.data.remote.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ChangeUserRoleRequest {

    public static final String OPERATION_GRANT = "GRANT";
    public static final String OPERATION_REMOVE = "REMOVE";

    @NotBlank
    private final String user;

    @NotBlank
    private final String role;

    @NotBlank
    private final String operation;
}
