package service.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import service.observable.Observer;

public class XMLParser {
    private static Logger logXML = Logger.getLogger(XMLParser.class.getName());
    public static List fileParser(String path, Class myClass) throws ParserConfigurationException,
            IOException, SAXException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        logXML.info("Parsing xml file: " + path);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new File(path));
        NodeList list = document.getElementsByTagName(myClass.getSimpleName());
        List list1 = listHandler(list, myClass);
        return list1;
    }
    public static List listHandler(NodeList nList, Class myClass) throws IllegalAccessException,
            InstantiationException, InvocationTargetException {
        Node node;
        List list = new LinkedList();
        for(int i = 0; i < nList.getLength(); i++) {
            node = nList.item(i);
            list.add(objectHandler(node, myClass));
        }
        return list;
    }
    public static Object objectHandler(Node node, Class myClass) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Field[] fields = myClass.getDeclaredFields();
        NodeList nodeList = node.getChildNodes();
        Object[] objects = new Object[fields.length];
        Node n;
        for(int i = 0, j = 0; i < nodeList.getLength() && j < objects.length; i++) {
            n = nodeList.item(i);
            if(n.getNodeType() == Node.ELEMENT_NODE) {
                objects[j] = xmlReader(fields[j].getType(), n);
                j++;
            }
            else {
                continue;
            }
        }
        return myClass.getDeclaredConstructors()[0].newInstance(objects);
    }
    private static Object xmlReader(Class type, Node node) throws IllegalAccessException,
            InstantiationException, InvocationTargetException {
        if (type.equals(String.class)) {
            return node.getTextContent();
        } else {
            if(type.equals(int.class)) {
                return Integer.parseInt(node.getTextContent());
            }
            else {
                if(type.equals(double.class)) {
                    return Double.parseDouble(node.getTextContent());
                }
                else {
                    if(type.equals(boolean.class)) {
                        return Boolean.parseBoolean(node.getTextContent());
                    }
                    else {
                        return objectHandler(node, type);
                    }
                }
            }
        }
    }
}
