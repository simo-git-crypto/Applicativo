package org.example;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ToDo {
    private String titolo;
    private LocalDate datascadenza;
    private String url;
    private String immagine;
    private String colSfondo;
    private final List<Utente> listaUt;
    private boolean stato;
    private final String loginCreatore;
    private Bacheca bacheca;

    public ToDo(String titolo, Utente creatore) {
        this.titolo = titolo;
        this.listaUt = new ArrayList<>();
        this.stato = false;
        this.loginCreatore = creatore.getLogin();
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public LocalDate getDatascadenza() {
        return datascadenza;
    }

    public void setDatascadenza(LocalDate datascadenza) {
        this.datascadenza = datascadenza;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getColSfondo() {
        return colSfondo;
    }

    public void setColSfondo(String colSfondo) {
        this.colSfondo = colSfondo;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public List<Utente> getListaUt() {
        return listaUt;
    }

    public void aggiungiUtente(Utente utente) {
        this.listaUt.add(utente);
    }

    public void rimuoviutente(Utente utente) {
        if (listaUt.contains(utente)) {
            listaUt.remove(utente);
            System.out.println("Utente rimosso con successo! ");
        } else {
            System.out.println("Utente non trovato! ");
        }
    }

    public String getLoginCreatore() {
        return loginCreatore;
    }

    public void inserisciDatastiera() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Inserisci il titolo del ToDo: ");
        setTitolo(sc.nextLine());
        System.out.println("Inserisci la data di scadenza (yyyy-MM-dd):");
        LocalDate dataInserita=null;
        try {
            dataInserita=LocalDate.parse(sc.nextLine());
        }
        catch (Exception e) {
            System.out.println("Errore: formato data non valido! Imposto NULL ");
        }
        setDatascadenza(dataInserita);
        System.out.println("Inserisci l' url: ");
        setUrl(sc.nextLine());
        System.out.println("Inserire nome immagine: ");
        setImmagine(sc.nextLine());
        System.out.println("Inserisci il colore dello Sfondo: ");
        setColSfondo(sc.nextLine());
        System.out.println("Inserisci lo stato: ");
        setStato(sc.nextLine().equalsIgnoreCase("S"));
        System.out.println("ToDo creato con successo! ");

    }

    public void creaBacheca(String titolobacheca, String descrizione) {
        this.bacheca = new Bacheca(titolobacheca, descrizione);
        System.out.println("Bacheca " + titolobacheca + "creata per il ToDo " + titolo + " ' ");
    }

    public void visualizzadettagli() {
        System.out.println("Dettagli del ToDo: ");
        System.out.println("Titolo " + getTitolo());
        System.out.println("Datascadenza " + (datascadenza != null ? datascadenza : "Nessuna data impostata")); //stampa la data se non è null
        System.out.println("Url: " + (url.isEmpty() ? "Nessun url " : url));
        System.out.println("Colore di sfondo: " + getColSfondo());
        System.out.println("Stato: " + (isStato() ? "Completato" : "Non completato"));
        System.out.println("Creato da: " + getLoginCreatore());
        System.out.println("Il ToDo è condiviso con: ");
        for (Utente u : listaUt) { //ciclo for-each che prende ogni utente dalla lista Ut,
System.out.println(" - "+u.getLogin());

        }
    }
}
