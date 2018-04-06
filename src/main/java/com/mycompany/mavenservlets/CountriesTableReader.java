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
    private ResultSet rs;
//    private ArrayList<String> countriesArList=new ArrayList<String>();
    ArrayList<Country> countries=new ArrayList<Country>();
    Country country;
    ArrayList<City> cities=new ArrayList<City>();
    City city;
    

    /**
     * Метод чтения списка стран из БД (алгоритм работы) 
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     * @return 
     */

    public ArrayList<Country> readCountries() throws ClassNotFoundException, SQLException {        
        connect();
        readCountriesTable();           
        disconnect();
        return this.countries;
    }
    
    /**
     * Метод чтения списка городов из БД
     * 
     */
    
    public ArrayList<City> readCities(String country) throws ClassNotFoundException, SQLException {
        connect();
        readCitiesTable(country);
        disconnect();
        return this.cities;
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
//                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase?useSSL=no&serverTimezone=UTC","root","123456");
                connection = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11231124?useSSL=no&serverTimezone=UTC","sql11231124","T7UI6DZqye");
                stmt = connection.createStatement();
            }
            catch (SQLException ex) {              
                System.out.println(ex);                
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
            country=new Country();
            country.setName(this.rs.getString(1));
//            country.setCity(city);
//            this.countriesArList.add(this.rs.getString(1));
            this.countries.add(country);
            
        }        
    }

    private void readCitiesTable(String country) throws SQLException {
        this.rs = this.stmt.executeQuery("SELECT  city.city FROM COUNTRY "
                + "inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country='"+country+"';");
        while(this.rs.next()){
            city=new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);            
        }        
        
    }
}
