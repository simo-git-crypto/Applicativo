// src/dao/postgresimpl/PostgresToDoDAO.java
package dao.postgresimpl;

import dao.ToDoDAO;
import database.DBConnection;
import model.StatoToDo;
import model.ToDo;
import dao.UtenteDAO; // Import UtenteDAO

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.time.LocalDate;
import java.util.logging.Logger;
/** * Implementazione di ToDoDAO per PostgreSQL.
 * Questa classe gestisce le operazioni di creazione, lettura, aggiornamento
 * ed eliminazione per gli oggetti ToDo nel database PostgreSQL.
 */
public class PostgresToDoDAO implements ToDoDAO {
    private final Connection connection;
    private final UtenteDAO utenteDAO; // Added this
    private static final Logger logger = Logger.getLogger(PostgresToDoDAO.class.getName());
    private static final String COL_DESCRIZIONE = "descrizione";
    private static final String COL_COLORE = "colore";
    private static final String COL_TITOLO = "titolo";
    private static final String COL_SCADENZA = "scadenza";
    private static final String COL_STATO = "stato";
    private static final String COL_POSIZIONE = "posizione";
    private static final String COL_ID_BOARD = "id_board";
    private static final String COL_ID_UTENTE = "id_utente";
    private static final String COL_CONDIVISO_DA_UTENTE = "condiviso_da_utente";
    public PostgresToDoDAO(Connection connection, UtenteDAO utenteDAO){ // Modified constructor
        this.connection = connection;
        this.utenteDAO = utenteDAO; // Initialize
    }
    @Override
    /**
     * Aggiunge un nuovo ToDo al database.
     * @param todo L'oggetto ToDo da aggiungere.
     */
    public void addToDo(ToDo todo) {
        String sql = "INSERT INTO todo (titolo, descrizione, scadenza, colore, stato, url, img, posizione, id_utente, condiviso_da_utente, id_board) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            stmt.setDate(3, todo.getScadenza() != null ? new java.sql.Date(todo.getScadenza().getTime()) : null);
            stmt.setString(4, todo.getColore());
            stmt.setString(5, todo.getStato().name());
            stmt.setString(6, todo.getUrl());
            stmt.setString(7, todo.getImg());
            stmt.setInt(8, todo.getPosizione());
            stmt.setInt(9, todo.getIdUtente());
            stmt.setString(10, todo.getSharedByUsername());
            stmt.setInt(11, todo.getIdBoard());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                todo.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.severe("Errore durante l'inserimento del ToDo: " + e.getMessage());
        }
    }

    @Override
    /**
     * Recupera un ToDo dal database in base al suo ID.
     * @param id L'ID del ToDo da recuperare.
     * @return L'oggetto ToDo corrispondente all'ID, o null se non trovato.
     */
    public ToDo getToDoById(int id) {
       // String sql = "SELECT * FROM todo WHERE id = ?";
        String sql = "SELECT id, titolo, descrizione, scadenza, colore, stato, url, img, posizione, id_utente, condiviso_da_utente, id_board FROM todo WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date scadenzaDate = rs.getDate(COL_SCADENZA);
                LocalDate scadenzaLocalDate = (scadenzaDate != null) ? ((java.sql.Date) scadenzaDate).toLocalDate() : null;
                ToDo todo = new ToDo(
                        rs.getInt("id"),
                        rs.getString(COL_TITOLO),
                        rs.getString(COL_DESCRIZIONE),
                        scadenzaLocalDate,
                        rs.getString(COL_COLORE),
                        rs.getString(COL_STATO),
                        rs.getString("url"),
                        rs.getString("img"),
                        rs.getInt(COL_POSIZIONE),
                        rs.getInt(COL_ID_UTENTE),
                        rs.getString(COL_CONDIVISO_DA_UTENTE),
                        rs.getInt(COL_ID_BOARD)
                );
                // Populate utenteCreatore
                int idUtenteCreatore = rs.getInt(COL_ID_UTENTE);
                if (idUtenteCreatore > 0) { // Assuming ID 0 is invalid or not used for real users
                    todo.setUtenteCreatore(utenteDAO.getUtenteById(idUtenteCreatore));
                }
                return todo;
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero del ToDo per ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    /**
     * Recupera tutti i ToDo dal database.
     * @return Una lista di tutti gli oggetti ToDo presenti nel database.
     */
    public List<ToDo> getAllToDos() {
        List<ToDo> todos = new ArrayList<>();
        String sql = "SELECT id, titolo, descrizione, scadenza, colore, stato, url, img, posizione, id_utente, condiviso_da_utente, id_board FROM todo";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Date scadenzaDate = rs.getDate(COL_SCADENZA);
                LocalDate scadenzaLocalDate = (scadenzaDate != null) ? ((java.sql.Date) scadenzaDate).toLocalDate() : null;
                ToDo todo = new ToDo(
                        rs.getInt("id"),
                        rs.getString(COL_TITOLO),
                        rs.getString(COL_DESCRIZIONE),
                        scadenzaLocalDate,
                        rs.getString(COL_COLORE),
                        rs.getString(COL_STATO),
                        rs.getString("url"),
                        rs.getString("img"),
                        rs.getInt(COL_POSIZIONE),
                        rs.getInt(COL_ID_UTENTE),
                        rs.getString(COL_CONDIVISO_DA_UTENTE),
                        rs.getInt(COL_ID_BOARD)
                );
                // Populate utenteCreatore
                int idUtenteCreatore = rs.getInt(COL_ID_UTENTE);
                if (idUtenteCreatore > 0) {
                    todo.setUtenteCreatore(utenteDAO.getUtenteById(idUtenteCreatore));
                }
                todos.add(todo);
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero di tutti i ToDo: " + e.getMessage());
        }
        return todos;
    }

    @Override
    /**
     * Aggiorna un ToDo esistente nel database.
     * @param todo L'oggetto ToDo con i dati aggiornati.
     */
    public void updateToDo(ToDo todo) {
        String sql = "UPDATE todo SET titolo = ?, descrizione = ?, scadenza = ?, colore = ?, stato = ?, url = ?, img = ?, posizione = ?, id_utente = ?, condiviso_da_utente = ?, id_board = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            stmt.setDate(3, todo.getScadenza() != null ? new java.sql.Date(todo.getScadenza().getTime()) : null);
            stmt.setString(4, todo.getColore());
            stmt.setString(5, todo.getStato().name());
            stmt.setString(6, todo.getUrl());
            stmt.setString(7, todo.getImg());
            stmt.setInt(8, todo.getPosizione());
            stmt.setInt(9, todo.getIdUtente());
            stmt.setString(10, todo.getSharedByUsername());
            stmt.setInt(11, todo.getIdBoard());
            stmt.setInt(12, todo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore durante l'aggiornamento del ToDo: " + e.getMessage());
        }
    }


    @Override
    /**
     * Elimina un ToDo dal database in base al suo ID.
     * @param id L'ID del ToDo da eliminare.
     */
    public void deleteToDo(int id) {
        String sql = "DELETE FROM todo WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
         logger.severe("Errore durante l'eliminazione del ToDo: " + e.getMessage());
        }
    }

    @Override
    /**
     * Recupera tutti i ToDo associati a una specifica board.
     * @param boardId L'ID della board per cui recuperare i ToDo.
     * @return Una lista di ToDo associati alla board specificata.
     */
    public List<ToDo> getToDosByBoardId(int boardId) {
        List<ToDo> todos = new ArrayList<>();
        String sql = "SELECT id, titolo, descrizione, scadenza, colore, stato, url, img, posizione, id_utente, condiviso_da_utente, id_board FROM todo WHERE id_board = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Date scadenzaDate = rs.getDate(COL_SCADENZA);
                LocalDate scadenzaLocalDate = (scadenzaDate != null) ? ((java.sql.Date) scadenzaDate).toLocalDate() : null;
                ToDo todo = new ToDo(
                        rs.getInt("id"),
                        rs.getString(COL_TITOLO),
                        rs.getString(COL_DESCRIZIONE),
                        scadenzaLocalDate,
                        rs.getString(COL_COLORE),
                        rs.getString(COL_STATO),
                        rs.getString("url"),
                        rs.getString("img"),
                        rs.getInt( COL_POSIZIONE),
                        rs.getInt(COL_ID_UTENTE),
                        rs.getString(COL_CONDIVISO_DA_UTENTE),
                        rs.getInt(COL_ID_BOARD)
                );
                // Populate utenteCreatore
                int idUtenteCreatore = rs.getInt(COL_ID_UTENTE);
                if (idUtenteCreatore > 0) {
                    todo.setUtenteCreatore(utenteDAO.getUtenteById(idUtenteCreatore));
                }
                todos.add(todo);
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero dei ToDo per board ID: " + e.getMessage());
        }
        return todos;
    }

    @Override
    /**
     * Segna tutti i ToDo di una specifica board come completati.
     * @param boardId L'ID della board per cui segnare i ToDo come completati.
     */
    public void markAllToDosAsCompletedByBoardId(int boardId) {
        String sql = "UPDATE todo SET stato = ? WHERE id_board = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, StatoToDo.COMPLETATO.name());
            stmt.setInt(2, boardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore durante l'aggiornamento di tutti i ToDo a completato per board ID: " + e.getMessage());
        }
    }
}