/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Html.Standard_Html;
import MySql.DB_Prodotto;
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
@WebServlet(name = "Product", urlPatterns = {"/Product"})
public class Product extends HttpServlet {

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
        Standard_Html html_std = new Standard_Html();
        String focusPage = html_std.PAGINA_PRODOTTI;
        boolean isLogged = false;
        boolean isFornitore = false;
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
        if(user.getFornitore() != 0)
        {
            int fornitore = user.getFornitore();
            isFornitore = true;
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            MySqlAccess db_manager = new MySqlAccess();
            List<DB_Prodotto> listaProdotti;
            if(isFornitore)
            {
                listaProdotti = db_manager.get_ProdottiFornitore(user.getFornitore());
            }
            else
            {
                if (request.getParameterMap().containsKey("fornitore")) {
                    listaProdotti = db_manager.get_ProdottiFornitore(Integer.parseInt(request.getParameter("fornitore")));
                    focusPage = "";
                }
                else if (request.getParameterMap().containsKey("like")) 
                {
                    listaProdotti = db_manager.get_ProdottiLike(request.getParameter("like"));
                    focusPage = "";
                }
                else
                {
                    listaProdotti = db_manager.get_Prodotti();
                }
            }
            out.println(html_std.getHeader(focusPage, user.getNome(), user.getFornitore()));
            int contatoreCaroselli = 0;
            out.println("<div class=\"container\"> \n");
            if(isFornitore)
            {
                out.println("<div class=\"row\">\n" +
                    "        <div class=\"col-cm-12\">\n" +
                    "            <div class=\"panel panel-primary\">\n" +
                    "                <div class=\"panel-heading\">INSERISCI UN NUOVO PRODOTTO</div>\n" +
                    "                <div class=\"panel-body\">\n" +
                    "                    <div class=\"col-sm-12\">\n" +
                    "                        <center>\n" +
                    "                            <form action=\"/PRJ165773/NewProdotto\" enctype=\"multipart/form-data\">\n" +
                    "                                <table border=\"0\">\n" +
                    "                                    <tr>\n" +
                    "                                        <td>Nome prodotto: </td>\n" +
                    "                                        <td><input type=\"text\" class=\"form-control\" name=\"nome\" size=\"50\"/></td>\n" +
                    "                                    </tr>\n" +
                    "                                    <tr>\n" +
                    "                                        <td>Descrizione prodotto: </td>\n" +
                    "                                        <td><input type=\"text\" class=\"form-control\" name=\"descrizione\" size=\"50\"/></td>\n" +
                    "                                    </tr>\n" +
                    "                                    <tr>\n" +
                    "                                        <td>Prezzo: </td>\n" +
                    "                                        <td><input type=\"number\" class=\"form-control\" name=\"prezzo\" size=\"50\"/></td>\n" +
                    "                                    </tr>\n" +
                    "                                    <tr>\n" +
                    "                                        <td><input type=\"hidden\" class=\"form-control\" name=\"fornitore\" value=\"" + user.getFornitore()    + "\"" + user.getFornitore() + "\" size=\"50\"/></td>" +
                    "                                    </tr>\n" +
                    "                                    <tr>\n" +
                    "                                        <td colspan=\"2\">\n" +
                    "                                            <center><input type=\"submit\" class=\"btn btn-primary\" value=\"Save\"></center>\n" +
                    "                                        </td>\n" +
                    "                                    </tr>\n" +
                    "                                </table>\n" +
                    "                            </form>\n" +
                    "                        </center>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div><br>");
            }
            for(DB_Prodotto prodotto : listaProdotti) {
                contatoreCaroselli++;
                List<String> fotoProdotti = prodotto.getImmagini();
                out.println("<div class=\"row\">\n" +
                    "        <div class=\"col-cm-12\">\n" +
                    "            <div class=\"panel panel-primary\">\n" +
                    "                <div class=\"panel-heading\">" + prodotto.getNome().toUpperCase() + "</div>\n" +
                    "                <div class=\"panel-body\">\n" +
                    "                    <div class=\"col-sm-4\">\n" +
                    "                        <div class=\"panel-body\">\n" +
                    "                            <div id=\"myCarousel" + contatoreCaroselli + "\" class=\"carousel slide\" data-ride=\"carousel\">\n" +
                    "                                  <!-- Indicators -->\n" +
                    "                                  <ol class=\"carousel-indicators\">\n");
                int contatore = 0;
                for(String foto : fotoProdotti)
                {
                    out.println("<li data-target=\"#myCarousel" + contatoreCaroselli + "\" data-slide-to=\"" + contatore + "\" class=\"active\"></li>\n");
                    contatore++;
                }
                out.println("                                  </ol>\n" +
                    "                                  <!-- Wrapper for slides -->\n" +
                    "                                  <div class=\"carousel-inner\">\n"); 
                boolean primaFoto = true;
                for(String foto : fotoProdotti)
                {
                    if(primaFoto)
                    {
                        out.println("<div class=\"item active\">\n");
                    }
                    else
                    {
                        out.println("<div class=\"item\">\n");
                    }
                    out.println("<img src=\"ImageServlet/" + foto + "\" alt=\"Nessuna foto\">\n" +
                        "</div>\n");
                    primaFoto = false;
                }
                out.println("</div>\n" +
                    "                                  <!-- Left and right controls -->\n" +
                    "                                  <a class=\"left carousel-control\" href=\"#myCarousel" + contatoreCaroselli + "\" data-slide=\"prev\">\n" +
                    "                                    <span class=\"glyphicon glyphicon-chevron-left\"></span>\n" +
                    "                                    <span class=\"sr-only\">Previous</span>\n" +
                    "                                  </a>\n" +
                    "                                  <a class=\"right carousel-control\" href=\"#myCarousel" + contatoreCaroselli + "\" data-slide=\"next\">\n" +
                    "                                    <span class=\"glyphicon glyphicon-chevron-right\"></span>\n" +
                    "                                    <span class=\"sr-only\">Next</span>\n" +
                    "                                  </a>\n" +
                    "                                </div>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"col-sm-8\">\n" +
                    "                        <div class=\"panel-body\">\n"); 
                out.println(prodotto.getDescrizione());
                out.println("                        </div>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"col-sm-7\">\n" +
                    "                        <div class=\"panel-body\">\n" +
                    "                            Venduto da: " + prodotto.getFornitore() + "\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"col-sm-3\">\n" +
                    "                        <div class=\"panel-body\">\n" +
                    "                            Rating: \n");
                int rating = prodotto.getRating();
                if(rating > 0){out.println("<div class=\"glyphicon glyphicon-star\"> </div>\n");}
                    else{out.println("<div class=\"glyphicon glyphicon-star-empty\"></div>\n");}
                if(rating > 1){out.println("<div class=\"glyphicon glyphicon-star\"> </div>\n");}
                    else{out.println("<div class=\"glyphicon glyphicon-star-empty\"></div>\n");}
                if(rating > 2){out.println("<div class=\"glyphicon glyphicon-star\"> </div>\n");}
                    else{out.println("<div class=\"glyphicon glyphicon-star-empty\"></div>\n");}
                if(rating > 3){out.println("<div class=\"glyphicon glyphicon-star\"> </div>\n");}
                    else{out.println("<div class=\"glyphicon glyphicon-star-empty\"></div>\n");}
                if(rating > 4){out.println("<div class=\"glyphicon glyphicon-star\"> </div>\n");}
                    else{out.println("<div class=\"glyphicon glyphicon-star-empty\"></div>\n");}
                out.println("<br>\n" +
                    "                            Prezzo: <span class=\"label label-primary\">€ " + prodotto.getPrezzo() + "</span>\n" +
                    "                        </div>\n" +
                    "                    </div>\n");
                if(isFornitore)
                {
                    out.println("<div class=\"col-sm-2\">\n" +
                        "                        <div class=\"panel-body\">\n" +
                        "                            <form method=\"post\" action=\"/PRJ165773/ImageServletUpload\" enctype='multipart/form-data'>\n" +
                        "                                <table border=\"0\">\n" +
                        "                                    <tr>\n" +
                        "                                        <td>Foto:</td>\n" +
                        "                                        <td><input type=\"file\" name=\"photo\" size=\"50\"/></td>\n" +
                        "                                    </tr>\n" +
                        "                                    <tr>\n" +
                        "                                        <td><input type=\"hidden\" name=\"prodotto\" value=\"" + prodotto.getId() + "\"/></td>" +
                        "                                    </tr>\n" +
                        "                                    <tr>\n" +
                        "                                        <td colspan=\"2\">\n" +
                        "                                            <input type=\"submit\" class=\"btn btn-primary\" value=\"Salva\">\n" +
                        "                                        </td>\n" +
                        "                                    </tr>\n" +
                        "                                </table>\n" +
                        "                            </form>\n" +
                        "                        </div>\n" +
                        "                    </div>");
                }
                else
                {
                    out.println("                    <div class=\"col-sm-2\">\n" +
                        "                        <div class=\"panel-body\">\n" +
                        "                            <a href=\"./Carrello?item=" + prodotto.getId() + "\"><button type=\"button\" class=\"btn btn-primary\">Aggiungi al carrello</button></a>\n" +
                        "                        </div>\n" +
                        "                    </div>\n");
                }
                out.println("                </div>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div><br>\n");
            }
            out.println("</div>");
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
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
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
