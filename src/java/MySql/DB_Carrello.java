/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MySql;

/**
 *
 * @author Marco
 */
public class DB_Carrello {

    public String getFornitore() {
        return fornitore;
    }

    public String getArticolo() {
        return articolo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public void setArticolo(String articolo) {
        this.articolo = articolo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    private String fornitore;
    private String articolo;
    private double prezzo;
    
}
