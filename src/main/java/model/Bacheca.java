package model;

import java.util.ArrayList;
import java.util.List;

public class Bacheca {
    private TitoloBacheca titolo;
    private String descrizione;
    private List<ToDo> todos;

    // Costruttore
    public Bacheca(TitoloBacheca titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.todos = new ArrayList<>();
    }

}