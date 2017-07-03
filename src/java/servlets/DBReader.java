/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author katharina
 */
@WebServlet(name = "DBReader", urlPatterns = {"/DBReader"})
public class DBReader extends HttpServlet {
    private String dbURL = "jdbc:derby://localhost:1527/middleware;create=true;user=middleware;password=middleware";

    private Connection conn = null;

    private Statement stmt = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String date = request.getParameter("date");
            
            String t_date = date+" 23:59:00";
            
            
            try {
                String sql = "SELECT * FROM tax_record WHERE create_date <='"+t_date+"'";
                conn = DriverManager.getConnection(dbURL);
                stmt = conn.createStatement();
                ResultSet r_set = stmt.executeQuery(sql);
                
                List<String> res = new ArrayList<>();
                
                while(r_set.next()){
                    
                    String obj = "{";
                    
                    Timestamp time = r_set.getTimestamp("create_date");
                    double total = r_set.getDouble("total");
                    String currency = r_set.getString("currency");
                    double amount = r_set.getDouble("amount");
                    double tax = r_set.getDouble("tax");
                    
                    obj+= "\"time\":\""+time.toString()+"\",";
                    obj+="\"amount\":"+amount+",";
                    obj+="\"tax\":"+tax+",";
                    obj+="\"currency\":\""+currency+"\",";
                    obj+="\"total\":"+total+"}";
                    res.add(obj);
                }
                conn.close();
             
                String out_obj = "[";
                
                for(String entry : res){
                    out_obj+=entry+","; 
                }
                
                out_obj = out_obj.substring(0, out_obj.length()-1);
                out_obj+="]";
                
                out.print(out_obj);
                
            } catch (SQLException ex) {
                Logger.getLogger(DBReader.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            
            
            
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
        processRequest(request, response);
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
        processRequest(request, response);
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
