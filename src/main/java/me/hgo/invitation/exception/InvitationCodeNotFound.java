package me.hgo.invitation.exception;

public class InvitationCodeNotFound extends Throwable {
    private final String code;

    public InvitationCodeNotFound(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
