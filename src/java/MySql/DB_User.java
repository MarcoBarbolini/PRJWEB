package MySql;
/**
 *
 * @author Marco
 */
public class DB_User {
    private String uid;
    private String usename;
    private String password;
    private String nome;
    private String cognome;

    public int getFornitore() {
        return fornitore;
    }

    public void setFornitore(int fornitore) {
        this.fornitore = fornitore;
    }
    private int fornitore;

    public String getId() {
        return uid;
    }
    
    public void setId(String uid) {
        this.uid = uid;
    }
    
    public String getUsename() {
        return usename;
    }

    public void setUsername(String usename) {
        this.usename = usename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}
