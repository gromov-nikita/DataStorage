import models.user.User;
import org.xml.sax.SAXException;
import service.db.connection.DBConnection;
import service.db.query.Queries;
import service.parsers.CSVParser;
import service.parsers.XMLParser;
import service.parsers.JSONParser;
import service.parsers.parser.FileParser;
import service.parsers.parser.Type;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


public class Runner {
    public static void main(String[] args) {
        Properties propertiesFileInfo = new Properties();
        Properties propertiesDBInfo = new Properties();
        FileReader readerFileInfo = null;
        FileReader readerDBInfo = null;
        try {
            readerFileInfo = new FileReader("src/main/resources/fileInfo.properties");
            propertiesFileInfo.load(readerFileInfo);
            readerDBInfo = new FileReader("src/main/resources/dbInfo.properties");
            propertiesDBInfo.load(readerDBInfo);

            DBConnection connection = new DBConnection(propertiesDBInfo.getProperty("login"),
                    propertiesDBInfo.getProperty("password"),
                    propertiesDBInfo.getProperty("url"));
            Queries queries = new Queries(connection);
            List<User> listXML = (List<User>)FileParser.parse(
                    propertiesFileInfo.getProperty("xml"), User.class, Type.XML);
            for(User x : listXML) {
                queries.insert(x);
            }
            List<User> listJSON = (List<User>)FileParser.parse(
                    propertiesFileInfo.getProperty("json"), User.class, Type.JSON);
            for(User x : listJSON) {
                queries.insert(x);
            }
            List<User> listCSV = (List<User>)FileParser.parse(
                    propertiesFileInfo.getProperty("csv"), User.class, Type.CSV);
            for(User x : listCSV) {
                queries.insert(x);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(readerFileInfo != null) {
                try {
                    readerFileInfo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(readerDBInfo != null) {
                try {
                    readerDBInfo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
