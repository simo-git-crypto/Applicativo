// ToDo.java (Conceptual - Add these to your existing ToDo class)
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDo {
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
    private String sharedByUsername;

    public ToDo(String titolo, String descrizione, Utente utenteCreatore, Date scadenza, String colore) {
        this.titolo = titolo;
        this.url = "";
        this.img = "";
        this.posizione = -1;
        this.descrizione = descrizione;
        this.utenteCreatore = utenteCreatore;
        this.scadenza = scadenza;
        this.colore = colore;
        this.stato = StatoToDo.COMPLETATO;
        this.utentiCondivisi = new ArrayList<>();
        this.sharedByUsername = null;
    }

    public void setSharedByUsername(String sharedByUsername) {
        this.sharedByUsername = sharedByUsername;
    }
    public String getSharedByUsername() {
        return sharedByUsername;
    }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public Utente getUtenteCreatore() { return utenteCreatore; } // Make sure this exists
    public Date getScadenza() { return scadenza; }
    public String getColore() { return colore; }
    public void setColore(String colore) { this.colore = colore; }
    public StatoToDo getStato() { return stato; }
    public void setStato(StatoToDo stato) { this.stato = stato; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public int getPosizione() { return posizione; }
    public void setPosizione(int posizione) { this.posizione = posizione; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
}