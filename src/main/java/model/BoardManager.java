package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BoardManager {
    private final Map<String, List<Board>> userBoards = new HashMap<>();
    private final List<ToDo> salvaToDoCondivisi = new ArrayList<>();

    public void addBoard(String username, Board board) {
        userBoards.computeIfAbsent(username, k -> new ArrayList<>()).add(board);
    }

    public void addToDo(String username, String boardName, ToDo todo) {
        List<Board> boards = userBoards.get(username);
        if (boards != null) {
            for (Board board : boards) {
                if (board.getTitolo().equalsIgnoreCase(boardName)) {
                    board.getTodos().add(todo);
                    return;
                }
            }
        }
    }

    public List<ToDo> getToDosForBoard(String username, String boardName) {
        List<Board> boards = userBoards.get(username);
        if (boards != null) {
            for (Board board : boards) {
                if (board.getTitolo().equalsIgnoreCase(boardName)) {
                    return board.getTodos();
                }
            }
        }
        return new ArrayList<>();
    }

    public void deleteToDo(String username, String boardName, ToDo todo) {
        List<Board> boards = userBoards.get(username);
        if (boards != null) {
            for (Board board : boards) {
                if (board.getTitolo().equalsIgnoreCase(boardName)) {
                    board.getTodos().remove(todo);
                    return;
                }
            }
        }
    }

    public void condividiToDo(ToDo todo, String boardNameOriginal, String destinatarioUsername) {
        userBoards.computeIfAbsent(destinatarioUsername, k -> new ArrayList<>());

        List<Board> boardsDestinatario = userBoards.get(destinatarioUsername);
        if (boardsDestinatario == null) {
            System.err.println("Error: Recipient user's board list not found or created.");
            return;
        }

        Board bachecaDestinatario = boardsDestinatario.stream()
                .filter(br -> br.getTitolo().equalsIgnoreCase(boardNameOriginal))
                .findFirst()
                .orElseGet(() -> {
                    Board nuova = new Board(boardNameOriginal);
                    boardsDestinatario.add(nuova);
                    return nuova;
                });

        if (!bachecaDestinatario.getTodos().contains(todo)) {
            bachecaDestinatario.getTodos().add(todo);
             todo.setSharedByUsername(todo.getUtenteCreatore().getUsername());
        }

        if (!salvaToDoCondivisi.contains(todo)) {
            salvaToDoCondivisi.add(todo);
        }
        System.out.println("ToDo '" + todo.getTitolo() + "' condiviso con '" + destinatarioUsername + "' nella bacheca '" + boardNameOriginal + "'.");
    }


    public boolean salvaBacheche(String username) {
        List<Board> boards = userBoards.get(username);
        if (boards == null) return false;
        try (PrintWriter writer = new PrintWriter(new FileWriter(username + "_bacheche.txt"))) {
            for (Board board : boards) {
                writer.println(board.getTitolo());
                for (ToDo todo : board.getTodos()) {
                    writer.println(" - " + todo.getTitolo() + "|" +
                            (todo.getScadenza() != null ? todo.getScadenza().getTime() : "") + "|" +
                            todo.getColore() + "|" +
                            todo.getStato().name() + "|" +
                            (todo.getSharedByUsername() != null ? todo.getSharedByUsername() : "")); // Save sharedBy
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}