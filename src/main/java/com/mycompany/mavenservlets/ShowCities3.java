/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.mavenservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
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
    CountriesTableReader3 ctr3;
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
            String requestDB=null;//передаваемое в запрос название страны
            requestDB=request.getParameter("country[]");//передача данных запроса
            int delta=0;//смещение запрашиваемых значений для sql запроса limit
            
            //читаем из БД список городов для выбранной страны
            ctr3=new CountriesTableReader3();   
            cityList=ctr3.readLimitListCities(requestDB, delta);
      
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowCities3</title>");            
            out.println("</head>");
            out.println("<body>");
//            out.println("pages="+pages+"");
            out.println("<br>");
            
             //выводим на экран то что прочитали         
            for (City itemcity: cityList) {
                out.println("<h1>"+itemcity.getNameCity()+"</h1>");
            }
            out.println("<br>");
            out.println("<div style=\"float: right;\"> ");
            if (cityList.size()>0){
    //            формируем отправку данных по кнопке вперед
                out.println("<form action=\"ShowCitiesNext3\" method=\"post\">");                    
                out.println("<input name=\"requestDB\" type=\"hidden\" id=\"hidden\" value=\""+requestDB+"\">");
                out.println("<input name=\"delta\" type=\"hidden\" id=\"hidden\" value=\""+(delta+5)+"\">");
                out.println("<input type=\"submit\" value=\"Вперед\">");    
                out.println("</form>");
            }            
            out.println("</div>");
            if (cityList.size()<=0) {
                out.println("В БД нет ни одного города этой страны");
            } 
            out.println("<br>");
            out.println("<br>");
            out.println("<a href=\"SimplePaging3\" > Выбрать другую страну </a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
