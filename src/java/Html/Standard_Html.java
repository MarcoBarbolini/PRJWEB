/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Html;

/**
 *
 * @author Marco
 */
public class Standard_Html {
    public final String PAGINA_HOME = "Home";
    public final String PAGINA_PRODOTTI = "Prodotti";
    public final String PAGINA_UTENTE = "Utente";
    public final String PAGINA_CARRELLO = "Carrello";
    public String getHeader(String focusPage, String userName, int venditore)
    {
        String nomePaginaUtente = "Login";
        String nomeTabProdotti = "Top 10 - Prodotti";
        boolean isVenditore = false;
        if(venditore != 0)
        {
            isVenditore = true;
            nomeTabProdotti = "Gestione prodotti";
        }
        if(userName != "")
        {
            nomePaginaUtente = "Ciao, " + userName;
        }
        String menu_home = "        <li><a href=\"./Index\">Home</a></li>\n";
        String menu_prodotti = "        <li><a href=\"./Product\">" + nomeTabProdotti + "</a></li>\n";
        String menu_utente = "        <li><a href=\"./User\"><span class=\"glyphicon glyphicon-user\"></span> " + nomePaginaUtente + "</a></li>\n";
        String menu_carrello = "        <li><a href=\"./Carrello\"><span class=\"glyphicon glyphicon-shopping-cart\"></span> Carrello</a></li>\n";
        switch(focusPage){
            case PAGINA_HOME:
                menu_home = "        <li class=\"active\"><a href=\"./Index\">Home</a></li>\n";
                break;
            case PAGINA_PRODOTTI:
                menu_prodotti = "        <li class=\"active\"><a href=\"./Product\">" + nomeTabProdotti + "</a></li>\n";
                break;
            case PAGINA_UTENTE:
                menu_utente = "        <li class=\"active\"><a href=\"./User\"><span class=\"glyphicon glyphicon-user\"></span> " + nomePaginaUtente + "</a></li>\n";
                break;
            case PAGINA_CARRELLO:
                menu_carrello = "        <li class=\"active\"><a href=\"./Carrello\"><span class=\"glyphicon glyphicon-shopping-cart\"></span> Carrello</a></li>\n";
                break;
        }
        if(isVenditore){menu_carrello=""; menu_home = "";}
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <title>Make You Up Store</title>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
            "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
            "  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
            "  <style>\n" +
            "    /* Remove the navbar's default rounded borders and increase the bottom margin */ \n" +
            "    .navbar {\n" +
            "      margin-bottom: 50px;\n" +
            "      border-radius: 0;\n" +
            "    }\n" +
            "    \n" +
            "    /* Remove the jumbotron's default bottom margin */ \n" +
            "     .jumbotron {\n" +
            "      padding-bottom: 0px;\n" +
            "      padding-top: 0px;\n" +
            "      background-color: black;\n" +
            "      margin-bottom: 0;\n" +
            "    }\n" +
            "   \n" +
            "    /* Add a gray background color and some padding to the footer */\n" +
            "    footer {\n" +
            "      background-color: #f2f2f2;\n" +
            "      padding: 25px;\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>" +
            "<body>" +
            "<div class=\"jumbotron\">\n" +
            "  <div class=\"container text-center\" >\n" +
            "    <img src=\"ImageServlet/header.jpg\">\n" +
            "  </div>\n" +
            "</div>" +
            "<nav class=\"navbar navbar-inverse\">\n" +
            "  <div class=\"container-fluid\">\n" +
            "    <div class=\"navbar-header\">\n" +
            "      <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#myNavbar\">\n" +
            "        <span class=\"icon-bar\"></span>\n" +
            "        <span class=\"icon-bar\"></span>\n" +
            "        <span class=\"icon-bar\"></span>                        \n" +
            "      </button>\n" +
            "    </div>\n" +
            "    <div class=\"collapse navbar-collapse\" id=\"myNavbar\">\n" +
            "      <ul class=\"nav navbar-nav\">\n" +
            menu_home +
            menu_prodotti +
            "      </ul>\n" +
            "      <ul class=\"nav navbar-nav navbar-right\">\n" +
            menu_utente +
            menu_carrello +
            "      </ul>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</nav>";
    }
    
    public String getFooter(){
        return "<footer class=\"container-fluid text-center\">\n" +
            "  <p>Make You Up Store Copyright</p>\n" +
            "</footer>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
    }
}


