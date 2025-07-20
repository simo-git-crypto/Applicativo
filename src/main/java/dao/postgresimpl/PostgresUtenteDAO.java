package dao.postgresimpl;

import database.DBConnection;
import model.Utente;
import dao.UtenteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implementazione di UtenteDAO per PostgreSQL.
 * Questa classe gestisce le operazioni di Creazione, Lettura, Aggiornamento ed eliminazione(CRUD) sugli utenti nel database PostgreSQL.
 */
public class PostgresUtenteDAO implements UtenteDAO {
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(PostgresUtenteDAO.class.getName());
    private static final String PASSWORD_COLUMN = "password";
    public PostgresUtenteDAO(Connection connection) {
        this.connection = connection;
    }
    @Override
    /** Aggiunge un nuovo utente al database.
     * @param utente L'Utente da aggiungere.
     */
    public void addUtente(Utente utente) {
        String sql ="INSERT INTO utente(username,password) VALUES (?, ?) RETURNING id";
        try(PreparedStatement stmt =DBConnection.getConnection().prepareStatement(sql)){
            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getPassword());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int generateId = rs.getInt("id");
                utente.setId(generateId);
            }
        }   catch (SQLException e) {

            LOGGER.log(Level.SEVERE, "Errore durante la creazione del utente: " + e.getMessage(), e); //NOSONAR //Disabilitato poichè le lambda non sono più supportate da java.util.logging.Logger
        }
    }
    /**
     * Registra un nuovo utente nel database.
     * @param username Il nome utente da registrare.
     * @param password La password dell'utente da registrare.
     * @return true se la registrazione è avvenuta con successo, false se l'utente esiste già.
     */
    public boolean register(String username, String password) {
        if (getUtenteByUsername(username) != null) {
            return false;
        }
        addUtente(new Utente(username, password));
        return true;
    }

    @Override
    /**
     * Recupera un utente dal database in base al nome utente.
     * @param username Il nome utente da cercare.
     * @return L'oggetto Utente corrispondente, o null se non trovato.
     */
    public Utente getUtenteByUsername(String username) {
        String sql = "SELECT id, username, password FROM utente WHERE username = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String password = rs.getString(PASSWORD_COLUMN);
                int id = rs.getInt("id");
                Utente utente = new Utente(username, password);
                utente.setId(id);
                return utente;
            }
        } catch (SQLException e) {


            LOGGER.log(Level.SEVERE, "Errore durante il recupero dell'utente per ID: " + e.getMessage(), e); //NOSONAR
        }
        return null;
    }

    @Override
    /**
     * Aggiorna le informazioni di un utente nel database.
     * @param utente L'oggetto Utente con le informazioni aggiornate.
     */
    public void updateUtente(Utente utente) {
        String sql = "UPDATE utente SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, utente.getPassword());
            stmt.setString(2, utente.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {

            LOGGER.log(Level.SEVERE, "Errore durante l'aggiornamento dell'utente: " + e.getMessage(), e); //NOSONAR
        }
    }

    @Override
    /**
     * Elimina un utente dal database in base all'ID.
     * @param id L'ID dell'utente da eliminare.
     */
    public void deleteUtenteById(int id) {
        String sql = "DELETE FROM utente WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'eliminazione dell'utente per ID: " + e.getMessage(), e); //NOSONAR
        }
    }

    @Override
    /**
     * Recupera tutti gli utenti dal database.
     * @return Una lista di oggetti Utente.
     */
    public List<Utente> getAllUtenti() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT id, username, password FROM utente";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString(PASSWORD_COLUMN);
                utenti.add(new Utente(username, password));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero di tutti gli utenti: " + e.getMessage(), e); //NOSONAR
        }
        return utenti;
    }

    @Override
    /**
     * Recupera un utente dal database in base all'ID.
     * @param id L'ID dell'utente da cercare.
     * @return L'Utente corrispondente, o null se non trovato.
     */
    public Utente getUtenteById(int id) {
        String sql = "SELECT id, username, password FROM utente WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString(PASSWORD_COLUMN);
                Utente utente = new Utente(username, password);
                utente.setId(id);
                return utente;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero dell'utente per ID: " + e.getMessage(), e); //NOSONAR
        }
        return null;
    }
}