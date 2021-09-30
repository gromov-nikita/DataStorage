package service.db.query;
import models.user.IQueryTable;
import service.db.connection.DBConnection;
import service.notify.Notify;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;

public class Queries {
    private DBConnection connection;
    private Statement statement;
    private static Logger logQ = Logger.getLogger(Queries.class.getName());
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/resources/logInfo.properties"));
            Handler handler = new FileHandler(properties.getProperty("queryLog"));
            handler.setFormatter(new SimpleFormatter());
            logQ.addHandler(handler);
            logQ.setUseParentHandlers(false);
        } catch (IOException e) {
            logQ.log(Level.WARNING,"File logger not working");
        }
    }
    public Queries(DBConnection connection) throws SQLException {
        this.connection = connection;
        statement = connection.getConnection().createStatement();
        logQ.info(connection.getNameDB() + " database. Created a statement.");
    }
    public void insert(IQueryTable query) throws SQLException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Notify.notify("Push to database...");
        StringBuffer str = new StringBuffer("INSERT INTO " +
                query.getClass().getDeclaredMethod("getTableName").invoke(query) + " SET ");
        stringMaker(str,query);
        logQ.info(connection.getNameDB() + " " + str);
        statement.executeUpdate(String.valueOf(str));
        Notify.notify("Successful push to database.");
    }
    public void deleteByID(Class myClass, int id) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, SQLException {
        logQ.info(connection.getNameDB() +
                "DELETE FROM " + myClass.getDeclaredMethod("getTableName").invoke(null) +
                " WHERE ID= " + id);
        statement.executeUpdate(
                "DELETE FROM " + myClass.getDeclaredMethod("getTableName").invoke(null) +
                        " WHERE ID= " + id);
    }
    public void updateByID(IQueryTable query, int id) throws SQLException, NoSuchFieldException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        StringBuffer str = new StringBuffer("UPDATE " +
                query.getClass().getDeclaredMethod("getTableName").invoke(query) + " SET ");
        stringMaker(str,query);
        str.append(" WHERE ID = " + id);
        logQ.info(connection.getNameDB() + " " + str);
        statement.executeUpdate(String.valueOf(str));
    }
    public List selectAll(Class myClass) throws SQLException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchFieldException {
        logQ.info(connection.getNameDB() + " Select all from " +
                myClass.getDeclaredMethod("getTableName").invoke(null));
        ResultSet res = statement.executeQuery("SELECT * " + "FROM " +
                myClass.getDeclaredMethod("getTableName").invoke(null));
        List list = new LinkedList();
        Field[] fields = myClass.getDeclaredFields();
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Object[] objects = new Object[fields.length];
        while(res.next()) {
            for (int i = 0; i < objects.length; i++) {
                objects[i] = res.getObject(fields[i].getName());
            }
            list.add(constructor.newInstance(objects));
        }
        return list;
    }
    public List selectByField(Class myClass, Field field, String value) throws NoSuchMethodException,
            SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
        logQ.info(connection.getNameDB() + " Select by field from " +
                myClass.getDeclaredMethod("getTableName").invoke(null));
        ResultSet res;
        if(field.getType().equals(String.class)) {
            res = statement.executeQuery("SELECT * " + "FROM " +
                    myClass.getDeclaredMethod("getTableName").invoke(null) + " WHERE " + field.getName() +
                    " = '" + value + "'");
        }
        else {
            res = statement.executeQuery("SELECT * " + "FROM " +
                    myClass.getDeclaredMethod("getTableName").invoke(null) + " WHERE " + field.getName() +
                    " = " + value);
        }
        List list = new LinkedList();
        Field[] fields = myClass.getDeclaredFields();
        Constructor constructor = myClass.getDeclaredConstructors()[0];
        Object[] objects = new Object[fields.length];
        while(res.next()) {
            for (int i = 0; i < objects.length; i++) {
                objects[i] = res.getObject(fields[i].getName());
            }
            list.add(constructor.newInstance(objects));
        }
        return list;
    }
    private StringBuffer stringMaker(StringBuffer str, IQueryTable table) throws IllegalAccessException {
        for(Field x : table.getClass().getDeclaredFields()) {
            x.setAccessible(true);
            if(x.getType() != String.class) {
                str.append(x.getName() + " = " +
                        x.get(table) + ", ");
            }
            else {
                str.append(x.getName() + " = '" + x.get(table) + "', ");
            }
        }
        str.delete(str.length()-2,str.length());
        return str;
    }
}
