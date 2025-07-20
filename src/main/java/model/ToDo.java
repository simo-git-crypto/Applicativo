// src/model/ToDo.java
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;

/**
 * Le attivita da completare, che possono essere condivise con altri utenti.
 */
public class ToDo {
    private int id;
    private String titolo;
    private String url;
    private String descrizione;
    private Date scadenza;
    private String img;
    private int posizione;
    private String colore;
    private StatoToDo stato;
    private List<Utente> utentiCondivisi;
    private Utente utenteCreatore;
    private String condiviso_da_utente;
    private int idUtente;
    private int idBoard;

    /**
     * Costruttore della classe ToDo usato per creare nuovi ToDo con i parametri di base.
     * @param titolo Il titolo del ToDo.
     * @param descrizione La descrizione del ToDo.
     * @param utenteCreatore L'utente che ha creato il ToDo.
     * @param scadenza La data di scadenza del ToDo.
     * @param colore Il colore associato al ToDo.

     */
    public ToDo(String titolo, String descrizione, Utente utenteCreatore, Date scadenza, String colore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.utenteCreatore = utenteCreatore;
        this.scadenza = scadenza;
        this.colore = colore;
        this.url = "";
        this.posizione = -1;
        this.stato = StatoToDo.NON_COMPLETATO; // Default status for new ToDo
        this.utentiCondivisi = new ArrayList<>();
        this.condiviso_da_utente = null;
        if (utenteCreatore != null) {
            this.idUtente = utenteCreatore.getId();
        } else {
            this.idUtente = 0; // Should be set to actual user ID
        }
        this.idBoard = 0; // Should be set to actual board ID
    }



    /**
     * Costruttore della classe ToDo usato per creare nuovi ToDo con un'immagine.
     * @param titolo Il titolo del ToDo.
     * @param descrizione La descrizione del ToDo.
     * @param utenteCreatore L'utente che ha creato il ToDo.
     * @param scadenza La data di scadenza del ToDo.
     * @param colore Il colore associato al ToDo.
     * @param img L'immagine associata al ToDo.
     */
    public ToDo(String titolo, String descrizione, Utente utenteCreatore, Date scadenza, String colore, String img) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.utenteCreatore = utenteCreatore;
        this.scadenza = scadenza;
        this.colore = colore;
        this.img = img;
        this.url = "";
        this.posizione = -1;
        this.stato = StatoToDo.NON_COMPLETATO; // Default status for new ToDo
        this.utentiCondivisi = new ArrayList<>();
        this.condiviso_da_utente = null;
        if (utenteCreatore != null) {
            this.idUtente = utenteCreatore.getId();
        } else {
            this.idUtente = 0; // Should be set to actual user ID
        }
        this.idBoard = 0; // Should be set to actual board ID
    }

    /**
     * Costruttore della classe ToDo usato per creare nuovi ToDo con un'immagine e altri parametri.
     * E'utile quando si recuperano i ToDo dal database e si vogliono popolare tutti i campi.
     * @param id L'ID del ToDo.
     *  @param titolo Il titolo del ToDo.
     *  @param descrizione La descrizione del ToDo.
     *  @param scadenza La data di scadenza del ToDo.
     *  @param colore Il colore associato al ToDo.
     *  @param stato Lo stato del ToDo (ad esempio, COMPLETATO, NON_COMPLETATO).
     *  @param url L'URL associato al ToDo.
     * @param img L'immagine associata al ToDo.
     *  @param posizione La posizione del ToDo nella lista.
     *  @param idUtente L'ID dell'utente che ha creato il ToDo.
     *  @param condivisoDaUtente Il nome utente di chi ha condiviso il ToDo.
     *  @param idBoard L'ID della bacheca a cui il ToDo appartiene.

     */
    public ToDo(int id, String titolo, String descrizione, LocalDate scadenza, String colore,
                String stato, String url, String img, int posizione, int idUtente,
                String condivisoDaUtente, int idBoard) {

        this.id = id;
        this.titolo = titolo;
        this.url = url;
        this.descrizione = descrizione;
        this.scadenza = java.sql.Date.valueOf(scadenza);
        this.img = img;
        this.posizione = posizione;
        this.colore = colore;
        this.stato = StatoToDo.valueOf(stato); // if using enum
        this.idUtente = idUtente;
        this.condiviso_da_utente = condivisoDaUtente;
        this.idBoard = idBoard;
        this.utenteCreatore = null; // Will be populated by DAO after retrieval based on idUtente
    }


    /**
     * Questo metodo restituisce l'id del todo.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return è l'istruzione per restituire l'id del ToDo
     */

    public int getId() {
        return id;
    }
    /**
     * Questo metodo imposta l'id del ToDo.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.

     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Questo metodo restituisce l'id dell'utente che ha creato il ToDo.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return id dell'utente che ha creato il ToDo
     */
    public int getIdUtente() { return idUtente; }
    /**
     * Questo metodo imposta l'id dell'utente che ha creato il ToDo.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param idUtente id dell'utente che ha creato il ToDo
     */
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    /**
     * Questo metodo restituisce l'id della bacheca a cui il ToDo appartiene.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return id della bacheca
     */
    public int getIdBoard() { return idBoard; }

    /**
     * Questo metodo imposta l'id della bacheca a cui il ToDo appartiene.
     * E' necessario poichè, essendo l'id un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param idBoard id della bacheca a cui il ToDo appartiene
     */
    public void setIdBoard(int idBoard) { this.idBoard = idBoard; }

    /**
     * Questo metodo restituisce l'utente che ha condiviso il ToDo.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return nome utente dell'utente che ha condiviso il ToDo
     */
    public String getCondiviso_da_utente() {
        return condiviso_da_utente;
    }

    /**
     * Questo metodo imposta l'utente che ha condiviso il ToDo.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     *  * @param condiviso_da_utente : nome utente dell'utente che ha condiviso il ToDo
     */
    public void setCondiviso_da_utente(String condiviso_da_utente) {
        this.condiviso_da_utente = condiviso_da_utente;
    }

    /**
     * Questo metodo restituisce il titolo del ToDo.
     * E' necessario poichè, essendo il titolo un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return titolo del ToDo
     */
    public String getTitolo() { return titolo; }
    /**
        * Questo metodo imposta il titolo del ToDo.
     * E' necessario poichè, essendo il titolo un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param titolo  IL Titolo del ToDo
     */
    public void setTitolo(String titolo) { this.titolo = titolo; }


    /**
     * Questo metodo restituisce l'URL associato al ToDo.
     * E' necessario poichè, essendo l'URL un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.

     * @return URL del ToDo
     */
    public String getUrl() { return url; }
    /**
     * Questo metodo imposta l'URL associato al ToDo.
     * E' necessario poichè, essendo l'URL un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     @param url URL del ToDo
     */
    public void setUrl(String url) { this.url = url; }

    /**
     * Questo metodo restituisce la descrizione del ToDo.
     * E' necessario poichè, essendo la descrizione un campo privato, non è accessibile direttamente con il semplice costruttore,
     *      * quindi è necessario un metodo getter per poterla leggere.
     * @return La descrizione del ToDo
     */
    public String getDescrizione() { return descrizione; }

    /**
     * Questo metodo imposta la descrizione del ToDo,  essendo la descrizione un campo privato, non è accessibile direttamente con il semplice costruttore,
     *      *      * quindi è necessario un metodo setter per poterla assegnarw.
     * @param descrizione La descrizione del ToDo
     */
     public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    /**
     * Questo metodo restituisce la data di scadenza del ToDo.
     * E' necessario poichè, essendo la data di scadenza un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterla leggere.
     * @return data di scadenza del ToDo
     */
    public Date getScadenza() { return scadenza; }

    /**
     * Questo metodo imposta la data di scadenza del ToDo.
     * E' necessario poichè, essendo la data di scadenza un campo privato, non è accessibile direttamente con il semplice costruttore,
     * * quindi è necessario un metodo setter per poterla assegnare.
     * @param scadenza  data di scadenza del ToDo
     */
    public void setScadenza(Date scadenza) { this.scadenza = scadenza; }


   /**
     * Questo metodo restituisce l'immagine associata al ToDo.
     * E' necessario poichè, essendo l'immagine un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterla leggere.
     * @return immagine del ToDo
     */
    public String getImg() { return img; }

    /**
     * Questo metodo imposta l'immagine associata al ToDo.
     * E' necessario poichè, essendo l'immagine un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterla assegnare.
     * @param img  immagine del ToDo
     */
    public void setImg(String img) { this.img = img; }

    /**
     * Questo metodo restituisce la posizione del ToDo nella lista.
     * E' necessario poichè, essendo la posizione un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterla leggere.
     * @return posizione del ToDo
     */
    public int getPosizione() { return posizione; }

    /**
     * Questo metodo imposta la posizione del ToDo nella lista.
     * E' necessario poichè, essendo la posizione un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterla assegnare.
     * @param posizione  posizione del ToDo
     */
    public void setPosizione(int posizione) { this.posizione = posizione; }

    /**
     * Questo metodo restituisce il colore associato al ToDo.
     * E' necessario poichè, essendo il colore un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return colore del ToDo  colore del ToDo
     */
    public String getColore() { return colore; }

    /**
     * Questo metodo imposta il colore associato al ToDo.
     * E' necessario poichè, essendo il colore un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param colore colore del ToDo
     */
    public void setColore(String colore) { this.colore = colore; }

    /**
     * Questo metodo restituisce lo stato del ToDo.
     * E' necessario poichè, essendo lo stato un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return stato del ToDo
     */
    public StatoToDo getStato() { return stato; }

    /**
     * Questo metodo imposta lo stato del ToDo.
     * E' necessario poichè, essendo lo stato un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param stato: stato del ToDo
     */
    public void setStato(StatoToDo stato) { this.stato = stato; }

    /**
     * Questo metodo restituisce la lista degli utenti con cui il ToDo è condiviso.
     * E' necessario poichè, essendo la lista un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterla leggere.
     * @return lista degli utenti condivisi
     */
    public List<Utente> getUtentiCondivisi() { return utentiCondivisi; }

    /**
     * Questo metodo imposta la lista degli utenti con cui il ToDo è condiviso.
     * E' necessario poichè, essendo la lista un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterla assegnare.
     * @param utentiCondivisi lista degli utenti con cui il ToDo è condiviso
     */
    public void setUtentiCondivisi(List<Utente> utentiCondivisi) { this.utentiCondivisi = utentiCondivisi; }

    /**
     * Questo metodo restituisce l'utente che ha creato il ToDo.
     * E' necessario poichè, essendo l'utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return utente creatore del ToDodegli
     */
    public Utente getUtenteCreatore() { return utenteCreatore; }

    /**
     * Questo metodo imposta l'utente che ha creato il ToDo.
     * E' necessario poichè, essendo l'utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param utenteCreatore  utente che ha creato il ToDo
     */
    public void setUtenteCreatore(Utente utenteCreatore) { this.utenteCreatore = utenteCreatore; }

    /**
     * Questo metodo aggiunge un utente alla lista degli utenti con cui il ToDo è condiviso.
     * Se l'utente è già presente nella lista, non viene aggiunto nuovamente.
     * @param u l'utente da aggiungere
     */
    public void addUtenteCondiviso(Utente u) {
        if (!utentiCondivisi.contains(u)) {
            utentiCondivisi.add(u);
        }
    }

    /**
     * Questo metodo rimuove un utente dalla lista degli utenti con cui il ToDo è condiviso.
     * Se l'utente non è presente nella lista, non viene fatto nulla.
     * @param u l'utente da rimuovere
     */
    public void removeUtenteCondiviso(Utente u) {
        utentiCondivisi.remove(u);
    }

/**
     * Questo metodo imposta il nome utente di chi ha condiviso il ToDo.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo setter per poterlo assegnare.
     * @param sharedByUsername nome utente di chi ha condiviso il ToDo
     */
    public void setSharedByUsername(String sharedByUsername) {
        this.condiviso_da_utente = sharedByUsername;
    }

    /**
     * Questo metodo restituisce il nome utente di chi ha condiviso il ToDo.
     * E' necessario poichè, essendo il nome utente un campo privato, non è accessibile direttamente con il semplice costruttore,
     * quindi è necessario un metodo getter per poterlo leggere.
     * @return nome utente di chi ha condiviso il ToDo
     */
    public String getSharedByUsername() {
        return this.condiviso_da_utente;
    }

    /**
     * Questo metodo imposta la bacheca a cui il ToDo appartiene.
     * Non imposta direttamente l'idBoard, ma lo fa indirettamente tramite il metodo setIdBoard di Board.
     * @param bachecaDestinatario la bacheca a cui il ToDo deve essere associato
     */
    public void setBoard(Board bachecaDestinatario) {
        if (bachecaDestinatario != null) {
            this.idBoard = bachecaDestinatario.getId();
        }
    }
}