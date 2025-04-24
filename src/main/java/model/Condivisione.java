package org.example;

import java.util.ArrayList;
import java.util.List;
import org.example.Bacheca;
import org.example.Utente;
public class Condivisione {

    private ToDo todo;
    private Utente creatore;
    private List<Utente> utenticondivisi;

    // Costruttore
    public Condivisione(ToDo todo) {
        this.todo=todo;
        this.creatore = new Utente(todo.getLoginCreatore(),"");
        this.utenticondivisi=new ArrayList<>();
    }

    // GETTER
    public String getTitoloToDo() {
        return todo.getTitolo();
    }

    public Utente getCreatore() {
        return creatore;
    }



    public List<Utente> getUtentiCondivisi() {
        return new ArrayList<>(utenticondivisi); // Restituisce una copia per evitare modifiche dirette
    }

    // Metodo per condividere un ToDo con un nuovo utente
    public void Condividi(Utente nuovoUtente) {
        if (nuovoUtente == null ) {
            System.out.println("Errore: Utente non valido.");
            return;
        }
        if (utenticondivisi.contains(nuovoUtente)) {
            System.out.println(" L'utente ha già accesso al ToDo.");
            return;
        }
        utenticondivisi.add(nuovoUtente);
        System.out.println("ToDo condiviso con: " + nuovoUtente.getLogin());
    }

    // Metodo per revocare la condivisione di un utente
    public void Revocacondivisione(Utente utente) {
        if (utente == null ) {
            System.out.println("Utente non valido ");
            return;
        }
        if (utenticondivisi.remove(utente)) {
            System.out.println(" Condivisione revocata per: " + utente.getLogin());
        } else {
            System.out.println("Utente non presente nella condivisione.");
        }
    }

    // Metodo per visualizzare gli utenti con cui è condiviso il ToDo
    public void visualizzaCondivisione() {
        System.out.println("\n🔹 ToDo: '" + todo.getTitolo() + "' (Creato da: " + creatore.getLogin() + ")");
        if (utenticondivisi.isEmpty()) {
            System.out.println(" Nessun utente con cui è condiviso.");
        } else {
            System.out.println("Condiviso con:");
            for (Utente ut : utenticondivisi) {
                System.out.println(" - " + ut.getLogin());
            }
        }
    }
}
