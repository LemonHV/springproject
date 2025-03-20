package myproject.springproject.Exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized error"),
    USER_EXISTED(1001, "User existed"),
    INVALID_KEY(1002, "Invalid message key"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
    USER_NOT_EXIST(1005, "User not exist"),
    UNAUTHENTICATED(1006, "Unauthenticated")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
