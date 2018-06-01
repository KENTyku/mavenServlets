/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.showcontrieslist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author kentyku
 */
public class CountriesTableReader {
    Statement stmt;
    PreparedStatement pstmt;
    Connection connection;
    ResultSet rs;
//    private ArrayList<String> countriesArList=new ArrayList<String>();
    ArrayList<Country> countries=new ArrayList<Country>();
    Country country;
    ArrayList<City> cities=new ArrayList<City>();
    City city;
    int sizeListCities;
    
    /**
     * Метод возвращающий количество городов в стране
     * @param country
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    
    public int readSizeListCities(String country) throws ClassNotFoundException, SQLException {
        connect();
        readSizeListRequest(country);
        disconnect();
        return this.sizeListCities;
    }
    
    /**
     * Метод возвращающий лимитированный список городов в стране
     * @param country
     * @param limitShift
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<City> readLimitListCities(String country,int limitShift) throws ClassNotFoundException, SQLException {
        connect();
        readLimitListRequest(country,limitShift);
        disconnect();
        return this.cities;
    }
    
    /**
     * Метод считывающий из БД количество городов в стране
     * @param country 
     */
    private void readSizeListRequest(String country) throws SQLException {
        //готовим запрос
        this.pstmt=connection.prepareStatement("SELECT  count(city.city) "
                + "FROM COUNTRY inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country=?;");
        pstmt.setString(1, country);
        //выполняем запрос
        rs=pstmt.executeQuery();
//        this.rs = this.stmt.executeQuery("SELECT  count(city.city) FROM COUNTRY "
//                + "inner join city on COUNTRY.idcountry=city.idcountry "
//                + "WHERE country='"+country+"';");
        while(this.rs.next()){
            sizeListCities=this.rs.getInt(1);                      
        }        
    }
    /**
     * Метод считывающий из БД ограниченный список городов в стране
     * @param country
     * @param limitShift
     * @throws SQLException 
     */
    private void readLimitListRequest(String country, int limitShift) throws SQLException {
        this.pstmt=connection.prepareStatement("SELECT  city.city FROM COUNTRY "
                + "inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country=? ORDER BY city limit ?,5;");
        pstmt.setString(1, country);
        pstmt.setInt(2, limitShift);
        rs=pstmt.executeQuery();
//        this.rs = this.stmt.executeQuery("SELECT  city.city FROM COUNTRY "
//                + "inner join city on COUNTRY.idcountry=city.idcountry "
//                + "WHERE country='"+country+"' ORDER BY city limit "+limitShift+",5;");
        while(this.rs.next()){
            city=new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);            
        }    
    }    


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
     * Метод поиска списка стран из БД
     * 
     * @param country
     * @return 
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    
    public ArrayList<City> searchCountry(String country) throws ClassNotFoundException, SQLException {
        connect();
        searchCountryDBRequest(country);
        disconnect();
        return this.cities;
    }
    
    void searchCountryDBRequest(String request ) throws SQLException {
        this.rs = this.stmt.executeQuery("SELECT country FROM mydatabase.COUNTRY WHERE country RLIKE '"+request+"' ORDER BY country");
        //обрабатываем результат запроса
        while(this.rs.next()){
            country=new Country();
            country.setName(this.rs.getString(1));
//            country.setCity(city);
//            this.countriesArList.add(this.rs.getString(1));
            this.countries.add(country);
            
        }        
    }

    /**
     * Метод, отвечающий за подключение к БД
     * @throws SQLException, ClassNotFoundException
     */


    void connect() throws ClassNotFoundException {
        try {
                Class.forName("com.mysql.jdbc.Driver");//требуется чтобы 
                /*
                когда используете JDBC не забывайте загружать драйвера таким 
                образом т.к. DriverManager.getConnection() ищет драйверы среди
                загруженных классов, а не пытается загрузить их сам.
                */
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase?useSSL=no&serverTimezone=UTC","root","123456");
//                connection = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11231124?useSSL=no&serverTimezone=UTC","sql11231124","T7UI6DZqye");
                stmt = connection.createStatement();
            }
            catch (SQLException ex) {              
                System.out.println(ex);                
            }
    }

    /**
     * Метод, отвечающий за отключение от БД
     */

    void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    /**
     * Метод, осуществляющий запрос из БД 
     * @throws Exception
     */

    void readCountriesTable() throws SQLException {
        this.rs = this.stmt.executeQuery("SELECT country FROM COUNTRY ORDER BY country;");
        while(this.rs.next()){
            country=new Country();
            country.setName(this.rs.getString(1));
//            country.setCity(city);
//            this.countriesArList.add(this.rs.getString(1));
            this.countries.add(country);
            
        }        
    }
    
    /**
     * Метод, осуществляющий запрос из БД списка городов опредененной страны 
     * 
     */
    void readCitiesTable(String country) throws SQLException {
        //готовим запрос
        this.pstmt=connection.prepareStatement("SELECT  city.city FROM COUNTRY "
                + "inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country=? ORDER BY city;");
        pstmt.setString(1, country);    
        //выполняем запрос
        rs=pstmt.executeQuery();
//        this.rs = this.stmt.executeQuery("SELECT  city.city FROM COUNTRY "
//                + "inner join city on COUNTRY.idcountry=city.idcountry "
//                + "WHERE country='"+country+"' ORDER BY city;");
        while(this.rs.next()){
            city=new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);            
        }        
        
    }
    
    void createdb() throws ClassNotFoundException{
        connect();                  
        disconnect();
        /**
         * 
         * CREATE TABLE `mydatabase`.`city` (
  `idcity` INT NOT NULL AUTO_INCREMENT,
  `city` VARCHAR(45) NOT NULL,
  `idcountry` INT NOT NULL,
  PRIMARY KEY (`idcity`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


INSERT INTO `mydatabase`.`city` (`idcity`, `city`, `idcountry`) VALUES ('1', 'Izhevsk', '1');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Moskow', '1');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Perm', '1');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Ufa', '1');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Ryzan', '1');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Saratov', '1');



ALTER TABLE `mydatabase`.`city` 
ADD INDEX `fk_idcountry_idx` (`idcountry` ASC);
ALTER TABLE `mydatabase`.`city` 
ADD CONSTRAINT `fk_idcountry`
  FOREIGN KEY (`idcountry`)
  REFERENCES `mydatabase`.`country` (`idcountry`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;



INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Barcelona', '2');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Vic', '2');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Madrid', '2');
INSERT INTO `mydatabase`.`city` (`city`, `idcountry`) VALUES ('Palma', '2');



CREATE SCHEMA `countrydb` DEFAULT CHARACTER SET utf8 ;
use countrydb;
CREATE TABLE `country` (
  `idcountry` INT NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idcountry`),
  UNIQUE INDEX `idcountry_UNIQUE` (`idcountry` ASC),
  UNIQUE INDEX `country_UNIQUE` (`country` ASC));

         * 
         */
    }
}
