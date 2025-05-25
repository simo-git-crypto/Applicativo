package controller;

import model.*;

import java.util.List;

public class Controller {
    private final BoardManager boardManager;
    private final UtenteManager utenteManager;

    public Controller(UtenteManager utenteManager) {
        this.boardManager = new BoardManager();
        this.utenteManager = utenteManager;
    }

    public Utente getUser(String username) {
        return utenteManager.getUser(username);
    }

    public boolean verifyCredentials(String login, String password) {
        return utenteManager.verifyCredentials(login, password);
    }

    // Board
    public void addBoard(String username, Board board) {
        boardManager.addBoard(username, board);
    }

    // ToDo
    public void createToDo(String username, String boardName, ToDo todo) {
        boardManager.addToDo(username, boardName, todo);
    }

    public List<ToDo> getToDo(String username, String boardName) {
        return boardManager.getToDosForBoard(username, boardName);
    }

    public void markAsCompleted(ToDo todo) {
        todo.setStato(StatoToDo.COMPLETATO);
    }

    public BoardManager getBoardController() {
        return this.boardManager;
    }
    public boolean salvaBacheche(String username) {
        return boardManager.salvaBacheche(username);
    }

}
