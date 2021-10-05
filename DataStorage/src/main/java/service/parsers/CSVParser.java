package service.parsers;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.apache.log4j.Logger;
import service.observable.Observer;

public class CSVParser {
    private static Logger logCSV = Logger.getLogger(CSVParser.class.getName());
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
            list = listHandler(list, myClass);
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
        return list;
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
