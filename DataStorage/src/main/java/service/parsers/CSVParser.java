package service.parsers;

import service.db.query.Queries;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.*;

public class CSVParser {
    private static Logger logCSV = Logger.getLogger(CSVParser.class.getName());
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/resources/logInfo.properties"));
            Handler handler = new FileHandler(properties.getProperty("csvLog"));
            handler.setFormatter(new SimpleFormatter());
            logCSV.addHandler(handler);
            logCSV.setUseParentHandlers(false);
        } catch (IOException e) {
            logCSV.log(Level.WARNING,"File logger not working");
        }
    }
    public static List fileParser(String path, Class myClass) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        BufferedReader reader = null;
        List<String> list = new LinkedList<String>();
        try {
            reader = new BufferedReader(new FileReader(path));
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNext()) {
                list.add(scanner.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logCSV.info("Parsing csv file : " + path);
        }
        return listHandler(list, myClass);
    }
    public static List listHandler(List<String> list, Class myClass) throws IllegalAccessException,
            InstantiationException, InvocationTargetException {
        List<Object> ans = new LinkedList<Object>();
        for(String x : list) {
            ans.add(objectHandler(new ArrayList<String>(Arrays.asList(x.split(";"))), myClass));
        }
        return ans;
    }
    public static Object objectHandler(List<String> list, Class myClass) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Field[] fields = myClass.getDeclaredFields();
        Object[] objects = new Object[list.size()];
        for(int i = 0; i < list.size(); i++) {
            objects[i] = csvReader(fields[i].getType(),list.get(i));
        }
        return myClass.getDeclaredConstructors()[0].newInstance(objects);
    }
    private static Object csvReader(Class type, String str)  {
        if (type.equals(String.class)) {
            return str;
        } else {
            if(type.equals(int.class)) {
                return Integer.parseInt(str);
            }
            else {
                if(type.equals(double.class)) {
                    return Double.parseDouble(str);
                }
                else {
                    return Boolean.parseBoolean(str);
                }
            }
        }
    }
}
