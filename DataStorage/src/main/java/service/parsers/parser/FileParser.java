package service.parsers.parser;

import org.xml.sax.SAXException;
import service.parsers.CSVParser;
import service.parsers.JSONParser;
import service.parsers.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FileParser {
    public static List parse(String path, Class myClass, Type type) throws NoSuchMethodException,
            IOException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchFieldException, ParserConfigurationException, SAXException {
        switch (type) {
            case JSON: {
                return JSONParser.fileParser(path,myClass);
            }
            case XML: {
                return XMLParser.fileParser(path,myClass);
            }
            case CSV: {
                return CSVParser.fileParser(path,myClass);
            }
            default: {
                throw new IllegalArgumentException("Wrong data type.");
            }
        }
    }
}
