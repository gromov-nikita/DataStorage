import models.observer.PhoneObsUser;
import models.table.user.User;
import org.xml.sax.SAXException;
import service.db.connection.DBConnection;
import service.db.query.Queries;
import service.observable.Observer;
import service.parsers.parser.FileParser;
import service.parsers.parser.Type;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/*
To develop application of parsing different format data files (.json, .xml, .csv).
All parsed data are stored in DB.
All actions that user produce (file process, parsing, store in db)
should be logged and displayed as status of processing data to user.
Application should provide report of loading information in DB.
Provide settings of search data (fields).
Application should notify user about completion of parsing, error of parsing (email, mobile as example)
Use patterns.
 */

public class Runner {
    public static void main(String[] args) {
        Properties propertiesFileInfo = new Properties();
        Properties propertiesDBInfo = new Properties();
        FileReader readerFileInfo = null;
        FileReader readerDBInfo = null;
        Observer observer = new Observer();
        FileParser parser = new FileParser(observer);
        observer.pushObsUser(new PhoneObsUser("phone1"));
        observer.pushObsUser(new PhoneObsUser("phone2"));
        observer.pushObsUser(new PhoneObsUser("phone3"));
        try {
            readerFileInfo = new FileReader("src/main/resources/fileInfo.properties");
            propertiesFileInfo.load(readerFileInfo);
            readerDBInfo = new FileReader("src/main/resources/dbInfo.properties");
            propertiesDBInfo.load(readerDBInfo);

            DBConnection connection = DBConnection.getInstance(propertiesDBInfo.getProperty("login"),
                    propertiesDBInfo.getProperty("password"),
                    propertiesDBInfo.getProperty("url"));
            Queries queries = new Queries(connection, observer);
            List<User> listXML = (List<User>)parser.parse(
                    propertiesFileInfo.getProperty("xml"), User.class, Type.XML);
            for(User x : listXML) {
                queries.insert(x);
            }
            List<User> listJSON = (List<User>)parser.parse(
                    propertiesFileInfo.getProperty("json"), User.class, Type.JSON);
            for(User x : listJSON) {
                queries.insert(x);
            }
            List<User> listCSV = (List<User>)parser.parse(
                    propertiesFileInfo.getProperty("csv"), User.class, Type.CSV);
            for(User x : listCSV) {
                queries.insert(x);
            }

            List<User> list = queries.selectByField(
                    User.class, User.class.getDeclaredField("age"),"4");
            for(User x : list) {
                System.out.println(x.toString());
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
