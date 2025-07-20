// src/model/UtenteManager.java
package model;
import controller.Controller;
import dao.UtenteDAO;

import java.util.logging.Logger;


/**
 * Questa classe consente di gestire le operazioni relative agli utenti,
 */
public class UtenteManager {
    private Controller controller;
    private UtenteDAO utenteDAO;
    private Utente utenteLoggato;
    private static final Logger LOGGER = Logger.getLogger(UtenteManager.class.getName());




    /**
     *Questo metodo è il costruttore della classe UtenteManager.
     * @param utenteDAO l'istanza di UtenteDAO che gestisce le operazioni sugli utenti
     */
    public UtenteManager(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
       LOGGER.info("Istanza UtenteManager: " + this.hashCode());
    }



    /**
     * Questo metodo restituisce l'istanza di UtenteDAO associata allo specifico utente.
     * @return l'istanza di UtenteDAO
     */
    public UtenteDAO getUtenteDAO() {
        return this.utenteDAO;
    }


    /** Questo metodo verifica le credenziali dell'utente.
     * @param username il nome utente da verificare
     * @param password la password da verificare
     */
    public boolean verifyCredentials(String username, String password) {

        LOGGER.info("Verify su istanza: " + this.hashCode());
        LOGGER.info("Verifica credenziali: " + username + " / " + password);
        Utente utente = utenteDAO.getUtenteByUsername(username);
        if (utente == null) {
            LOGGER.warning("Errore: username non trovato.");
            return false;
        }
        if (!utente.getPassword().equals(password)) {
        LOGGER.warning("Errore: password errata per username " + username);
            return false;
        }
        utenteLoggato = utente;
   LOGGER.info("Risultato verifica: true");
        return true;
    }



    /** Questo metodo consente di registrare un nuovo utente.
     * @param username il nome utente da registrare
     * @param password la password da registrare
     * @return restituisce true se la registrazione è avvenuta con successo, false se il nome utente esiste già
     */
    public boolean register(String username, String password) {

        if (utenteDAO.getUtenteByUsername(username) != null) {
            return false; // username già esistente
        }
        Utente nuovo = new Utente(username, password);
        utenteDAO.addUtente(nuovo);
        return true;
    }



    /**
     * Questo metodo consente di impostare il controller associato a questo UtenteManager,
     * poichè , essendo  l'attributo controller dichiarato come privato,
     * la dipendenza del controller non viene direttamente passata tramite il costruttore,
     * ed è quindi necessario un metodo setter per assegnarlo
     * @param controller rappresenta il controller che deve essere assegnato
     */
    public void setController(Controller controller) {

        this.controller = controller;
    }




    /**
     * Questo metodo restituisce l'utente attualmente loggato.
     * @return l'istanza di Utente che rappresenta l'utente loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}