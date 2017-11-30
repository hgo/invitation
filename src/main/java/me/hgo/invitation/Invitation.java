package me.hgo.invitation;

import me.hgo.invitation.api.*;
import me.hgo.invitation.exception.InvitationCodeNotFound;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class Invitation {

    private final NamedParameterJdbcTemplate template;
    private final InvitationService service;


    public Invitation(DataSource ds, InvitationAmountCalculator calculator, MailSender mailSender) {
        this.template = new NamedParameterJdbcTemplate(ds);
        createTables();
        CodeRepo codeRepo = new CodeRepoImpl(this.template);
        CreditRepo transactionRepo = new CreditRepoImpl(this.template);
        this.service = new InvitationServiceImpl(codeRepo, calculator, transactionRepo, mailSender);
    }

    public Invitation(DataSource ds) {
        this(ds, SingleGainStrategy.getInstance(), NoopMailSender.INSTANCE);
    }


    private void createTables() {
        Integer exists = template.queryForObject("SELECT count(1) FROM INFORMATION_SCHEMA.TABLES WHERE lower(TABLE_NAME) = 'invitation_transaction'", EmptySqlParameterSource.INSTANCE, Integer.class);
        if (exists == 0) {
            SqlExecutor.getInstance().execute(template, "ddl.sql");
        }
    }

    public String invitationCode(InvitationSubscriber user) {
        return service.invitationCode(user);
    }

    public CodeUsageResult invitedUserLoggedIn(InvitationSubscriber user, String code) throws InvitationCodeNotFound {
        return service.invitedUserLoggedIn(user, code);
    }

    public List<Transaction> getTransactionsOf(InvitationSubscriber user) {
        return service.getTransactionsOf(user);
    }
}
