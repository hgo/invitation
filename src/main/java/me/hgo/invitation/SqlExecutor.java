package me.hgo.invitation;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;

class SqlExecutor {

    private static SqlExecutor instance;

    public static SqlExecutor getInstance() {
        return instance == null ? instance = new SqlExecutor() : instance;
    }

    void execute(NamedParameterJdbcTemplate template, String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        String content = null;
        try {
            content = IOUtils.toString(classLoader.getResourceAsStream(filePath), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        template.getJdbcOperations().execute(content);
    }
}
