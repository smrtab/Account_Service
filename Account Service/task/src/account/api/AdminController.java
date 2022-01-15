package account.api;

import account.data.remote.enitity.RemotePayment;
import account.data.remote.enitity.RemoteUser;
import account.data.remote.request.AccessControlRequest;
import account.data.remote.request.ChangeUserRoleRequest;
import account.data.remote.response.StatusResponse;
import account.data.remote.response.UserDeleteResponse;
import account.service.AdminService;
import account.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("api/admin/user/")
    public List<RemoteUser> getUsers() {
        return adminService.getUsers();
    }

    @PutMapping("api/admin/user/role")
    public RemoteUser updateUserRole(@RequestBody ChangeUserRoleRequest request) {
        return adminService.updateUserRole(request);
    }

    @PutMapping("api/admin/user/access")
    public StatusResponse accessControl(@RequestBody AccessControlRequest request) {
        return adminService.accessControl(request);
    }

    @DeleteMapping("api/admin/user/{email}")
    public UserDeleteResponse deleteUser(@PathVariable String email) {
        return adminService.deleteUser(email);
    }
}
