//Il ruolo del Controller è quello di fungere da intermediario tra la logica di business (model) e l'interfaccia utente (view).

// src/controller/Controller.java
package controller; //definisce il package del Controller

import model.*; //importa tutte le classi del package model
import dao.*; //importa tutte le classi del package dao
import java.util.logging.Logger;
import java.util.ArrayList; //importa la classe ArrayList
import java.util.List; //Importa l'interfacccia List
import java.util.logging.Logger;
/** * Questa classe Controller funge da intermediario tra il model e la GUI.
 * Gestisce le operazioni relative agli utenti e alle bacheche,
 * come login, registrazione, creazione di ToDo e gestione delle bacheche.
 */
public class Controller { //Definizione della classe Controller
    private final BoardManager boardManager; //Gestore delle bacheche
    private final UtenteManager utenteManager; //Gestore degli utenti  (dipendenza da UtenteManager)
    private Utente utenteLoggato; //Utente appena loggato
    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    public Controller(UtenteManager utenteManager, BoardManager boardManager) { //Costruttore del Controller
        this.utenteManager = utenteManager; //Inizializza il gestore degli utenti this.utenteManager indica il campo della classe  = utenteManager // parametro del costruttore
        this.boardManager = boardManager; //Inizializza il gestore delle bacheche
    }

    /**
     * Questo metodo imposta l'utente loggato nel Controller.
     * * Serve per tenere traccia dell'utente attualmente autenticato nell'applicazione.
     * E' necessario poichè, essendo l'utente loggato un campo privato,
     * non è accessibile direttamente con il semplice costruttore,
     * @param utente l'utente da impostare come loggato.
     */
    public void setUtenteLoggato(Utente utente) { //Metodo per assegnare l'utente loggato
        this.utenteLoggato = utente; //imposta l'utente loggato
    }

    /**
     * Questo metodo restituisce l'utente loggato nel Controller.
     * Serve per ottenere l'utente attualmente autenticato nell'applicazione.
     * E' necessario poichè, essendo l'utente loggato un campo privato,
     * non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere.
     * @return l'utente loggato
     */
    public Utente getUtenteLoggato() { //Metodo per restituire il campo privato utenteLoggato
        return utenteLoggato; //restituisce l'utente loggato
    }

    /**
     * Questo metodo serve per ottenere un utente in base al nome utente.
     *@param username il nome utente da cercare
     * @return l'istanza di Utente corrispondente al nome utente specificato,
     */

    public Utente getUserByUsername(String username) { //Metodo per ottenere un utente in base al nome utente
        UtenteDAO utenteDAO = utenteManager.getUtenteDAO(); //Ottiene l'istanza di UtenteDAO dal gestore degli utenti
        return utenteDAO.getUtenteByUsername(username);// Restituisce l'utente corrispondente al nome utente
    }



    /**
     * Questo metodo serve per verificare le credenziali dell'utente.
     * * Utilizza il gestore degli utenti per verificare se le credenziali fornite sono corrette.
     * @param login il nome utente da verificare
     * @param password la password da verificare
     * @return true se le credenziali sono corrette, altrimenti false
     */
    public boolean verifyCredentials(String login, String password) { //Metodo per verificare le credenziali dell'utente
        return utenteManager.verifyCredentials(login, password);// Restituisce true se le credenziali sono corrette, altrimenti false
    }

    /**
     * Questo metodo serve per aggiungere una bacheca.
     * * Utilizza il gestore delle bacheche per aggiungere una nuova bacheca all'applicazione.
     * @param board la bacheca da aggiungere
     */
    public void addBoard(Board board) { //Metodo per aggiungere una bacheca
        boardManager.addBoard(board); // Aggiunge la bacheca tramite il gestore delle bacheche
    }

 /** Questo metodo serve per creare un nuovo ToDo.
     * * Utilizza il gestore delle bacheche per aggiungere un nuovo ToDo alla bacheca specificata.
     * @param todo il ToDo da creare
     */
    public void createToDo(ToDo todo) { //Metodo per creare un nuovo ToDo
        boardManager.addToDo(todo); // Aggiunge il ToDo tramite il gestore delle bacheche
    }

    /** * Questo metodo serve per ottenere una lista di bacheche in base al nome dell'utente e della bacheca.
     * * Utilizza il gestore delle bacheche per ottenere tutte le bacheche associate all'utente specificato.
     * * @param userId l'ID dell'utente di cui si vogliono ottenere le bacheche
     * @param boardName il nome della bacheca da cercare
     * @return una lista di bacheche associate all'utente
     */

    public Board getBoardByNameAndUser(String boardName, int userId) { //Metodo per ottenere una bacheca in base al nome e all'ID dell'utente
        return boardManager.getBoardByTitoloAndUtente(boardName, userId); // Restituisce la bacheca corrispondente al nome e all'ID dell'utente
    }

    /** Questo metodo serve per otttenere una lista di ToDo associati a una bacheca.
     * * Utilizza il gestore delle bacheche per ottenere tutti i ToDo associati alla bacheca specificata.
     * @param board la bacheca di cui si vogliono ottenere i ToDo
     * @return una lista di ToDo associati alla bacheca
     */
    public List<ToDo> getToDo(Board board) { //Metodo per ottenere la lista dei ToDo associati a una bacheca
        if (board == null) {// Controlla se la bacheca è null
            logger.severe("Errore: il Board passato è null.");
            return new ArrayList<>();// Restituisce una lista vuota se la bacheca è null
        }
        return boardManager.getToDosForBoard(board.getId()); // Restituisce la lista dei ToDo associati alla bacheca
    }
/** Questo metodo serve a segnare un ToDo come completato.
     * * Utilizza il gestore delle bacheche per aggiornare lo stato del ToDo specificato a COMPLETATO.
     * @param todo il ToDo da segnare come completato
     */
    public void markAsCompleted(ToDo todo) { //Metodo per segnare un ToDo come completato
        todo.setStato(StatoToDo.COMPLETATO); // Imposta lo stato del ToDo a COMPLETATO
        boardManager.updateToDo(todo); // Save this modification in the DB
    }

    /** Questo metodo serve per aggiornare un ToDo esistente.
     * * Utilizza il gestore delle bacheche per aggiornare le informazioni del ToDo specificato.
     * @param todo il ToDo da aggiornare
     */
    public void updateToDo(ToDo todo) { //Metodo per aggiornare un ToDo
        boardManager.updateToDo(todo);// Aggiorna il ToDo tramite il gestore delle bacheche
    }



    /**
     * Questo metodo serve per eliminare un ToDo esistente.
      * Utilizza il gestore delle bacheche per rimuovere il ToDo specificato dalla bacheca.
     * @param todo il ToDo da eliminare
     */
    public void deleteToDo(ToDo todo) { //Metodo per eliminare un ToDo
        boardManager.deleteToDo(todo); // Elimina il ToDo tramite il gestore delle bacheche
    }

    /**
     * Questo metodo serve per ottenere il gestore delle bacheche.
     * * Serve per accedere alle operazioni relative alle bacheche nell'applicazione.
     * E' necessario poichè, essendo il gestore delle bacheche un campo privato,
     * non è accessibile direttamente con il semplice costruttore,
     * quindi serve un metodo getter per poterlo leggere.
     * @return il gestore delle bacheche
     */
    public BoardManager getBoardController() { //Metodo per ottenere il gestore delle bacheche
        return this.boardManager; // Restituisce il gestore delle bacheche
    }

    /**
     * Questo metodo serve per segnare tutti i ToDo di una bacheca come completati.
     * * Utilizza il gestore delle bacheche per aggiornare lo stato di tutti i ToDo della bacheca specificata a COMPLETATO.
     * @param boardId l'ID della bacheca di cui si vogliono segnare i ToDo come completati
     */
    public void markAllToDosAsCompletedByBoardId(int boardId) { //Metodo per segnare tutti i ToDo di una bacheca come completati
        boardManager.markAllToDosAsCompletedByBoardId(boardId); // Segna tutti i ToDo della bacheca con l'ID specificato come completati
    }
}