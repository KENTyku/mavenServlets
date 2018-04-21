/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.showcitieslist;

import com.mycompany.showcitieslist.ShowCities;
import com.mycompany.showcontrieslist.City;
import com.mycompany.showcontrieslist.CountriesTableReader;
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
@WebServlet(name = "ShowCitiesPaging", urlPatterns = {"/ShowCitiesPaging"})
public class ShowCitiesPaging extends ShowCities {
    CountriesTableReader ctr3;

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
            String requestDB=null;//передаваемое в запрос название страны
            requestDB=request.getParameter("country[]");//считывание данных запроса
            
            if(requestDB==null){
                requestDB=request.getParameter("requestDB");//считывание данных запроса
            }            
            int delta;//смещение запрашиваемых значений для sql запроса limit
                        
            delta=Integer.parseInt(request.getParameter("delta"));//считывание данных запроса
            
            //читаем из БД список городов для выбранной страны со смещением delta sql команды limit
            ctr3=new CountriesTableReader();   
            cityList=ctr3.readLimitListCities(requestDB, delta);       
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowCities</title>");            
            out.println("</head>");
            out.println("<body>");            
            
             //выводим на экран то что прочитали         
            for (City itemcity: cityList) {
                out.println("<h1>"+itemcity.getNameCity()+"</h1>");
            }
            out.println("<br>");
            
            out.println("<div>");
            out.println("<div style=\"float: left;\">");
            
            if (delta>0){//условие показа кнопки Назад формы
                //            формируем отправку данных по кнопке Назад
                out.println("<form action=\"ShowCitiesPaging\" method=\"post\">");                    
                out.println("<input name=\"requestDB\" type=\"hidden\" id=\"hidden\" value=\""+requestDB+"\">");
                out.println("<input name=\"delta\" type=\"hidden\" id=\"hidden\" value=\""+(delta-5)+"\">");
                out.println("<input type=\"submit\" value=\"Назад\">");    
                out.println("</form>");
            }
            
            out.println("</div>");            
            out.println("<div style=\"float: right;\"> ");
            
            if (cityList.size()>4){ //условие показа кнопки Вперед формы
    //            формируем отправку данных по кнопке Вперед
                out.println("<form action=\"ShowCitiesPaging\" method=\"post\">");                    
                out.println("<input name=\"requestDB\" type=\"hidden\" id=\"hidden\" value=\""+requestDB+"\">");
                out.println("<input name=\"delta\" type=\"hidden\" id=\"hidden\" value=\""+(delta+5)+"\">");
                out.println("<input type=\"submit\" value=\"Вперед\">");    
                out.println("</form>");
            }
            out.println("</div>");
            out.println("</div>");
            
            if ((cityList.size()<=0) & (delta==0)) {
                out.println("В БД нет ни одного города этой страны");
            } 
            out.println("<br>");
            out.println("<br>");
            out.println("<a href=\"SelectCountryPaging\" > Выбрать другую страну </a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}
