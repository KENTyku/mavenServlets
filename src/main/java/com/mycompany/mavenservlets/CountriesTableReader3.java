/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.mavenservlets;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author kentyku
 */
public class CountriesTableReader3 extends CountriesTableReader {
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
        this.rs = this.stmt.executeQuery("SELECT  count(city.city) FROM COUNTRY "
                + "inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country='"+country+"';");
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
        this.rs = this.stmt.executeQuery("SELECT  city.city FROM COUNTRY "
                + "inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country='"+country+"' ORDER BY city limit "+limitShift+",5;");
        while(this.rs.next()){
            city=new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);            
        }    
    }
}
