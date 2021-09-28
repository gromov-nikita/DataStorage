package service.db.connection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;

public class DBConnection {
    private static Logger log = Logger.getLogger(DBConnection.class.getName());
    private Connection connection;
    private String nameDB;
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/resources/logInfo.properties"));
            Handler handler = new FileHandler(properties.getProperty("connectionLog"),true);
            handler.setFormatter(new SimpleFormatter());
            log.addHandler(handler);
            log.setUseParentHandlers(false);
        } catch (IOException e) {
            log.log(Level.WARNING,"File logger not working");
        }
    }
    public DBConnection(String login, String password, String url) throws SQLException {
        String[] arr = url.split("/");
        nameDB = arr[arr.length - 1];
        connection = DriverManager.getConnection(url, login, password);
        log.info(nameDB + " database. Created a connection." );
    }
    public String getNameDB() {
        return nameDB;
    }
    public Connection getConnection() {
        return connection;
    }
    public void closeConnection() throws SQLException {
        if(connection != null) {
            connection.close();
            connection = null;
            log.info(nameDB + " database connection. Close. ");
        }
    }
}


