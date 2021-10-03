package service.db.connection;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnection {
    private static Logger log = Logger.getLogger(DBConnection.class.getName());
    private Connection connection;
    private String nameDB;
    private static DBConnection instance;

    private DBConnection(String login, String password, String url) throws SQLException {
        String[] arr = url.split("/");
        nameDB = arr[arr.length - 1];
        connection = DriverManager.getConnection(url, login, password);
        log.info(nameDB + " database. Created a connection." );
    }
    public static DBConnection getInstance(String login, String password, String url) throws SQLException {
        if(instance == null) {
            instance = new DBConnection(login,password,url);
        }
        return instance;
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


