package account.api;

import account.data.remote.request.ChangePasswordRequest;
import account.data.remote.request.SignupRequest;
import account.data.remote.response.ChangePasswordResponse;
import account.data.remote.response.SignupResponse;
import account.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/auth/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.auth(request);
    }

    @PostMapping("api/auth/changepass")
    public ChangePasswordResponse changepass(@Valid @RequestBody ChangePasswordRequest request) {
        return authService.changepass(request);
    }
}
