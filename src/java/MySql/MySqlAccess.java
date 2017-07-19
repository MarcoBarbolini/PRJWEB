package MySql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Marco
 */
public class MySqlAccess {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public MySqlAccess() throws SQLException {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/web?user=root&password=laspo");
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe.getMessage(), cnfe.getCause());
        }
    }

    public String insert_User(String nome, String cognome, String utente, String password) throws SQLException {
        try (PreparedStatement stm = connect.prepareStatement("select count(uuid) from users where username = ?")) {
            stm.setString(1, utente);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                int presente = rs.getInt(1);
                stm.close();
                if (presente > 0) {
                    connect.close();
                    return "Presente";
                }
            }
        }
        try (PreparedStatement stm = connect.prepareStatement("insert into users (uuid, username, password, name, lastname) values (uuid(), ?,?,?,?)")) {
            stm.setString(1, utente);
            stm.setString(2, password);
            stm.setString(3, nome);
            stm.setString(4, cognome);
            stm.executeUpdate();
            stm.close();
        }
        try (PreparedStatement stm = connect.prepareStatement("SELECT uuid FROM users WHERE username = ?")) 
        {
            stm.setString(1, utente);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                String uuid = rs.getString("uuid");
                stm.close();
                connect.close();
                return uuid;
            }
        }
    }

    public void active_User(String uuid) throws SQLException {
        try (PreparedStatement stm = connect.prepareStatement("update users set attivo = 'S' where uuid = ?")) {
            stm.setString(1, uuid);
            stm.executeUpdate();
            stm.close();
            connect.close();
        }
    }

    public String login_User(String username, String password) throws SQLException {
        String uuid = "";
        try (PreparedStatement stm = connect.prepareStatement("SELECT uuid FROM users WHERE username = ? AND password = ? AND attivo = 'S'")) {
            stm.setString(1, username);
            stm.setString(2, password);
            try (ResultSet rs = stm.executeQuery()) {
                if (!rs.wasNull()) {
                    rs.next();
                    uuid = rs.getString("uuid");
                }
            } catch (Exception exc) {
                return uuid;
            }
            stm.close();
            connect.close();
        }
        return uuid;
    }

    public DB_User get_User_Details(String uuid) throws SQLException {
        DB_User user = new DB_User();
        try (PreparedStatement stm = connect.prepareStatement("SELECT name, lastname, username, venditore FROM users WHERE uuid = ?")) {
            stm.setString(1, uuid);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                user.setId(uuid);
                user.setNome(rs.getString("name"));
                user.setCognome(rs.getString("lastname"));
                user.setUsername(rs.getString("username"));
                user.setFornitore(rs.getInt("venditore"));
            }
            stm.close();
            connect.close();
        }
        return user;
    }

    public List<DB_Notifica> get_Notifiche(String uuid) throws SQLException {
        List<DB_Notifica> ritorno = new ArrayList();
        try (PreparedStatement stm = connect.prepareStatement("SELECT id, descrizione, id_prodotto FROM notifiche WHERE uuid = ?")) {
            stm.setString(1, uuid);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    DB_Notifica notifica = new DB_Notifica();
                    notifica.setId(rs.getInt("id"));
                    notifica.setDescrizione(rs.getString("descrizione"));
                    notifica.setId_prodotto(rs.getInt("id_prodotto"));
                    ritorno.add(notifica);
                }
            }
            stm.close();
            connect.close();
        }
        return ritorno;
    }

    public byte[] getImmagine(String nome) throws SQLException {
        byte[] ritorno = null;
        try (PreparedStatement stm = connect.prepareStatement("SELECT content FROM immagini WHERE nome = ?")) {
            stm.setString(1, nome);
            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                ritorno = rs.getBytes("content");
            }
            stm.close();
            connect.close();
        }
        return ritorno;
    }
    
    public void setImmagine(InputStream foto, String idProdotto) throws SQLException
    {
        try (PreparedStatement stm = connect.prepareStatement("INSERT INTO immagini(id_prodotto, content, nome) VALUES (?, ?, uuid())")) {
            stm.setString(1, idProdotto);
            stm.setBlob(2, foto);
            stm.executeUpdate();
            stm.close();
            connect.close();
        }
    }
    
    public List<DB_Prodotto> get_Prodotti() throws SQLException {
        List<DB_Prodotto> ritorno = new ArrayList();
        try (PreparedStatement stm = connect.prepareStatement("select p.id, p.nome, p.descrizione, p.id_fornitore, f.descrizione as 'fornitore', p.rating, p.prezzo \n" +
            "from prodotti p join fornitori f on (p.id_fornitore = f.id) order by p.rating DESC, p.prezzo DESC limit 10")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    DB_Prodotto prodotto = new DB_Prodotto();
                    prodotto.setId(rs.getInt("id"));
                    prodotto.setNome(rs.getString("nome"));
                    prodotto.setDescrizione(rs.getString("descrizione"));
                    prodotto.setId_fornitore(rs.getInt("id_fornitore"));
                    prodotto.setFornitore(rs.getString("fornitore"));
                    prodotto.setRating(rs.getInt("rating"));
                    prodotto.setPrezzo(rs.getDouble("prezzo"));
                    try (PreparedStatement stmFoto = connect.prepareStatement("select i.nome from immagini i where i.id_prodotto = ?")) {
                        stmFoto.setInt(1, prodotto.getId());
                        try (ResultSet rsFoto = stmFoto.executeQuery()) {
                            List<String> foto = new ArrayList();
                            while (rsFoto.next()) {
                                foto.add(rsFoto.getString("nome"));
                            }
                            prodotto.setImmagini(foto);
                        }
                    }
                    ritorno.add(prodotto);
                }
            }
            stm.close();
            connect.close();
        }
        return ritorno;
    }
    
    public List<DB_Prodotto> get_ProdottiFornitore(int id_fornitore) throws SQLException {
        List<DB_Prodotto> ritorno = new ArrayList();
        try (PreparedStatement stm = connect.prepareStatement("select p.id, p.nome, p.descrizione, p.id_fornitore, f.descrizione as 'fornitore', p.rating, p.prezzo \n" +
            "from prodotti p join fornitori f on (p.id_fornitore = f.id) where p.id_fornitore = ? order by p.rating DESC, p.prezzo DESC limit 10")) {
            stm.setInt(1, id_fornitore);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    DB_Prodotto prodotto = new DB_Prodotto();
                    prodotto.setId(rs.getInt("id"));
                    prodotto.setNome(rs.getString("nome"));
                    prodotto.setDescrizione(rs.getString("descrizione"));
                    prodotto.setId_fornitore(rs.getInt("id_fornitore"));
                    prodotto.setFornitore(rs.getString("fornitore"));
                    prodotto.setRating(rs.getInt("rating"));
                    prodotto.setPrezzo(rs.getDouble("prezzo"));
                    try (PreparedStatement stmFoto = connect.prepareStatement("select i.nome from immagini i where i.id_prodotto = ?")) {
                        stmFoto.setInt(1, prodotto.getId());
                        try (ResultSet rsFoto = stmFoto.executeQuery()) {
                            List<String> foto = new ArrayList();
                            while (rsFoto.next()) {
                                foto.add(rsFoto.getString("nome"));
                            }
                            prodotto.setImmagini(foto);
                        }
                    }
                    ritorno.add(prodotto);
                }
            }
            stm.close();
            connect.close();
        }
        return ritorno;
    }
    
    public List<DB_Prodotto> get_ProdottiLike(String like) throws SQLException {
        List<DB_Prodotto> ritorno = new ArrayList();
        try (PreparedStatement stm = connect.prepareStatement("select p.id, p.nome, p.descrizione, p.id_fornitore, f.descrizione as 'fornitore', p.rating, p.prezzo \n" +
            "from prodotti p join fornitori f on (p.id_fornitore = f.id) where p.nome like ? order by p.rating DESC, p.prezzo DESC limit 20")) {
            stm.setString(1, "%" + like + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    DB_Prodotto prodotto = new DB_Prodotto();
                    prodotto.setId(rs.getInt("id"));
                    prodotto.setNome(rs.getString("nome"));
                    prodotto.setDescrizione(rs.getString("descrizione"));
                    prodotto.setId_fornitore(rs.getInt("id_fornitore"));
                    prodotto.setFornitore(rs.getString("fornitore"));
                    prodotto.setRating(rs.getInt("rating"));
                    prodotto.setPrezzo(rs.getDouble("prezzo"));
                    try (PreparedStatement stmFoto = connect.prepareStatement("select i.nome from immagini i where i.id_prodotto = ?")) {
                        stmFoto.setInt(1, prodotto.getId());
                        try (ResultSet rsFoto = stmFoto.executeQuery()) {
                            List<String> foto = new ArrayList();
                            while (rsFoto.next()) {
                                foto.add(rsFoto.getString("nome"));
                            }
                            prodotto.setImmagini(foto);
                        }
                    }
                    ritorno.add(prodotto);
                }
            }
            stm.close();
            connect.close();
        }
        return ritorno;
    }

    public void aggiungiACarrello(int id_prodotto, String sessione, String uuid) throws SQLException {
        boolean isLogged = false;
        try (PreparedStatement stm = connect.prepareStatement("insert into carrello (uuid, sessione, id_prodotto) values (?,?,?)")) {
            stm.setString(1, uuid);
            if("".equals(uuid) | uuid == null)
            {
                stm.setString(2, sessione);
            }
            else
            {
                isLogged = true;
                stm.setString(2, null);
            }
            stm.setInt(3, id_prodotto);
            stm.executeUpdate();
            stm.close();
        }
        if(isLogged)
        {
            try (PreparedStatement stm = connect.prepareStatement("UPDATE carrello SET uuid = ?, sessione = null WHERE sessione = ?")) {
                stm.setString(1, uuid);
                stm.setString(2, sessione);
                stm.executeUpdate();
                stm.close();
            }
        }
        connect.close(); 
    }
    
    public List<DB_Carrello> get_ArticoliCarrello(String uuid, String sessione) throws SQLException {
        List<DB_Carrello> ritorno = new ArrayList();
        if("".equals(uuid) | uuid == null)
        {
            try (PreparedStatement stm = connect.prepareStatement("select fornitori.descrizione, prodotti.nome, prodotti.prezzo \n" +
                "from carrello \n" +
                "join prodotti on (carrello.id_prodotto = prodotti.id)\n" +
                "join fornitori on (prodotti.id_fornitore = fornitori.id)\n" +
                "where carrello.sessione = ?")) {
                stm.setString(1, sessione);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        DB_Carrello carrello = new DB_Carrello();
                        carrello.setFornitore(rs.getString("descrizione"));
                        carrello.setArticolo(rs.getString("nome"));
                        carrello.setPrezzo(rs.getDouble("prezzo"));
                        ritorno.add(carrello);
                    }
                }
                stm.close();
                connect.close();
            }
        }
        else{
            try (PreparedStatement stm = connect.prepareStatement("select fornitori.descrizione, prodotti.nome, prodotti.prezzo \n" +
                "from carrello \n" +
                "join prodotti on (carrello.id_prodotto = prodotti.id)\n" +
                "join fornitori on (prodotti.id_fornitore = fornitori.id)\n" +
                "where carrello.`uuid` = ? or carrello.sessione = ?")) {
                stm.setString(1, uuid);
                stm.setString(2, sessione);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        DB_Carrello carrello = new DB_Carrello();
                        carrello.setFornitore(rs.getString("descrizione"));
                        carrello.setArticolo(rs.getString("nome"));
                        carrello.setPrezzo(rs.getDouble("prezzo"));
                        ritorno.add(carrello);
                    }
                }
                stm.close();
                connect.close();
            }
        }
        return ritorno;
    }
    
    public void checkOut(String uuid, String sessione) throws SQLException {
        if("".equals(uuid) | uuid == null)
        {
            try (PreparedStatement stm = connect.prepareStatement("delete from carrello where sessione = ?")) {
                stm.setString(1, sessione);
                stm.executeUpdate();
                stm.close();
                connect.close();
            }
        }
        else{
            try (PreparedStatement stm = connect.prepareStatement("delete from carrello where `uuid` = ? or sessione = ?")) {
                stm.setString(1, uuid);
                stm.setString(2, sessione);
                stm.executeUpdate();
                stm.close();
                connect.close();
            }
        }
    }
    
    public void newProdotto(String nome, String descrizione, String prezzo, String fornitore) throws SQLException {
        try (PreparedStatement stm = connect.prepareStatement("insert into prodotti (nome, descrizione, id_fornitore, rating, prezzo) values (?,?,?,0,?)")) {
            stm.setString(1, nome);
            stm.setString(2, descrizione);
            stm.setString(3, fornitore);
            stm.setString(4, prezzo);
            stm.executeUpdate();
            stm.close();
            connect.close();
        }
    }
    
    public void deleteNotifica(String id_notifica) throws SQLException {
        try (PreparedStatement stm = connect.prepareStatement("delete from notifiche where id = ?")) {
                stm.setString(1, id_notifica);
                stm.executeUpdate();
                stm.close();
                connect.close();
        }
    }
    
    public void rateProduct(String prodotto, String rate, String id_notifica) throws SQLException {
        try (PreparedStatement stm = connect.prepareStatement("insert into rating (id_prodotto, rating) values (?,?)")) {
                stm.setString(1, prodotto);
                stm.setString(2, rate);
                stm.executeUpdate();
                stm.close();
        }
        try (PreparedStatement stm = connect.prepareStatement("update notifiche set id_prodotto = 0 where id = ?")) {
                stm.setString(1, id_notifica);
                stm.executeUpdate();
                stm.close();
                connect.close();
        }
    }
    
    public List<DB_Fornitori> get_Fornitori() throws SQLException {
        List<DB_Fornitori> ritorno = new ArrayList();
        try (PreparedStatement stm = connect.prepareStatement("SELECT id, descrizione FROM fornitori")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    DB_Fornitori fornitore = new DB_Fornitori();
                    fornitore.setId(rs.getInt("id"));
                    fornitore.setFornitore(rs.getString("descrizione"));
                    ritorno.add(fornitore);
                }
            }
            stm.close();
            connect.close();
        }
        return ritorno;
    }
        
    }
    
