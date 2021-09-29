package service.parsers.parser;

import org.xml.sax.SAXException;
import service.notify.Notify;
import service.parsers.CSVParser;
import service.parsers.JSONParser;
import service.parsers.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FileParser {
    public static List parse(String path, Class myClass, Type type) throws IllegalAccessException,
            InstantiationException, NoSuchFieldException, NoSuchMethodException, IOException,
            InvocationTargetException, SAXException, ParserConfigurationException {
        try {
            switch (type) {
                case JSON: {
                    return JSONParser.fileParser(path, myClass);
                }
                case XML: {
                    return XMLParser.fileParser(path, myClass);
                }
                case CSV: {
                    return CSVParser.fileParser(path, myClass);
                }
                default: {
                    throw new IllegalArgumentException("Wrong data type.");
                }
            }
        } catch (IOException e) {
            Notify.notify("Parsing error.");
            throw new IOException(e);
        } catch (InvocationTargetException e) {
            Notify.notify("Parsing error.");
            throw new InvocationTargetException(e);
        } catch (NoSuchMethodException e) {
            Notify.notify("Parsing error.");
            throw new NoSuchMethodException();
        } catch (NoSuchFieldException e) {
            Notify.notify("Parsing error.");
            throw new NoSuchFieldException();
        } catch (InstantiationException e) {
            Notify.notify("Parsing error.");
            throw new InstantiationException();
        } catch (IllegalAccessException e) {
            Notify.notify("Parsing error.");
            throw new IllegalAccessException();
        } catch (SAXException e) {
            Notify.notify("Parsing error.");
            throw new SAXException(e);
        } catch (ParserConfigurationException e) {
            Notify.notify("Parsing error.");
            throw new ParserConfigurationException();
        }
    }
}
