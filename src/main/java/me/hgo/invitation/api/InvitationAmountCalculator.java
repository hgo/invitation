package me.hgo.invitation.api;

import me.hgo.invitation.Transaction;

import java.util.List;

public interface InvitationAmountCalculator {

    CodeUsageResult calculate(List<Transaction> transactionsOfInviter, List<Transaction> transactionsOfInvitee);
}
