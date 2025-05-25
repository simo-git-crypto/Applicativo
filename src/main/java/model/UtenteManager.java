package model;

import controller.Controller;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class UtenteManager {
    private final Map<String, String> utenti;
    private Controller controller;

    public UtenteManager() {
        this.utenti = new HashMap<>();
        System.out.println("Istanza UtenteManager: " + this.hashCode());
        register("admin", "admin123");
    }
    public boolean register(String username, String password) {
        System.out.println("Register su istanza: " + this.hashCode());
        System.out.println("Registrazione: " + username + " / " + password);
        if (utenti.containsKey(username)) {
            return false;
        }
        utenti.put(username, password);
        return true;
    }

    public boolean verifyCredentials(String username, String password) {
        System.out.println("Verify su istanza: " + this.hashCode());
        System.out.println("Verifica credenziali: " + username + " / " + password);
        boolean result = utenti.containsKey(username) && utenti.get(username).equals(password);
        System.out.println("Risultato verifica: " + result);
        return result;
    }

    public Utente getUser(String username) {
        String password = utenti.get(username);
        return (password != null) ? new Utente(username, password) : null;
    }

    public void setController(controller.Controller controller) {
        this.controller = controller;
    }
}