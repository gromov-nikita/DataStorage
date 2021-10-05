package service.parsers.parser;

import org.xml.sax.SAXException;
import service.observable.Observer;
import service.parsers.CSVParser;
import service.parsers.JSONParser;
import service.parsers.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FileParser {
    private Observer observer;
    public FileParser(Observer observer) {
        this.observer = observer;
    }
    public List parse(String path, Class myClass, Type type) throws IllegalAccessException,
            InstantiationException, NoSuchFieldException, NoSuchMethodException, IOException,
            InvocationTargetException, SAXException, ParserConfigurationException {
        try {
            List list;
            switch (type) {
                case JSON: {
                    observer.notify("JSON parsing...");
                    list = JSONParser.fileParser(path, myClass);
                    observer.notify("Successful JSON parsing.");
                    return list;
                }
                case XML: {
                    observer.notify("XML parsing...");
                    list = XMLParser.fileParser(path, myClass);
                    observer.notify("Successful XML parsing.");
                    return list;
                }
                case CSV: {
                    observer.notify("CSV parsing...");
                    list = CSVParser.fileParser(path, myClass);
                    observer.notify("Successful XML parsing.");
                    return list;
                }
                default: {
                    throw new IllegalArgumentException("Wrong data type.");
                }
            }
        } catch (IOException e) {
            observer.notify("Parsing error.");
            throw new IOException(e);
        } catch (InvocationTargetException e) {
            observer.notify("Parsing error.");
            throw new InvocationTargetException(e);
        } catch (NoSuchMethodException e) {
            observer.notify("Parsing error.");
            throw new NoSuchMethodException();
        } catch (NoSuchFieldException e) {
            observer.notify("Parsing error.");
            throw new NoSuchFieldException();
        } catch (InstantiationException e) {
            observer.notify("Parsing error.");
            throw new InstantiationException();
        } catch (IllegalAccessException e) {
            observer.notify("Parsing error.");
            throw new IllegalAccessException();
        } catch (SAXException e) {
            observer.notify("Parsing error.");
            throw new SAXException(e);
        } catch (ParserConfigurationException e) {
            observer.notify("Parsing error.");
            throw new ParserConfigurationException();
        }
    }
}
