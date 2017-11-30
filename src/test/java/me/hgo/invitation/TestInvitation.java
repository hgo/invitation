package me.hgo.invitation;

import me.hgo.invitation.api.*;
import me.hgo.invitation.exception.InvitationCodeNotFound;
import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class TestInvitation {

    private static DriverManagerDataSource ds;
    private static NamedParameterJdbcTemplate template = createNamedTemplate();
    private CodeRepo codeRepo = new CodeRepoImpl(template);
    private InvitationAmountCalculator calculator = SingleGainStrategy.getInstance();
    private CreditRepo creditRepo = new CreditRepoImpl(template);

    private InvitationSubscriber guven = Subscriber.of("Güven Ozyurt", "guven@asdasd.com", "12345");

    public TestInvitation() {
        Integer exists = template.queryForObject("SELECT count(1) FROM INFORMATION_SCHEMA.TABLES WHERE lower(TABLE_NAME) = 'invitation_transaction'", EmptySqlParameterSource.INSTANCE, Integer.class);
        if (exists == 0) {
            SqlExecutor.getInstance().execute(template, "ddl.sql");
        }
    }

    private static NamedParameterJdbcTemplate createNamedTemplate() {

        Properties properties = new Properties();
        properties.setProperty("username", "sa");
        properties.setProperty("password", "");

        ds = new DriverManagerDataSource("jdbc:h2:mem:db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1", properties);
        ds.setDriverClassName("org.h2.Driver");
        return new NamedParameterJdbcTemplate(ds);
    }

    @After
    public void after() throws SQLException {
        ds.getConnection().rollback();
    }

    @Test
    public void test() {
        InvitationService service = new InvitationServiceImpl(codeRepo, calculator, creditRepo, NoopMailSender.INSTANCE);
        String code = service.invitationCode(guven);
        assertEquals(11, code.length());
    }

    @Test
    public void testCodeRepoImplementation() {
        InvitationService service = new InvitationServiceImpl(codeRepo, calculator, creditRepo, NoopMailSender.INSTANCE);
        String code = service.invitationCode(guven);
        assertEquals(11, code.length());
        Code byCode = codeRepo.findByCode(code);
        assertNotNull(byCode);
        assertEquals(code, byCode.getCode());
        assertEquals("12345", byCode.getSubscriber().getId());
    }

    @Test
    public void testCreditRepoImplementation() {
        CreditRepoImpl creditRepo = new CreditRepoImpl(template);
        Transaction newTransaction = creditRepo.createNewTransaction(Transaction.builder().toId("12345")
                .fromId("34455")
                .amount(123)
                .build()
        );
        assertNotNull(newTransaction.getId());
        List<Transaction> transactions = creditRepo.gainsOf("12345");
        assertTrue(transactions.size() > 0);
        System.err.println("all transactions " + transactions);
    }

    @Test
    public void testInvitationCreation() throws InvitationCodeNotFound {
        Invitation invitation = new Invitation(ds, calculator, NoopMailSender.INSTANCE);
        String invitationCode = invitation.invitationCode(guven);
        Subscriber other = Subscriber.of("other", "email@fomain.com", "4321");
        CodeUsageResult codeUsageResult = invitation.invitedUserLoggedIn(other, invitationCode);
        assertEquals(50, codeUsageResult.getInvitorGain());
        assertEquals(10, codeUsageResult.getInviteeGain());
        List<Transaction> transactions = invitation.getTransactionsOf(guven);
        Transaction transaction = transactions.stream().filter(t -> t.getFromId().equals(other.getId())).findFirst().get();
        assertNotNull(transaction);
        assertEquals(50, transaction.getAmount());
        assertEquals(other.getId(), transaction.getFromId());
        assertEquals(guven.getId(), transaction.getToId());
        System.err.println("guven transactions : " + transactions);

    }
}
