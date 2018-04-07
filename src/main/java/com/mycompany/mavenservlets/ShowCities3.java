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
@WebServlet(name = "ShowCities3", urlPatterns = {"/ShowCities3"})
public class ShowCities3 extends ShowCities2 {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String requestDB;
            requestDB=request.getParameter("country[]");//передача данных запроса
            //читаем из БД список городов для выбранной страны
            ctr=new CountriesTableReader();   
            cityList=ctr.readCities(requestDB);
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowCities2</title>");            
            out.println("</head>");
            out.println("<body>");
//            out.println("<h1>Servlet ShowCities2 at " + request.getContextPath() + "</h1>");
            out.println("<br>");
            
             //выводим на экран то что прочитали         
            for (City itemcity: cityList) {
                out.println("<h1>"+itemcity.getNameCity()+"</h1>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
}
