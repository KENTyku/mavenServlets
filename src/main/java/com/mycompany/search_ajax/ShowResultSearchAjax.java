/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */
package com.mycompany.search_ajax;

import com.mycompany.search.*;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yuri Tveritin
 */
@WebServlet(name = "ShowResultSearchAjax", urlPatterns = {"/ShowResultSearchAjaxServlet"})
public class ShowResultSearchAjax extends ShowCities {

    CountriesTableReader ctr5;
    ArrayList<City> cities = new ArrayList<>();

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
        String requestDB = null;//передаваемое в запросе название страны
        
        //считываем данные из запроса
        requestDB = request.getParameter("id");//получение из запроса         
        //скрипта значения поля формы(введенное пользователем значение)        
        String action = request.getParameter("action");
        int index;//смещение запрашиваемых значений для sql запроса limit
        index = Integer.parseInt(request.getParameter("index"));
        
        StringBuffer sb = new StringBuffer();//для временного сохранения строки (для формирования xml)

        //читаем из БД
        ctr5 = new CountriesTableReader();
        if (requestDB != null) {//если значение не нулевое
            requestDB = requestDB.trim().toLowerCase();//убираем спереди и в конце пробелы, и делаем все буквы прописными
        } else {
//            context.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        //читаем из БД список городов для выбранной страны(стран), начиная с элемента 
        //index sql команды limit
        cities = ctr5.searchCitiesOfCountries(requestDB, index);
        //формируем ответ в виде xml
        boolean requestAdded = false;//запрос еще не сформирован
        if (action.equals("requestComplete")) {//идентифицируем наш запрос из
            //всех возможных запросов
            // проверяем что запрос не пустой
            if (!requestDB.equals("")) {
                //подготавливаем xml данные
                for (City itemcity : cities) {
                    sb.append("<city>");//                      
                    sb.append("<name>" + itemcity.getNameCity() + "</name>");//                       ;
                    sb.append("</city>");
                    requestAdded = true;
                }
            }
            if (requestAdded) {
                //если имена добавлены, то отправляем в ответ на запрос xml строку
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<cities>" + sb.toString() + "</cities>");//xml строка
            } else response.setStatus(HttpServletResponse.SC_NO_CONTENT);//иначе ответ пустой (код 204)            
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
            Logger.getLogger(ShowResultSearchAjax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ShowResultSearchAjax.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ShowResultSearchAjax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ShowResultSearchAjax.class.getName()).log(Level.SEVERE, null, ex);
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
