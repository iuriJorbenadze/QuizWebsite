package database;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                // Load database.properties file
                Properties props = new Properties();
                InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("/database.properties");
                props.load(inputStream);

                // Fetch properties
                String driver = props.getProperty("db.driver");
                String url = props.getProperty("db.url");
                String username = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                // Register the driver and establish the connection
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to establish database connection!", e);
            }
            return connection;
        }
    }
}
