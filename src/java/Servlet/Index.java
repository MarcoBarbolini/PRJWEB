/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Html.Standard_Html;
import MySql.DB_Fornitori;
import MySql.DB_Prodotto;
import MySql.DB_User;
import MySql.MySqlAccess;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
@WebServlet(name = "Index", urlPatterns = {"/Index"})
public class Index extends HttpServlet {

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
        boolean isLogged = false;
        DB_User user = new DB_User();
        user.setNome("");
        if(session.getAttribute("uuid") != null)
        {
            if(!"".equals(session.getAttribute("uuid").toString()))
            { 
                isLogged = true;
                MySqlAccess db_manager = new MySqlAccess();
                user = db_manager.get_User_Details(session.getAttribute("uuid").toString());
            }
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Standard_Html html_std = new Standard_Html();
            out.println(html_std.getHeader(html_std.PAGINA_HOME, user.getNome(), user.getFornitore()));
            out.println("<div class=\"container text-center\">\n" +
                "    <h1>Tutto quello che cerchi in un solo posto</h1>      \n" +
                "    <p>\"Founded in 1996, luxury Swedish cosmetics giant Make Up Store, currently houses over 200 stores worldwide! With over 1200 products available, we are known as a colour brand with strong high quality pigments... What you see is what you get! We proudly create our own unique formulas and develop them in the best quality factories in the USA, Canada, Italy, and throughout Europe. Our range is hypo-allergenic, fragrance free, allergy friendly, and we proudly NEVER test on animals. Originating from a Makeup Academy (The IMC), our products were originally designed for makeup artists. With the core of our business being education, our team consist of highly skilled professional makeup artists available to provide expert tips and advice. Welcome to our \"World Of Colours\"!  </p><br><br><br>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"container\">    \n" +
                "  <div class=\"row\">\n" +
                "    <div class=\"col-sm-6\">\n" +
                "      <div class=\"panel panel-primary\">\n" +
                "        <div class=\"panel-heading\">RICERCA PRODOTTO</div>\n" +
                "        <div class=\"panel-body\">\n" +
                "        <form name=\"formLike\" action=\"/PRJ165773/Product\" >\n"+
                "            <input type=\"text\"class=\"form-control\" name=\"like\" size=\"50\" placeholder=\"Inserisci il nome del prodotto\"><br>\n" +
                "            <div class=\"btn-group btn-group-justified\" role=\"group\" aria-label=\"...\">\n" +
                "                <div class=\"btn-group\" role=\"group\">\n" +
                "                    <button type=\"submit\" class=\"btn btn-primary\">Ricerca</button>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </form>"+
                "        </div>\n" +
                "        <div class=\"panel-footer\">Ricerca fra migliaia di prodotti dei più svariati negozi.</div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "<div class=\"col-sm-6\">\n" +
                "      <div class=\"panel panel-primary\">\n" +
                "        <div class=\"panel-heading\">RICERCA I PRODOTTI DEL TUO BRAND PREFERITO</div>\n" +
                "        <div class=\"panel-body\">\n" +
                "            <div class=\"btn-group btn-group-justified\" role=\"group\" aria-label=\"...\">\n" +
                "                <div class=\"btn-group\" role=\"group\">\n" +
                "                    <button class=\"btn btn-primary dropdown-toggle\" type=\"button\" id=\"dropdownMenu2\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                "                        Seleziona uno dei nostri brand\n" +
                "                        <span class=\"caret\"></span>\n" +
                "                      </button>\n" +
                "                    <ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenu1\">\n");
            MySqlAccess db_manager = new MySqlAccess();
            List<DB_Fornitori> listaFornitori= db_manager.get_Fornitori();
            for(DB_Fornitori fornitore : listaFornitori) 
            {
                out.println("                        <li><a href=\"./Product?fornitore=" + fornitore.getId() + "\">" + fornitore.getFornitore() + "</a></li>\n");
            }
            out.println("                    </ul>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"panel-footer\">Sei affezzionato ad una marca e vuoi vedere tutti i suoi prodotti a tua disposizione?</div>\n" +
                "      </div>\n" +
                "    </div>" +
                "  </div>\n" +
                "</div><br>");
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
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
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
