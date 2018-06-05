/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.search;

import com.mycompany.select.ShowCities;
import com.mycompany.showcontrieslist.City;
import com.mycompany.showcontrieslist.CountriesTableReader;
import com.mycompany.showcontrieslist.Country;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
@WebServlet(name = "ShowResultSearch", urlPatterns = {"/ShowResultSearch"})
public class ShowResultSearch extends ShowCities {
    CountriesTableReader ctr5;    
    ArrayList<City> cities=new ArrayList<City>();

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
        String requestDB;
        //обработка GET  запроса формы поиска
//        requestDB=request.getParameter("country");
        requestDB=null;//передаваемое в запрос название страны
            requestDB=request.getParameter("country");//считывание данных запроса
            
            if(requestDB==null){
                requestDB=request.getParameter("requestDB");//считывание данных запроса
            }      
        ctr5=new CountriesTableReader();
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>ShowRelustSearch</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ShowCities at " + request.getContextPath() + "</h1>");
//            out.println("<br>");
////            out.println(requestDB);
//            
//             //выводим на экран то что прочитали         
//            for (City itemcity: cities) {
//                out.println("<h1>"+itemcity.getNameCity()+"</h1>");
//            }
//            out.println("</body>");
//            out.println("</html>");
//            
            int delta;//смещение запрашиваемых значений для sql запроса limit
                        
            delta=Integer.parseInt(request.getParameter("delta"));//считывание данных запроса
            
            //читаем из БД список городов для выбранной страны со смещением delta sql команды limit
            cities=ctr5.searchCitiesOfCountries(requestDB,delta);
                 
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowCities</title>");            
            out.println("</head>");
            out.println("<body>");            
            
             //выводим на экран то что прочитали         
            for (City itemcity: cities) {
                out.println("<h1>"+itemcity.getNameCity()+"</h1>");
            }
            out.println("<br>");
            
            out.println("<div>");
            out.println("<div style=\"float: left;\">");
            
            if (delta>0){//условие показа кнопки Назад формы
                //            формируем отправку данных по кнопке Назад
                out.println("<form action=\"ShowResultSearch\" method=\"post\">");                    
                out.println("<input name=\"requestDB\" type=\"hidden\" id=\"hidden\" value=\""+requestDB+"\">");
                out.println("<input name=\"delta\" type=\"hidden\" id=\"hidden\" value=\""+(delta-5)+"\">");
                out.println("<input type=\"submit\" value=\"Назад\">");    
                out.println("</form>");
            }
            
            out.println("</div>");            
            out.println("<div style=\"float: right;\"> ");
            
            if (cities.size()>4){ //условие показа кнопки Вперед формы
    //            формируем отправку данных по кнопке Вперед
                out.println("<form action=\"ShowResultSearch\" method=\"post\">");                    
                out.println("<input name=\"requestDB\" type=\"hidden\" id=\"hidden\" value=\""+requestDB+"\">");
                out.println("<input name=\"delta\" type=\"hidden\" id=\"hidden\" value=\""+(delta+5)+"\">");
                out.println("<input type=\"submit\" value=\"Вперед\">");    
                out.println("</form>");
            }
            out.println("</div>");
            out.println("</div>");
            
            if ((cities.size()<=0) & (delta==0)) {
                out.println("В БД нет ни одной страны, соответствующей запросу");
            } 
            out.println("<br>");
            out.println("<br>");
            out.println("<a href=\"SearchCountryPaging\" > Выбрать другую страну </a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ShowResultSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ShowResultSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ShowResultSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ShowResultSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
