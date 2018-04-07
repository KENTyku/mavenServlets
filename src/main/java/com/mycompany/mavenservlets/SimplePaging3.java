/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.mavenservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kentyku
 */
@WebServlet(name = "SimplePaging3", urlPatterns = {"/SimplePaging3"})
public class SimplePaging3 extends SelectCountry2 {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SelectCountry2</title>");            
            out.println("</head>");
            out.println("<body>");
//            out.println("<h1>Servlet SelectCountry2 at " + request.getContextPath() + "</h1>");
            out.println("<br>");
            out.println("<br>");
            
            //читаем из БД список стран
            ctr=new CountriesTableReader();   
            countriesList=ctr.readCountries();  
            
            //формируем  выпадающий список
            out.println("<form action=\"ShowCities3\" method=\"post\">");
            out.println("<p><select size=\""+(countriesList.size()+1)+"\" multiple name=\"country[]\">");  
            out.println("<option disabled>Выберите страну</option>");                    
            
            for (Country itemcountry: countriesList){
                out.println("<option value=\""+itemcountry.getName()+"\">"+itemcountry.getName()+"</option>");     
            }                 
            out.println("</select>");    
            out.println("<input type=\"submit\" value=\"Выбрать\"></p>");    
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}
