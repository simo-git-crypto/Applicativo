package model;
import java.util.ArrayList;
import java.util.List;
/** * Questa classe rappresenta l'utente della piattaforma.
 * Contiene informazioni come l'id, il nome utente, la password e una lista di ToDo associati all'utente.
 * La classe fornisce metodi per accedere e modificare queste informazioni.
 */
public class Utente {
    private int id; // campo id aggiunto
    private String username;
    private String password;
    private List<ToDo> todos;

    /**
     * Costruttore che serve a  creare un nuovo utente.
     * Questo metodo inizializza i campi username e password dell'utente,
     * creando una lista vuota di ToDo associati all'utente.
     * @param username Rappresenta nickname dell'utente.
     * @param password La password dell'utente.
     */
    public Utente(String username, String password) {

        this.username = username;
        this.password = password;
        this.todos = new ArrayList<>();
    }

    /**
     * Questo metodo restituisce l'id dell'utente.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere.
     * @return id dell'utente
     */
    public int getId() {


        return id;
    }


    /**
     * Questo metodo imposta l'id dell'utente.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo setter per poterlo assegnare.
@param id rappresenta l'id dell'utente
     */
    public void setId(int id) {


        this.id = id;
    }

    /**
     * Questo metodo restituisce il nome utente dell'utente.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return nome utente dell'utente
     */
    public String getUsername() {

        return username;
    }

    /**
     * Questo metodo restituisce la password dell'utente.
     * E' necessario poichè, essendo la password un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterla leggere.
     * @return password dell'utente
     */
    public String getPassword() {

        return password;
    }
}