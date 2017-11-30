package me.hgo.invitation;

import lombok.Value;
import me.hgo.invitation.api.InvitationSubscriber;

@Value(staticConstructor = "of")
public class Subscriber implements InvitationSubscriber {

    private final String name;
    private final String email;
    private final CharSequence id;
}
