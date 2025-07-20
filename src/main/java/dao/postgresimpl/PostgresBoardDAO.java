package dao.postgresimpl; //definisce il package dao.postgresimpl

import dao.*; //importa tutte le classi del package dao
import database.DBConnection;
import model.*; //importa tutte le classi del package model
import java.util.logging.Logger;
import java.sql.*; //importa le classi necessarie per la gestione delle operazioni SQL
import java.util.ArrayList; //importa la classe ArrayList per la gestione delle liste
import java.util.List; //importa l'interfaccia List per la gestione delle liste
/** * Questa classe implementa l'interfaccia BoardDAO
 * per gestire le operazioni sulle bacheche nel database PostgreSQL.
 *
 */
public class PostgresBoardDAO implements BoardDAO { // Questa classe implementa l'interfaccia BoardDAO per gestire le operazioni sulle bacheche nel database PostgreSQL
    private static final String COLUMN_ID_UTENTE = "id_utente";
    private final Connection connection; // Connessione al database PostgreSQL
    private static final String COLUMN_TITOLO = "titolo";
    private static final Logger logger = Logger.getLogger(PostgresBoardDAO.class.getName());
    public PostgresBoardDAO(Connection connection) { // Costruttore che accetta una connessione al database
        this.connection = connection; // Inizializza la connessione al database
    }

    @Override//Questa annotazione indica che il metodo sta implementando un metodo dell'interfaccia BoardDAO
    /**
     * Questo metodo aggiunge una nuova board al database..
     * @throws SQLException Se si verifica un errore durante l'inserimento della board.
     *  Se la board esiste già, viene restituito un messaggio di log informativo
     *  e l'ID della board esistente viene impostato sull'oggetto board passato come parametro.
     *  Se la board non esiste, viene inserita nel database e l'ID generato viene impostato
     *  sull'oggetto board.
     *  Questo metodo è utile per evitare duplicati di board con lo stesso titolo per lo stesso utente.
     * @param board La board da aggiungere al database.
     */
    public void addBoard(Board board) {
        // controllo se esiste già una board con lo stesso titolo e id utente
        Board existing = getBoardByTitoloAndUtente(board.getTitolo(), board.getIdUtente()); // Recupera la board esistente con lo stesso titolo e ID utente
        if (existing != null) {
            logger.info("La board \"" + board.getTitolo() + "\" esiste già per l'utente ID " + board.getIdUtente());

            board.setId(existing.getId());//Imposta l'ID della board esistente all'oggetto board passato come parametro
            return;
        }

        String sql = "INSERT INTO board (titolo, id_utente) VALUES (?, ?) RETURNING id"; // Query per inserire la board e restituire l'id generato
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {  // apre una PreparedStatement per eseguire la query
            stmt.setString(1, board.getTitolo()); // Imposta il titolo della board
            stmt.setInt(2, board.getIdUtente());// Imposta l'ID dell'utente associato alla board
            ResultSet rs = stmt.executeQuery(); // Use executeQuery for RETURNING
            if (rs.next()) { // Controlla se la query ha restituito un risultato
                board.setId(rs.getInt(1)); // Imposta l'ID della board appena inserita
            }
        } catch (SQLException e) { // Viene eseguito se si verifica un'eccezione durante l'esecuzione della query
            logger.severe("Errore durante l'inserimento della board: " + e.getMessage());// Stampa un messaggio di errore se si verifica un'eccezione
        }
    }
    @Override //Questa annotazione indica che il metodo sta implementando un metodo dell'interfaccia BoardDAO
    /**
     * Questo metodo recupera una board dal database in base al titolo e all'ID dell'utente.
     * @param titolo Il titolo della board da cercare.
     * @param idUtente L'ID dell'utente proprietario della board.
     * @return La board trovata, o null se non esiste.
     */
    public Board getBoardByTitoloAndUtente(String titolo, int idUtente) {
        String sql = "SELECT id, titolo, id_utente FROM board WHERE titolo = ? AND id_utente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { // Crea una PreparedStatement per eseguire la query
            stmt.setString(1, titolo); // Imposta il titolo della board
            stmt.setInt(2, idUtente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idBoard = rs.getInt("id");
                return new Board(idBoard, titolo, idUtente);
            }
        } catch (SQLException e) {
            logger.severe("Errore nel recupero board per titolo e utente: " + e.getMessage());
        }
        return null;
    }

    @Override //Questa annotazione indica che il metodo sta implementando un metodo dell'interfaccia BoardDAO
    /**
     * Questo metodo recupera una board dal database in base al suo ID.
     * @param id L'ID della board da cercare.
     * @return La board trovata, o null se non esiste.
     */
    public Board getBoardById(int id) {
        String sql = "SELECT id, titolo, id_utente FROM board WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idBoard = rs.getInt("id");
                String titolo = rs.getString(COLUMN_TITOLO);
                int idUtente = rs.getInt(COLUMN_ID_UTENTE);

                return new Board(idBoard, titolo, idUtente);
            }
        } catch (SQLException e) {
            logger.severe("Errore nel recupero della board per ID: " + e.getMessage());
        }
        return null;
    }
    @Override // Questa annotazione indica che il metodo sta implementando un metodo dell'interfaccia BoardDAO
    /**
     * Questo metodo recupera tutte le board associate a un utente specifico in base al nome utente.
     * @param username Il nome utente per il quale si desidera recuperare le board.
     * @return Una lista di board associate all'utente, o una lista vuota se non ci sono board.
     */
    public List<Board> getBoardsByUsername(String username) {
        List<Board> boards = new ArrayList<>();
        String userQuery = "SELECT id FROM utente WHERE username = ?";
        String boardQuery = "SELECT id, titolo, id_utente FROM board WHERE id_utente = ?";
        try (
                PreparedStatement userStmt = connection.prepareStatement(userQuery);
        ) {
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("id");

                try (PreparedStatement boardStmt = connection.prepareStatement(boardQuery)) {
                    boardStmt.setInt(1, userId);
                    ResultSet boardRs = boardStmt.executeQuery();

                    while (boardRs.next()) {
                        int idBoard = boardRs.getInt("id");
                        String titolo = boardRs.getString(COLUMN_TITOLO);
                        int idUtente = boardRs.getInt(COLUMN_ID_UTENTE);
                        Board board = new Board(idBoard, titolo, idUtente);
                        boards.add(board);
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle board per username: " + e.getMessage());
        }

        return boards;
    }
    @Override
    /**
     * Questo metodo recupera tutte le board dal database.
     * @return Una lista di tutte le board presenti nel database.
     */
    public List<Board> getAllBoards() {
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT id, titolo, id_utente FROM board";
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int idBoard = rs.getInt("id");
                String titolo = rs.getString(COLUMN_TITOLO);
                int idUtente = rs.getInt(COLUMN_ID_UTENTE);
                Board board = new Board(idBoard, titolo, idUtente);
                boards.add(board);
            }
        } catch (SQLException e) {
            logger.severe("Errore nel recupero di tutte le board: " + e.getMessage());
        }
        return boards;
    }

    @Override
    /**
     * Questo metodo aggiorna il titolo di una board nel database.
     * @param board La board con l'ID e il nuovo titolo da aggiornare.
     */
    public void updateBoard(Board board) {
        String sql = "UPDATE board SET " + COLUMN_TITOLO + " = ? WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, board.getTitolo());
            stmt.setInt(2, board.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore durante l'aggiornamento della board: " + e.getMessage());
        }
    }

    @Override
    /**
     * Questo metodo elimina una board dal database in base al suo ID.
     * @param id L'ID della board da eliminare.
     */
    public void deleteBoard(int id) {
        String sql = "DELETE FROM board WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore durante l'eliminazione della board: " + e.getMessage());
        }
    }

    @Override
    /**
     * Questo metodo recupera tutte le board associate a un utente specifico in base all'ID dell'utente.
     * @param idUtente L'ID dell'utente per il quale si desidera recuperare le board.
     * @return Una lista di board associate all'utente, o una lista vuota se non ci sono board.
     */
    public List<Board> getBoardsByBachecaId(int idUtente) { // Renamed parameter for clarity
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT id, titolo, id_utente FROM board WHERE id_utente = ?";
        //  String sql = "SELECT * FROM board WHERE id_utente = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idBoard = rs.getInt("id");
                String titolo = rs.getString(COLUMN_TITOLO);
                int idUtenteDb = rs.getInt(COLUMN_ID_UTENTE);
                Board board = new Board(idBoard, titolo, idUtenteDb);
                boards.add(board);
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle board per utente ID: " + e.getMessage());
        }
        return boards;
    }



    @Override
    /**
     * Questo metodo elimina tutte le board associate a un utente specifico.
     * @param userId L'ID dell'utente per il quale si desidera eliminare tutte le board.
     */
    public void deleteAllBoardsByUserId(int userId) {
        String sql = "DELETE FROM board WHERE id_utente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore durante l'eliminazione di tutte le board per utente: " + e.getMessage());
        }
    }
}
