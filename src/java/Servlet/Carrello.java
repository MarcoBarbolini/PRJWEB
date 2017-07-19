/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Html.Standard_Html;
import MySql.DB_Carrello;
import MySql.DB_Notifica;
import MySql.DB_User;
import MySql.MySqlAccess;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
@WebServlet(name = "Carrello", urlPatterns = {"/Carrello"})
public class Carrello extends HttpServlet {

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
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        String sessione = session.getId();
        String id_articolo = "";
        String uuid = "";
        boolean isCheckOut = false;
        if (request.getParameterMap().containsKey("item")) {
            id_articolo = request.getParameter("item");
        }
        if(session.getAttribute("uuid") != null)
        {
            uuid = session.getAttribute("uuid").toString();
        }
        if (request.getParameterMap().containsKey("out")) {
            isCheckOut = true;
            MySqlAccess db_manager = new MySqlAccess();
            db_manager.checkOut(uuid, sessione);
        }
        boolean isLogged = false;
        DB_User user = new DB_User();
        user.setNome("");
        if(session.getAttribute("uuid") != null)
        {
            if(!"".equals(session.getAttribute("uuid").toString()))
            { 
                isLogged = true;
                MySqlAccess db_manager = new MySqlAccess();
                user = db_manager.get_User_Details(uuid);
            }
        }
        if(!"".equals(id_articolo))
        {
            MySqlAccess db_manager = new MySqlAccess();
            db_manager.aggiungiACarrello(Integer.parseInt(id_articolo), sessione, user.getId());
        }
        
        MySqlAccess db_manager = new MySqlAccess();
        List<DB_Carrello> listaArticoli = db_manager.get_ArticoliCarrello(uuid, sessione);
        //Creazione della pagina
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Standard_Html html_std = new Standard_Html();
            out.println(html_std.getHeader(html_std.PAGINA_CARRELLO, user.getNome(), user.getFornitore()));
            if (isCheckOut)
            {
                out.println("<div class=\"container text-center\">\n" +
                    "    <h4>Grazie per il tuo ordine, torna presto! ;)</h4>\n" +
                    "</div> <br>");
            }
            else if(listaArticoli.isEmpty())
            {
                out.println("<div class=\"container text-center\">\n" +
                    "    <h4>Il tuo carrello è vuoto, fai shopping!</h4>\n" +
                    "</div> <br>");
            }
            else
            {
                double totaleOrdine = 0;
                for(DB_Carrello articolo : listaArticoli)
                {
                    out.println("<div class=\"container\">\n" +
                        "  <div class=\"row\">\n" +
                        "    <div class=\"col-sm-12\">\n" +
                        "      <div class=\"panel panel-warning\">\n" +
                        "        <div class=\"panel-heading\">Articolo 1</div>\n" +
                        "        <div class=\"panel-body\">\n" +
                        "            <div class=\"col-sm-3\">\n" +
                        "            " + articolo.getFornitore() + "\n" +
                        "            </div>\n" +
                        "            <div class=\"col-sm-3\">\n" +
                        "            " + articolo.getArticolo()+ "\n" +
                        "            </div>\n" +
                        "            <div class=\"col-sm-3\">\n" +
                        "            € " + articolo.getPrezzo()+ "\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>" +
                        "</div>");
                    totaleOrdine = totaleOrdine + articolo.getPrezzo();
                }
                out.println("<div class=\"container text-center\">\n" +
                    "    <p>Totale dell'ordine <b>€ " + totaleOrdine + "</b></p><br>\n" +
                    "    <a href=\"./Carrello?out=true><button type=\"button\" class=\"btn btn-primary\" href=\"\">Check Out</button></a>\n" +
                    "</div> <br>");
            }
            out.println(html_std.getFooter());
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
        } catch (SQLException ex) {
            Logger.getLogger(Carrello.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(Carrello.class.getName()).log(Level.SEVERE, null, ex);
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
