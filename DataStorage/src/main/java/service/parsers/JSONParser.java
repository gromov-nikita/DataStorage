package service.parsers;

import org.json.JSONArray;
import org.json.JSONObject;
import service.notify.Notify;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class JSONParser {
    private static Logger logJSON = Logger.getLogger(JSONParser.class.getName());
    public static List fileParser(String path, Class myClass) throws IOException,
            InvocationTargetException, NoSuchMethodException, NoSuchFieldException,
            InstantiationException, IllegalAccessException {
        Notify.notify("JSON parsing...");
        String str = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            Scanner scan = new Scanner(reader);
            while (scan.hasNextLine()) {
                str += scan.nextLine();
            }
            List list = parseArr(str, myClass);
            Notify.notify("Successful JSON parsing.");
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOException();
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logJSON.info("Parsing json file: " + path);
        }
    }
    public static List parseArr(String jsonStr, Class myClass)  throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchFieldException {
        List list = new LinkedList();
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Field[] fields = myClass.getDeclaredFields();
        Object[] objects = new Object[fields.length];
        JSONArray array = new JSONArray(jsonStr);
        JSONObject obj;
        for(int i = 0; i < array.length(); i++) {
            obj = array.getJSONObject(i);
            for (int j = 0; j < fields.length; j++) {
                if(fields[j].getType().isPrimitive() || fields[j].getType().equals(String.class)) {
                    objects[j] = obj.get(fields[j].getName());
                }
                else {
                    objects[j] = parseObj(obj.getJSONObject(fields[j].getName()).toString(),
                            fields[j].getType());
                }
            }
            list.add(constructor.newInstance(objects));
        }
        return list;
    }
    public static Object parseObj(String jsonStr, Class myClass) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        JSONObject obj = new JSONObject(jsonStr);
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Field[] fields = myClass.getDeclaredFields();
        Object[] objects = new Object[fields.length];
        for(int i = 0; i < fields.length; i++) {
            if((fields[i].getType().isPrimitive() || fields[i].getType().equals(String.class))
                    && !fields[i].getType().equals(double.class)) {
                objects[i] = obj.get(fields[i].getName());
            }
            else {
                if(fields[i].getType().equals(double.class)) {
                    objects[i] = obj.getDouble(fields[i].getName());
                }
                else {
                    objects[i] = parseObj(obj.getJSONObject(fields[i].getName()).toString(),
                            fields[i].getType());
                }
            }
        }
        return constructor.newInstance(objects);
    }
}
