package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe rappresenta una bacheca che contiene una lista di ToDo.
 * Ogni bacheca ha un titolo, un ID e un ID utente associato.
 */
public class Board {
    private int id;
    private String titolo;
    private String username;
    private int idUtente;
    private final List<ToDo> todos;

    /**
     * Costruttore che serve a creare una nuova bacheca.
     * @param titolo Il titolo della bacheca.
     * @param idUtente L'ID dell'utente proprietario della bacheca.
     * @param id L'ID della bacheca.
     */

    public Board(int id, String titolo, int idUtente) {
        this.id = id;
        this.titolo = titolo;
        this.idUtente = idUtente;
        this.todos = new ArrayList<>();
    }

   /**
     * Questo metodo restituisce l'ID della bacheca.
     * E' necessario poichè, essendo l'ID un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere.
     * @return ID della bacheca
     */
    public int getId() {
        return id;
    }

    /**
     * Questo metodo imposta l'ID della bacheca.
     * E' necessario poichè, essendo l'ID un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo setter per poterlo assegnare.
     * @param id L'ID della bacheca da impostare.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Questo metodo restituisce il titolo della bacheca.
     * E' necessario poichè, essendo il titolo un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere
     */
    public String getTitolo() {
        return titolo;
    }


    /**
     * Questo metodo imposta il titolo della bacheca.
     * E' necessario poichè, essendo il titolo un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo setter per poterlo assegnare.
     * @param titolo Il titolo della bacheca da impostare.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }



    /**
     * Questo metodo restituisce la lista dei ToDo associati alla bacheca.
     * E' necessario poichè, essendo la lista di ToDo un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterla leggere.
     * @return Lista dei ToDo associati alla bacheca
     */
    public List<ToDo> getTodos() {
        return todos;
    }

    /**
     * Questo metodo imposta il nome utente associato alla bacheca.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo setter per poterlo assegnare.
     * @param username Il nome utente da associare alla bacheca.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Questo metodo restituisce il nome utente associato alla bacheca.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere.
     * @return Nome utente associato alla bacheca
     */
    public String getUsername() {
        return username;
    }

    /**
     * Questo metodo restituisce l'ID dell'utente associato alla bacheca.
     * E' necessario poichè, essendo l'ID utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere.
     * @return ID dell'utente associato alla bacheca
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Questo metodo imposta l'ID dell'utente associato alla bacheca.
     * E' necessario poichè, essendo l'ID utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo setter per poterlo assegnare.
     * @param idUtente L'ID dell'utente da associare alla bacheca.
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}