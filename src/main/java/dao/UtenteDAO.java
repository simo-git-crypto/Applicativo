// src/dao/UtenteDAO.java
package dao;

import model.Utente;
import java.util.List;

/**
 * Interfaccia per la gestione delle operazioni sugli utenti (Utente).
 * Definisce i metodi per aggiungere, recuperare, aggiornare ed eliminare utenti,
 * * oltre a metodi specifici per la ricerca di utenti per ID o nome utente.
 */
public interface UtenteDAO { // Interfaccia per la gestione delle operazioni sugli utenti (Utente)
    /**
     * Aggiunge un nuovo utente al sistema.
     * @param utente L'utente da aggiungere.
     */
    void addUtente(Utente utente);

    /**
     * Recupera un utente dal sistema in base al suo ID o nome utente.
     * @param id L'ID dell'utente da recuperare.
     * @return L'utente corrispondente all'ID specificato, o null se non esiste.
     */
    Utente getUtenteById(int id);
    /**
     * Recupera un utente dal sistema in base al suo nome utente.
     * @param username Il nome utente dell'utente da recuperare.
     * @return L'utente corrispondente al nome utente specificato, o null se non esiste.
     */
    Utente getUtenteByUsername(String username);

    /**
     * Recupera tutti gli utenti dal sistema.
     * @return Una lista di tutti gli utenti presenti nel sistema.
     */
    List<Utente> getAllUtenti();

    /**
     * Aggiorna le informazioni di un utente esistente nel sistema.
     * @param utente L'utente con le informazioni aggiornate da salvare.
     */
    void updateUtente(Utente utente);
    /**
     * Elimina un utente dal sistema in base al suo ID.
     * @param id L'ID dell'utente da eliminare.
     */
    void deleteUtenteById(int id);
}