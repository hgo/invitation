package me.hgo.invitation;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

class CodeRepoImpl implements CodeRepo {

    private final NamedParameterJdbcTemplate template;

    public CodeRepoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public boolean isCodeExists(String digitCode) {
        return findByCode(digitCode) != null;
    }

    @Override
    public String getUserCode(CharSequence id) {
        try {
            return template.queryForObject("select code from invitation_code where owner_id = :id",
                    new MapSqlParameterSource("id", id), String.class);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void createNewCode(Code entity) {
        template.update("insert into invitation_code(code,owner_id,owner_name,owner_email) values(:code,:owner_id,:owner_name,:owner_email)",
                new MapSqlParameterSource().addValue("code", entity.getCode())
                        .addValue("owner_id", entity.getSubscriber().getId())
                        .addValue("owner_name", entity.getSubscriber().getName())
                        .addValue("owner_email", entity.getSubscriber().getEmail())
        );
    }

    @Override
    public Code findByCode(String code) {
        try {
            Code found = template.queryForObject("select * from invitation_code where code = :code",
                    new MapSqlParameterSource("code", code), new RowMapper<Code>() {
                        @Override
                        public Code mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Code code_ = new Code();
                            code_.setCode(rs.getString("code"));
                            Subscriber subscriber = Subscriber.of(rs.getString("owner_name"), rs.getString("owner_email"), rs.getString("owner_id"));
                            code_.setSubscriber(subscriber);
                            return code_;
                        }
                    });
            return found;
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
}
