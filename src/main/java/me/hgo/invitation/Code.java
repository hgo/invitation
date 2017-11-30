package me.hgo.invitation;

import lombok.Getter;
import lombok.Setter;
import me.hgo.invitation.api.InvitationSubscriber;

@Getter
@Setter
class Code {

    private String code;
    private InvitationSubscriber subscriber;
}
