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
 * Класс чтения из БД
 *
 * @author kentyku
 */
public class CountriesTableReader {

    Statement stmt;
    PreparedStatement pstmt;
    Connection connection;
    ResultSet rs;
    ArrayList<Country> countries = new ArrayList<Country>();
    Country country;
    ArrayList<City> cities = new ArrayList<City>();
    City city;
    int sizeListCities;

    /**
     * Метод возвращающий количество городов в стране
     *
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
     * Метод возвращающий частичный список городов в стране (с определенного
     * индекса)
     *
     * @param country
     * @param index
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ArrayList<City> readLimitListCities(String country, int index) throws ClassNotFoundException, SQLException {
        connect();
        readLimitListRequest(country, index);
        disconnect();
        return this.cities;
    }

    /**
     * Метод считывающий из БД количество городов в стране
     *
     * @param country
     */
    private void readSizeListRequest(String country) throws SQLException {
        //готовим запрос
        this.pstmt = connection.prepareStatement("SELECT  count(city.city) "
                + "FROM country inner join city on COUNTRY.idcountry=city.idcountry "
                + "WHERE country=?;");
        pstmt.setString(1, country);
        //выполняем запрос
        rs = pstmt.executeQuery();

        while (this.rs.next()) {
            sizeListCities = this.rs.getInt(1);
        }
    }

    /**
     * Метод чтения из БД частичного списка городов в стране (с определенного
     * индекса)
     *
     * @param country
     * @param index
     * @throws SQLException
     */
    private void readLimitListRequest(String country, int index) throws SQLException {
        this.pstmt = connection.prepareStatement("SELECT  city.city FROM country "
                + "inner join city on country.idcountry=city.idcountry "
                + "WHERE country=? ORDER BY city limit ?,5;");
        pstmt.setString(1, country);
        pstmt.setInt(2, index);
        rs = pstmt.executeQuery();

        while (this.rs.next()) {
            city = new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);
        }
    }

    /**
     * Метод чтения списка стран из БД (алгоритм работы)
     *
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
     * @param country
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ArrayList<City> readCities(String country) throws ClassNotFoundException, SQLException {
        connect();
        readCitiesTable(country);
        disconnect();
        return this.cities;
    }

    /**
     * Метод поиска списка городов (для стран, удовлетворяющих условию поиска)
     * из БД
     *
     * @param country
     * @param index
     * @return
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public ArrayList<City> searchCitiesOfCountries(String country, int index) throws ClassNotFoundException, SQLException {
        connect();
        searchCitiesOfCountriesDBRequest(country, index);
        disconnect();
        return this.cities;
    }

    /**
     * Метод поиска списка городов (для стран, удовлетворяющих условию поиска)
     * из БД. Запрос выводит по пять элементов с определенного индекса
     *
     * @param request
     * @param index
     * @throws SQLException
     */
    void searchCitiesOfCountriesDBRequest(String request, int index) throws SQLException {
        this.pstmt = connection.prepareStatement("SELECT  city FROM country "
                + "INNER JOIN city on country.idcountry=city.idcountry "
                + "WHERE country RLIKE ? ORDER BY country "
                + "limit ?,5;");
        pstmt.setString(1, request);
        pstmt.setInt(2, index);
        rs = pstmt.executeQuery();
        //обрабатываем результат запроса
        this.cities.clear();
        while (this.rs.next()) {
            city = new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);
        }
    }

    /**
     * Метод, отвечающий за подключение к БД
     *
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
//                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mdb?useSSL=no&serverTimezone=UTC","root","123456");
                connection = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7251503?useSSL=no&serverTimezone=UTC","sql7251503","1Jx7N5vPQS");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase?useSSL=no&serverTimezone=UTC", "root", "123456");
            stmt = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Метод, отвечающий за отключение от БД
     */
    void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    /**
     * Метод, считывающий из БД список стран
     *
     * @throws Exception
     */
    void readCountriesTable() throws SQLException {
        this.rs = this.stmt.executeQuery("SELECT country FROM country ORDER BY country;");
        while (this.rs.next()) {
            country = new Country();
            country.setName(this.rs.getString(1));
            this.countries.add(country);

        }
    }

    /**
     * Метод, осуществляющий запрос из БД списка городов опредененной страны
     *
     * @param country
     * @throws SQLException
     */
    void readCitiesTable(String country) throws SQLException {
        //готовим запрос
        this.pstmt = connection.prepareStatement("SELECT  city.city FROM country "
                + "inner join city on country.idcountry=city.idcountry "
                + "WHERE country=? ORDER BY city;");
        pstmt.setString(1, country);
        //выполняем запрос
        rs = pstmt.executeQuery();

        while (this.rs.next()) {
            city = new City();
            city.setNameCity(this.rs.getString(1));
            this.cities.add(city);
        }

    }
}
