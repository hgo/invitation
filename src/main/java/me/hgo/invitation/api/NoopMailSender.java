package me.hgo.invitation.api;


public class NoopMailSender implements MailSender {

    public static final NoopMailSender INSTANCE = new NoopMailSender();

    private NoopMailSender() {}

    @Override
    public void onTransactionOccur(InvitationSubscriber subscriber) throws Exception {
        System.err.println("no op on transaction occur for " + subscriber);
    }
}
