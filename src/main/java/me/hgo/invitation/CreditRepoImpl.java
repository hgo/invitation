package me.hgo.invitation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

class CreditRepoImpl implements CreditRepo {

    private final NamedParameterJdbcTemplate template;

    public CreditRepoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Transaction> gainsOf(CharSequence id) {
        try {
            return template.query("select id,amount,from_id,to_id from invitation_transaction where to_id = :id",
                    new MapSqlParameterSource("id", id).getValues(), new TransactionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Transaction createNewTransaction(Transaction transaction) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        template.update("insert into invitation_transaction(from_id,to_id,amount) values(:from,:to,:amount)",
                new MapSqlParameterSource()
                        .addValue("from", transaction.getFromId())
                        .addValue("to", transaction.getToId())
                        .addValue("amount", transaction.getAmount())
                , keyHolder, new String[]{"id"});
        transaction.setId(keyHolder.getKey().longValue());
        return transaction;
    }

    private class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Transaction.builder()
                    .id(rs.getLong("id"))
                    .fromId(rs.getString("from_id"))
                    .toId(rs.getString("to_id"))
                    .amount(rs.getInt("amount"))
                    .build();
        }
    }
}
