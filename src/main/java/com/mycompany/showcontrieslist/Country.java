/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.showcontrieslist;

import java.util.ArrayList;

/**
 *
 * @author kentyku
 */
public class Country {
    private String name;
    private ArrayList<City> cityList;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cityList
     */
    public ArrayList<City> getCityList() {
        return cityList;
    }

    /**
     * @param cityList the cityList to set
     */
    public void setCityList(ArrayList<City> cityList) {
        this.cityList = cityList;
    }    
    
    
}
