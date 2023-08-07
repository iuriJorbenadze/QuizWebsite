package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractDAO {

    protected String jdbcURL;
    protected String jdbcUsername;
    protected String jdbcPassword;

    public AbstractDAO() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/database.properties"));
            jdbcURL = properties.getProperty("db.url");
            jdbcUsername = properties.getProperty("db.user");
            jdbcPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }
}
