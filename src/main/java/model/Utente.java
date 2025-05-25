package model;
import java.util.ArrayList;
import java.util.List;

public class Utente {
    private String username;
    private String password;
    private List<Bacheca> bacheche;
    private List<ToDo> todos;

    // Costruttore
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.bacheche = new ArrayList<>();
        this.todos = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }
}