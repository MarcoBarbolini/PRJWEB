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
public class DB_Fornitori {

    public String getFornitore() {
        return fornitore;
    }

    public int getId() {
        return id;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String fornitore;
    private int id;
}
