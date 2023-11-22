package com.zimlewis;
import java.sql.*;
import java.util.*;
public class Zql {

    public static ArrayList<Map<String, Object>> excuteQueryToArrayList(Connection connection , String query , ArrayList<String> columns){
        ArrayList<Map<String, Object>> l = new ArrayList<Map<String, Object>>();


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Execute the query and get the ResultSet
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Process the ResultSet
                l = resultSetToArrayList(resultSet, columns);
            }
        }catch (Exception e){

        }

        return l;
    }

    public static ArrayList<Map<String, Object>> resultSetToArrayList(ResultSet rs , ArrayList<String> collumnList){
        ArrayList<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        try{
            while (rs.next()){
                Map<String, Object> m = new HashMap<>();
                for (String i : collumnList){
                    m.put(i, rs.getObject(i));
                }
                l.add(m);
            }
        }
        catch(Exception e){
            
        }
        return l;
    }

    public static Connection getConnection(String url , String username, String password){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return connection;
    }
}
