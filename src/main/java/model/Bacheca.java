package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.ToDo;
public class Bacheca {
    private String titolo;
    private String descrizione;
    private List<ToDo> toDoList;
    private String tipo;
    private Utente proprietario;

    public Bacheca(String titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.toDoList = new ArrayList<>();
    }

    public List<ToDo> getToDoList() {
        return new ArrayList<>(toDoList);
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public static Bacheca creadatastiera() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Inserire titolo: ");
            String titolo = sc.nextLine();
            System.out.println("Inserire descrizione: ");
            String descrizione = sc.nextLine();
            System.out.println("Inserire tipo: ");
            String tipo = sc.nextLine();
            Bacheca bacheca = new Bacheca(titolo, descrizione);
            bacheca.setTipo(tipo);
            return bacheca;
        } catch (Exception e) {
            System.err.println("Errore nella creazione della bacheca");
            return null;
        }
    }

    public void creaToDo(String titolo, Utente creatore) {
        for (ToDo todo : toDoList) {
            if (todo.getTitolo().equals(titolo)) {
                System.out.println("Il ToDo '" + titolo + "' esiste già nella bacheca.");
                return;
            }
        }
        ToDo nuovoToDo = new ToDo(titolo, creatore);
        toDoList.add(nuovoToDo);
        System.out.println("ToDo creato con successo");

    }

    public boolean eliminaToDo(ToDo todo) {
        if (toDoList.remove(todo)) {
            System.out.println("ToDo " + todo.getTitolo() + "rimosso dalla bacheca ");
            return true;
        } else {
            System.out.println("Todo non trovato ");
            return false;
        }
    }

    public boolean modificaToDo(ToDo vecchio, ToDo nuovo) {
        int index = toDoList.indexOf(vecchio);
        if (index != -1) {
            toDoList.set(index, nuovo);
            System.out.println("ToDo modificato con successo ");
            return true;
        }
        System.out.println("ToDo non trovato");
        return false;
    }

    public boolean contieneToDo(ToDo todo) {
        return toDoList.contains(todo);
    }

    public void visualizzaBacheca() {
System.out.println(this);

    }
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder(); //StringBuilder è una classe che viene utilizzata per modificare le stringhe senza creare nuovi oggetti
        sb.append("Bacheca: ").append(titolo).append("\n"); //Il metodo append viene utilizzato per aggiungere un valore alla sequenza di caratteri senza creare un nuovo oggetto stringa
        sb.append("Descrizione: ").append(descrizione).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("ToDo List: \n");
        if(toDoList.isEmpty()) {
            sb.append("Nessun ToDo presente\n");
        }
            else{
                for(ToDo todo:toDoList){
                    sb.append(" -").append(todo.getTitolo()).append("Creato da: ").append(todo.getLoginCreatore()).append("\n");
                }
            }
            return sb.toString();
        }
    }
