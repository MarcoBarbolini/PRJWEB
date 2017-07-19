/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Html.Standard_Html;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import MySql.DB_User;
import MySql.DB_Notifica;
import MySql.MySqlAccess;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marco
 */
@WebServlet(name = "User", urlPatterns = {"/User"})
public class User extends HttpServlet {

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
        boolean isBadLogin = false;
        boolean alredyExist = false;
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
        String login = request.getParameter("login");
        if(login != null)
        {
            if("bad".equals(login))
            {isBadLogin = true;}
            else if("exist".equals(login))
            {alredyExist = true;}
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Standard_Html html_std = new Standard_Html();
            out.println(html_std.getHeader(html_std.PAGINA_UTENTE, user.getNome(), user.getFornitore()));
            if(isLogged)
            {
                MySqlAccess db_manager = new MySqlAccess();
                List<DB_Notifica> notifiche = null;
                notifiche = db_manager.get_Notifiche(session.getAttribute("uuid").toString());
                if(notifiche.isEmpty()){
                    out.println("<div class=\"container text-center\">\n" +
                    "    <h4>Nessuna notifica!</h4>\n" +
                    "</div> <br>");
                }
                else
                {
                    out.println("<div class=\"container\">    \n");
                    for(DB_Notifica notifica : notifiche) {
                        out.println("  <div class=\"row\">\n" +
                        "    <div class=\"col-sm-12\">\n" +
                        "      <div class=\"panel panel-warning\">\n" +
                        "        <div class=\"panel-heading\"><a href=\"./NotificaDelete?notifica=" + notifica.getId() + "\"><button type=\"button\" class=\"btn btn-warning\" href=\"\">Chiudi</button></a>"+
                        "        Notifica</div>\n" +
                        "        <div class=\"panel-body\">\n" +
                        "        " + notifica.getDescrizione() + "\n");
                        if(notifica.getId_prodotto() != 0)
                        {
                            out.println("<br>\n" +
                                "        Valuta il prodotto: \n" +
                                "        <a href=\"./RateProduct?product=" + notifica.getId_prodotto() + "&rate=1&notifica=" + notifica.getId() + "\"><button type=\"button\" class=\"btn btn-warning\" href=\"\">*</button></a>\n" +
                                "        <a href=\"./RateProduct?product=" + notifica.getId_prodotto() + "&rate=2&notifica=" + notifica.getId() + "\"><button type=\"button\" class=\"btn btn-warning\" href=\"\">**</button></a>\n" +
                                "        <a href=\"./RateProduct?product=" + notifica.getId_prodotto() + "&rate=3&notifica=" + notifica.getId() + "\"><button type=\"button\" class=\"btn btn-warning\" href=\"\">***</button></a>\n" +
                                "        <a href=\"./RateProduct?product=" + notifica.getId_prodotto() + "&rate=4&notifica=" + notifica.getId() + "\"><button type=\"button\" class=\"btn btn-warning\" href=\"\">****</button></a>\n" +
                                "        <a href=\"./RateProduct?product=" + notifica.getId_prodotto() + "&rate=5&notifica=" + notifica.getId() + "\"><button type=\"button\" class=\"btn btn-warning\" href=\"\">*****</button></a>");
                        }
                        out.println("        </div>\n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>\n");
                    }
                    out.println("</div><br>");
                }
                out.println("<div class=\"container text-center\">\n" +
                "    <p>Grazie della tua visita, torna presto.</p>\n" +
                "    <a href=\"./LogOut\"><button type=\"button\" class=\"btn btn-primary\">Log Out</button></a>\n" +
                "</div> <br>\n" +
                "    ");
            }
            else
            {
                out.println("<div class=\"container\">    \n" +
                "  <div class=\"row\">\n" +
                "    <div class=\"col-sm-6\">\n" +
                "      <div class=\"panel panel-primary\">\n" +
                "        <div class=\"panel-heading\">LOGIN</div>\n" +
                "        <div class=\"panel-body\">\n" +
                "        <script>\n" +
                "            function validateSignIn() {\n" +
                "                var x = document.forms[\"formLogIn\"][\"Username\"].value;\n" +
                "                    if (x == \"\") { alert(\"Inserire un email\"); return false;\n" +
                "                }\n" +
                "                x = document.forms[\"formLogIn\"][\"Password\"].value;\n" +
                "                    if (x == \"\") { alert(\"Inserire una password\"); return false;\n" +
                "                }\n" +
                "            }\n" +
                "        </script>\n" +
                "        <form name=\"formLogIn\" action=\"/PRJ165773/LogIn\" onsubmit=\"return validateLogIn()\">\n");
                if(isBadLogin)
                {
                    out.println("            <p style=\"color:red;\">Email o Password non validi</p>");
                }
                out.println("            <input type=\"text\"class=\"form-control\" name=\"Username\" placeholder=\"Email\"><br>\n" +
                "            <input type=\"password\"class=\"form-control\" name=\"Password\" placeholder=\"Password\"><br>\n" +
                "            <div class=\"btn-group btn-group-justified\" role=\"group\" aria-label=\"...\">\n" +
                "                <div class=\"btn-group\" role=\"group\">\n" +
                "                    <input type=\"submit\" class=\"btn btn-primary\" value=\"Log In\">\n" +
                "                </div>\n" +
                "            </div>  \n" +
                "        </form>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>" +
                "    <div class=\"col-sm-6\">\n" +
                "      <div class=\"panel panel-primary\">\n" +
                "        <div class=\"panel-heading\">SIGN IN</div>\n" +
                "        <div class=\"panel-body\">\n" +
                "            <script>\n" +
                "                function validateSignIn() {\n" +
                "                    var x = document.forms[\"formSignIn\"][\"Nome\"].value;\n" +
                "                        if (x == \"\") { alert(\"Inserire un nome\"); return false;\n" +
                "                    }\n" +
                "                    x = document.forms[\"formSignIn\"][\"Cognome\"].value;\n" +
                "                        if (x == \"\") { alert(\"Inserire un cognome\"); return false;\n" +
                "                    }\n" +
                "                    x = document.forms[\"formSignIn\"][\"Username\"].value;\n" +
                "                        if (x == \"\") { alert(\"Inserire una mail valida\"); return false;}\n" +
                "                        if (validateEmail(x) == false)\n" +
                "                        {\n" +
                "                            alert(\"Inserire una mail valida\"); return false;\n" +
                "                        }\n" +
                "                    x = document.forms[\"formSignIn\"][\"Password\"].value;\n" +
                "                    var y = document.forms[\"formSignIn\"][\"Password repeat\"].value;\n" +
                "                        if (x != y | x == \"\") { alert(\"Le password non corrispondono\"); return false;\n" +
                "                    }\n" +
                "                    if (! document.forms[\"formSignIn\"][\"Privacy\"].checked) { alert(\"Accettare le normative sulla privacys\"); return false; }\n" +
                "                }\n" +
                "                function validateEmail(email) {\n" +
                "                    var re = /^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/;\n" +
                "                    return re.test(email);\n" +
                "                }" +               
                "            </script>" +
                "            <form name=\"formSignIn\" action=\"/PRJ165773/SignIn\" onsubmit=\"return validateSignIn()\">\n");
                if(alredyExist)
                {
                    out.println("            <p style=\"color:red;\">Utente già presente</p>");
                }
                out.println("                <input type=\"text\"class=\"form-control\" name=\"Nome\" placeholder=\"Nome\"><br>\n" +
                "                <input type=\"text\"class=\"form-control\" name=\"Cognome\" placeholder=\"Cognome\"><br>\n" +
                "                <input type=\"text\"class=\"form-control\" name=\"Username\" placeholder=\"Email\"><br>\n" +
                "                <input type=\"password\"class=\"form-control\" name=\"Password\" placeholder=\"Password\"><br>\n" +
                "                <input type=\"password\"class=\"form-control\" name=\"Password repeat\" placeholder=\"Password repeat\"><br>\n" +
                "                <div class=\"checkbox\">\n" +
                "                    <label><input type=\"checkbox\" value=\"\" name=\"Privacy\">Accetto le normative sulla privacy.</label> <br><br>\n" +
                "                </div>\n" +
                "                <div class=\"btn-group btn-group-justified\" role=\"group\" aria-label=\"...\">\n" +
                "                    <div class=\"btn-group\" role=\"group\">\n" +
                "                        <input type=\"submit\" class=\"btn btn-primary\" value=\"Sign In\">\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div><br>");
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
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
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
