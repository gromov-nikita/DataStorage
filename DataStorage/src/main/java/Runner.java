import models.user.User;
import org.xml.sax.SAXException;
import service.parsers.CSVParser;
import service.parsers.XMLParser;
import service.parsers.JSONParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;


public class Runner {
    public static void main(String[] args) {
        Properties properties = new Properties();
        FileReader reader = null;
        try {
            reader = new FileReader("src/main/resources/fileInfo.properties");
            properties.load(reader);
            for(User x : (List<User>)XMLParser.fileParser(properties.getProperty("xml"), User.class)) {
                System.out.println(x.toString());
            }
            for(User x : (List<User>)JSONParser.fileParser(properties.getProperty("json"), User.class)) {
                System.out.println(x.toString());
            }
            for(User x : (List<User>) CSVParser.fileParser(properties.getProperty("csv"), User.class)) {
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
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
