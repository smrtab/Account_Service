package account.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't lock the ADMINISTRATOR!")
public class AdminUserLockException extends RuntimeException { }