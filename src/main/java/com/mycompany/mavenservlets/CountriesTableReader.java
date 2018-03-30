/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.mavenservlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author kentyku
 */
public class CountriesTableReader {
    private Statement stmt;
    private Connection connection;
//    private TreeMap <Integer, String> countriesList;
    private ResultSet rs;
    private ArrayList<String> countriesArList=new ArrayList<String>();
//   public ArrayList<String> temp=new ArrayList<String>();
    /**
     * Основной алгоритм работы 
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */

    public ArrayList<String> readData() throws ClassNotFoundException, SQLException {        
        connect();
        readCountriesTable();           
        disconnect();
        return this.countriesArList;
    }

    /**
     * Метод, отвечающий за подключение к БД
     * @throws SQLException, ClassNotFoundException
     */


    private void connect() throws ClassNotFoundException {
        try {
                Class.forName("com.mysql.jdbc.Driver");//требуется чтобы 
                /*
                когда используете JDBC не забывайте загружать драйвера таким 
                образом т.к. DriverManager.getConnection() ищет драйверы среди
                загруженных классов, а не пытается загрузить их сам.
                */
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase?useSSL=no&serverTimezone=UTC","root","123456");
                stmt = connection.createStatement();
            }
            catch (SQLException ex) {
                System.out.println("***************");
                System.out.println(ex);
                System.out.println("***************");
            }
    }

    /**
     * Метод, отвечающий за отключение от БД
     */

    private void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    /**
     * Метод, осуществляющий запрос из БД 
     * @throws Exception
     */

    private void readCountriesTable() throws SQLException {
        this.rs = this.stmt.executeQuery("SELECT country FROM COUNTRY;");
        while(this.rs.next()){
            this.countriesArList.add(this.rs.getString(1));
        }        
    }
//    /*
//    *Метод возвращающий коллекцию стран
//    */
//    public TreeMap <Integer, String> getCountriesList(){
//        return this.countriesList;
//    }
}
