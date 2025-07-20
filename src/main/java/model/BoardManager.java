// src/model/BoardManager.java
package model;

import dao.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Questa classe gestisce le operazioni relative alle bacheche (Board) e alle attività (ToDo).
 * Essa funge da intermediario tra le DAO e le operazioni di business, permettendo di aggiungere, aggiornare, eliminare e condividere ToDo tra bacheche.
 * Le operazioni sono registrate nei log per facilitare il debug e la tracciabilità delle azioni.
 */
public class BoardManager {
    private BoardDAO boardDAO;
    private ToDoDAO toDoDAO;
    private UtenteDAO utenteDAO;
    private static final Logger logger = Logger.getLogger(BoardManager.class.getName());

    /**
     * Costruttore della classe BoardManager.
     * Inizializza le DAO necessarie per gestire le operazioni su Board e ToDo.
     * @param boardDAO : l'istanza di BoardDAO che gestisce le operazioni sulle bacheche.
     * @param toDoDAO : l'istanza di ToDoDAO che gestisce le operazioni sulle attività.
     * @param utenteDAO : l'istanza di UtenteDAO che gestisce le operazioni sugli utenti.
     */
    public BoardManager(BoardDAO boardDAO, ToDoDAO toDoDAO, UtenteDAO utenteDAO) {
        this.boardDAO = boardDAO;
        this.toDoDAO = toDoDAO;
        this.utenteDAO = utenteDAO;
    }

    /**
     * Imposta il BoardDAO per questa istanza di BoardManager.
     * E' necessario poichè BoardDAO è un campo privato e non accessibile direttamente tramite il costruttore,
     * e serve un metodo setter per assegnarlo.
     * @param boardDAO Il BoardDAO da impostare.
     */
    public void setBoardDAO(BoardDAO boardDAO) {
        this.boardDAO = boardDAO;
    }

    /**
     * Aggiunge una nuova bacheca (Board) al sistema.
     * Questo metodo utilizza il BoardDAO per eseguire l'operazione di inserimento.
     * @param board La bacheca da aggiungere.
     */
    public void addBoard(Board board) {
        boardDAO.addBoard(board);
    }

    /**
     * Aggiunge un nuovo ToDo alla bacheca.
     * Questo metodo utilizza il ToDoDAO per eseguire l'operazione di inserimento.
     * @param todo è il ToDo da aggiungere.
     */
    public void addToDo(ToDo todo) {
        toDoDAO.addToDo(todo);
    }

    /**
     * Recupera tutti i ToDo associati a una specifica bacheca.
     * Questo metodo utilizza il ToDoDAO per ottenere la lista dei ToDo per l'ID della bacheca specificata.
     * @param boardId L'ID della bacheca per cui recuperare i ToDo.
     * @return Restituisce una lista di ToDo associati alla bacheca specificata.
     */
    public List<ToDo> getToDosForBoard(int boardId) {
        return toDoDAO.getToDosByBoardId(boardId);
    }

    /**
     * Elimina un ToDo specifico dal sistema.
     * Questo metodo utilizza il ToDoDAO per eseguire l'operazione di eliminazione.
     * * @param Rappresenta todo Il ToDo da eliminare.
     */
    public void deleteToDo(ToDo todo) {
        toDoDAO.deleteToDo(todo.getId());
    }

    /**
     * Condivide un ToDo con un altro utente in una bacheca specifica.
     * Questo metodo verifica se l'utente destinatario esiste e se la bacheca di destinazione è già presente.
     * Se la bacheca non esiste, viene creata una nuova bacheca per il destinatario.
     * Se un ToDo con lo stesso titolo e creato dallo stesso utente esiste già nella bacheca del destinatario, non viene creato un duplicato.
     * @param todo Il ToDo da condividere.
     * @param boardNameOriginal il nome della bacheca originale in cui il ToDo è stato creato.
     * @param destinatarioUsername il nome utente del destinatario con cui condividere il ToDo.
     */
    public void condividiToDo(ToDo todo, String boardNameOriginal, String destinatarioUsername) {
        Utente utenteDestinatario = utenteDAO.getUtenteByUsername(destinatarioUsername);
        if (utenteDestinatario == null) {
            logger.severe("Utente destinatario non trovato");
            return;
        }

        List<Board> boardsDestinatario = boardDAO.getBoardsByUsername(destinatarioUsername);
        if (boardsDestinatario == null) {
            boardsDestinatario = new ArrayList<>();
        }

        /**
         * Controlla se la bacheca del destinatario esiste già.
         */
        Board bachecaDestinatario = null;
        for (Board br : boardsDestinatario) {
            if (br.getTitolo().equalsIgnoreCase(boardNameOriginal)) {
                bachecaDestinatario = br;
                break;
            }
        }
        if (bachecaDestinatario == null) {
            bachecaDestinatario = new Board(0, boardNameOriginal, utenteDestinatario.getId());
            bachecaDestinatario.setUsername(destinatarioUsername);
            boardDAO.addBoard(bachecaDestinatario);
            bachecaDestinatario = boardDAO.getBoardByTitoloAndUtente(boardNameOriginal, utenteDestinatario.getId());
            if (bachecaDestinatario == null) {
             logger.severe("Errore nel recupero della bacheca destinatario appena creata.");
                return;
            }
        }


        /**
         * Controlla se un ToDo con lo stesso titolo e creato dallo stesso utente esiste già nella bacheca del destinatario.
         */
        List<ToDo> todosDestinatario = toDoDAO.getToDosByBoardId(bachecaDestinatario.getId());
        boolean alreadyExists = false;
        for (ToDo existingTodo : todosDestinatario) {
            if (existingTodo.getTitolo().equals(todo.getTitolo()) &&
                    existingTodo.getSharedByUsername() != null &&
                    existingTodo.getSharedByUsername().equals(todo.getUtenteCreatore().getUsername())) {
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {
            ToDo sharedToDo = new ToDo(
                    todo.getTitolo(),
                    todo.getDescrizione(),
                    utenteDestinatario, // The recipient is the new creator
                    todo.getScadenza(),
                    todo.getColore()
            );
            sharedToDo.setStato(todo.getStato());
            sharedToDo.setUrl(todo.getUrl());
            sharedToDo.setImg(todo.getImg());
            sharedToDo.setPosizione(todo.getPosizione());
            sharedToDo.setSharedByUsername(todo.getUtenteCreatore().getUsername());
            sharedToDo.setIdUtente(utenteDestinatario.getId()); // Recipient's user ID
            sharedToDo.setIdBoard(bachecaDestinatario.getId()); // Destination board ID
            toDoDAO.addToDo(sharedToDo);
         logger.info("ToDo '" + todo.getTitolo() + "' condiviso con '" + destinatarioUsername + "' nella bacheca '" + boardNameOriginal + "'.");
        } else {
         logger.info("ToDo '" + todo.getTitolo() + "' già esistente nella bacheca '" + boardNameOriginal + "' dell'utente '" + destinatarioUsername + "'.");
        }
    }


    /**
     * Recupera tutte le bacheche (Board) presenti nel sistema.
     * Questo metodo utilizza il BoardDAO per ottenere la lista di tutte le bacheche.
     * * @return Restituisce una lista di tutte le bacheche presenti nel sistema.
     */
    public List<Board> getAllBoards() {
        return boardDAO.getAllBoards();
    }

    /**
     * Aggiorna una bacheca (Board) esistente nel sistema.
     * Questo metodo utilizza il BoardDAO per eseguire l'operazione di aggiornamento.
     * * @param board La bacheca da aggiornare.
     */
    public void updateBoard(Board board) {
        boardDAO.updateBoard(board);
    }

    /**
     * Elimina una bacheca (Board) e tutti i ToDo associati ad essa.
     * Questo metodo prima elimina tutti i ToDo associati alla bacheca e poi elimina la bacheca stessa.
     * @param id L'ID della bacheca da eliminare.
     */
    public void deleteBoard(int id) {
        List<ToDo> todosToDelete = toDoDAO.getToDosByBoardId(id);
        if (todosToDelete != null) {
            for (ToDo todo : todosToDelete) {
                toDoDAO.deleteToDo(todo.getId());
            }
        }
        boardDAO.deleteBoard(id);
    }

    /**
     * Recupera una bacheca (Board) specifica in base al suo ID.
     * Questo metodo utilizza il BoardDAO per ottenere la bacheca corrispondente all'ID specificato.
     * @param bachecaId L'ID della bacheca da recuperare.
     * @return Restituisce la bacheca corrispondente all'ID specificato, o null se non esiste.
     */
    public List<Board> getBoardsByBachecaId(int bachecaId) { // This method seems to be a remnant, likely to be removed if Board is fully replacing Bacheca
        return boardDAO.getBoardsByBachecaId(bachecaId);
    }

    /**
     * Recupera una bacheca (Board) specifica in base al titolo e all'ID dell'utente.
     * Questo metodo utilizza il BoardDAO per ottenere la bacheca corrispondente al titolo e all'ID utente specificati.
     * * Questo è utile per evitare duplicati di bacheche con lo stesso titolo per lo stesso utente.
     * @param titolo Il titolo della bacheca da recuperare.
     * @param idUtente L'ID dell'utente a cui appartiene la bacheca.
     * @return Restituisce la bacheca corrispondente al titolo e all'ID utente specificati, o null se non esiste.
     */
    public Board getBoardByTitoloAndUtente(String titolo, int idUtente) {
        return boardDAO.getBoardByTitoloAndUtente(titolo, idUtente);
    }

    /**
     * Recupera tutte le bacheche (Board) associate a un utente specifico in base al nome utente.
     * Questo metodo utilizza il BoardDAO per ottenere la lista di bacheche associate al nome utente specificato.
     * @param username Il nome dell'utente da cui recuperare le bacheche.
     * @return Restituisce una lista di bacheche associate all'utente specificato.
     */
    public List<Board> getBoardsByUsername(String username) {
        return boardDAO.getBoardsByUsername(username);
    }

    public void updateToDo(ToDo todo) {
        toDoDAO.updateToDo(todo);
    }

    public void markAllToDosAsCompletedByBoardId(int boardId) {
        toDoDAO.markAllToDosAsCompletedByBoardId(boardId);
    }

    /**
     * Elimina tutte le bacheche (Board) associate a un utente specifico in base al suo ID.
     * Questo metodo elimina prima tutti i ToDo associati a ciascuna bacheca e poi elimina le bacheche stesse.
     * @param userId L'ID dell'utente di cui eliminare tutte le bacheche.
     */
    public void deleteAllBoardsByUserId(int userId) {
        List<Board> userBoards = boardDAO.getBoardsByBachecaId(userId);
        if (userBoards != null) {
            for (Board board : userBoards) {
                List<ToDo> todos = toDoDAO.getToDosByBoardId(board.getId());
                if (todos != null) {
                    for (ToDo todo : todos) {
                        toDoDAO.deleteToDo(todo.getId());
                    }
                }
                boardDAO.deleteBoard(board.getId());
            }
        }
    }

    /**
        * Recupera una bacheca (Board) specifica in base al suo ID.
     * Questo metodo utilizza il BoardDAO per ottenere la bacheca corrispondente all'ID specificato.
     * @param idBoard L'id della bacheca
     * @return Restituisce la bacheca corrispondente all'ID specificato, o null se l'ID non è valido o la bacheca non esiste.
     */
    public Board getBoardById(int idBoard) {
       if(idBoard <= 0) {
            logger.severe("ID della bacheca non valido: " + idBoard);
            return null; // Restituisce null se l'ID non è valido
        }
        return boardDAO.getBoardById(idBoard);

    }
}