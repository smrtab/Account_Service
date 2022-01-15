package account.data.remote.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AccessControlRequest {

    public static final String OPERATION_LOCK = "LOCK";
    public static final String OPERATION_UNLOCK = "UNLOCK";

    @NotBlank
    private final String user;

    @NotBlank
    private final String operation;
}
