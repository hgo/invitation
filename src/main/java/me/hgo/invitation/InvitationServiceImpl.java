package me.hgo.invitation;

import me.hgo.invitation.api.CodeUsageResult;
import me.hgo.invitation.api.InvitationAmountCalculator;
import me.hgo.invitation.api.InvitationSubscriber;
import me.hgo.invitation.api.MailSender;
import me.hgo.invitation.exception.InvitationCodeNotFound;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

class InvitationServiceImpl implements InvitationService {

    private final CodeRepo codeRepo;
    private final InvitationAmountCalculator invitationAmountCalculator;
    private final CreditRepo creditRepo;
    private final MailSender mailSender;

    public InvitationServiceImpl(CodeRepo codeRepo, InvitationAmountCalculator invitationAmountCalculator, CreditRepo creditRepo, MailSender mailSender) {
        this.codeRepo = codeRepo;
        this.invitationAmountCalculator = invitationAmountCalculator;
        this.creditRepo = creditRepo;
        this.mailSender = mailSender;
    }

    public String invitationCode(InvitationSubscriber user) {
        String _5digitCode = codeRepo.getUserCode(user.getId());
        if (_5digitCode != null) {
            return _5digitCode;
        }
        do {
            _5digitCode = generate(user);
        } while (codeRepo.isCodeExists(_5digitCode));

        Code code = new Code();
        code.setCode(_5digitCode);
        code.setSubscriber(user);
        codeRepo.createNewCode(code);
        return _5digitCode;
    }

    public CodeUsageResult invitedUserLoggedIn(InvitationSubscriber invitee, String code) throws InvitationCodeNotFound {
        code = code.trim().toLowerCase();
        Code invitorCode = codeRepo.findByCode(code);
        if (invitorCode == null) {
            throw new InvitationCodeNotFound(code);
        }
        List<Transaction> transactionsOfInviter = creditRepo.gainsOf(invitorCode.getSubscriber().getId());
        List<Transaction> transactionsOfInvitee = creditRepo.gainsOf(invitee.getId());
        CodeUsageResult codeUsageResult = invitationAmountCalculator.calculate(transactionsOfInviter, transactionsOfInvitee);
        if (codeUsageResult.getInviteeGain() > 0) {
            Transaction transaction = Transaction.builder()
                    .amount(codeUsageResult.getInviteeGain())
                    .fromId(invitorCode.getSubscriber().getId())
                    .toId(invitee.getId()).build();
            creditRepo.createNewTransaction(transaction);
            try {
                mailSender.onTransactionOccur(invitee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (codeUsageResult.getInvitorGain() > 0) {
            Transaction transaction = Transaction.builder()
                    .amount(codeUsageResult.getInvitorGain())
                    .fromId(invitee.getId())
                    .toId(invitorCode.getSubscriber().getId()).build();
            creditRepo.createNewTransaction(transaction);
            try {
                mailSender.onTransactionOccur(invitorCode.getSubscriber());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return codeUsageResult;
    }

    private String generate(InvitationSubscriber user) {
        return user.getName().toLowerCase().split(" ")[0] + "-" + RandomStringUtils.randomAlphanumeric(5).toLowerCase();
    }

    @Override
    public List<Transaction> getTransactionsOf(InvitationSubscriber user) {
        return creditRepo.gainsOf(user.getId());
    }
}












