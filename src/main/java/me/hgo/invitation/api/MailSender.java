package me.hgo.invitation.api;

public interface MailSender {

    public void onTransactionOccur(InvitationSubscriber subscriber) throws Exception;
}
