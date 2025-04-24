package org.example;
import org.example.Utente;
import org.example.Bacheca;
public class ToDo_BACHECA {
    private String titoloToDo;
    private Utente utenteCreatore;

    private String tipoBacheca;
    public ToDo_BACHECA(String titoloToDo, Utente utenteCreatore, String tipoBacheca ){
        this.titoloToDo=titoloToDo; this.utenteCreatore=utenteCreatore; this.tipoBacheca=tipoBacheca;
    } public void assegnaBacheca(Bacheca bacheca){
        if(bacheca==null){ System.out.println("Errore. La bacheca non può essere null!");
        }
        bacheca.creaToDo(titoloToDo,utenteCreatore);
        System.out.println("ToDo ' "+titoloToDo + " 'assegnato alla Bacheca' "+bacheca.getTitolo() + " '. ");
    } public String getTitoloToDo(){
        return titoloToDo;
    }
    public Utente getUtenteCreatore(){
        return utenteCreatore;
    } public String getTipoBacheca(){
        return tipoBacheca;
    }
    public void rimuoviDaBacheca(Bacheca bacheca, ToDo toDo){
        if(bacheca==null||toDo==null){
            System.out.println("Errore. Bacheca o ToDo non validi");
            return; }
        if(bacheca.eliminaToDo(toDo)){
            System.out.println("ToDo "+toDo.getTitolo()+ " rimosso dalla Bacheca "+bacheca.getTitolo());
        }
        else{ System.out.println("Il ToDo non era presente nella Bacheca");
        }
    }
}



