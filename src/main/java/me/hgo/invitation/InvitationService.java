package me.hgo.invitation;

import me.hgo.invitation.api.CodeUsageResult;
import me.hgo.invitation.api.InvitationSubscriber;
import me.hgo.invitation.exception.InvitationCodeNotFound;

import java.util.List;

interface InvitationService {

    public String invitationCode(InvitationSubscriber user);

    public CodeUsageResult invitedUserLoggedIn(InvitationSubscriber user, String code) throws InvitationCodeNotFound;

    List<Transaction> getTransactionsOf(InvitationSubscriber user);
}
