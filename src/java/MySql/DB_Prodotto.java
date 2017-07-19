/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MySql;

import java.util.List;

/**
 *
 * @author Marco
 */
public class DB_Prodotto {

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getId_fornitore() {
        return id_fornitore;
    }

    public String getFornitore() {
        return fornitore;
    }

    public int getRating() {
        return rating;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setId_fornitore(int id_fornitore) {
        this.id_fornitore = id_fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }
   
    public void setImmagini(List<String> immagini) {
        this.immagini = immagini;
    }

    public List<String> getImmagini() {
        return immagini;
    }
    
    private int id;
    private String nome;
    private String descrizione;
    private int id_fornitore;
    private String fornitore;
    private int rating;
    private double prezzo;
    private List<String> immagini;

}
