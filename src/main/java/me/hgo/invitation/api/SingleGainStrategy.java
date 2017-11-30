package me.hgo.invitation.api;

import me.hgo.invitation.Transaction;

import java.util.List;

public class SingleGainStrategy implements InvitationAmountCalculator {

    private static SingleGainStrategy instance;
    private final int invitorGain;
    private final int inviteeGain;

    public SingleGainStrategy(int invitorGain, int inviteeGain) {
        this.invitorGain = invitorGain;
        this.inviteeGain = inviteeGain;
    }

    private SingleGainStrategy() {
        this.inviteeGain = 10;
        this.invitorGain = 50;
    }

    public static SingleGainStrategy getInstance() {
        return instance == null ? instance = new SingleGainStrategy() : instance;
    }

    @Override
    public CodeUsageResult calculate(List<Transaction> transactionsOfInviter, List<Transaction> transactionsOfInvitee) {
        return new CodeUsageResult() {

            @Override
            public int getInvitorGain() {
                return transactionsOfInviter.stream().filter(t -> t.getAmount() > 0).count() == 0 ? invitorGain : 0;
            }

            @Override
            public int getInviteeGain() {
                return transactionsOfInvitee.stream().filter(t -> t.getAmount() > 0).count() == 0 ? inviteeGain : 0;
            }
        };
    }
}
