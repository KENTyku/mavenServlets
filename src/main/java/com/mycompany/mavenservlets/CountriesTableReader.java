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
     * Основной алгоритм работы нити
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */

    public ArrayList<String> readData() throws ClassNotFoundException, SQLException {        
        connect();
//        ArrayList<String> temp=new ArrayList<String>();
        this.countriesArList.addAll(readCountriesTable());           
        disconnect();
        return this.countriesArList;
    }

    /**
     * Метод, отвечающий за подключение к БД
     * @throws SQLException, ClassNotFoundException
     */

//    private void connect() throws SQLException, ClassNotFoundException {
//        Class.forName("org.sqlite.JDBC");
//        this.connection = DriverManager.getConnection("jdbc:sqlite:GEEKBRAINS_INTERNSHIP_15_1_DB.db");
//        this.stmt = connection.createStatement();
//    }
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
     * Метод, осуществляющий запрос из БД ключевых слов по которым ведется
     * парсинг html страниц
     * @throws Exception
     */

    private ArrayList<String> readCountriesTable() throws SQLException {
//        this.countriesList = new TreeMap<>();
//        this.countriesArList=new ArrayList<String>();
//        ArrayList<String> temp=new ArrayList<String>();
        this.rs = this.stmt.executeQuery("SELECT country FROM COUNTRY;");
        while(this.rs.next()){
//            this.countriesList.put(this.rs.getInt(1), this.rs.getString(2));//treemap: ключ=id страны, значение= название страны
            this.countriesArList.add(this.rs.getString(1));
        }
        return this.countriesArList;
    }
//    /*
//    *Метод возвращающий коллекцию стран
//    */
//    public TreeMap <Integer, String> getCountriesList(){
//        return this.countriesList;
//    }
}
