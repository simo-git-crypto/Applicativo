// src/model/Main.java
package model;

import gui.*;
import controller.Controller;
import dao.*;
import dao.postgresimpl.*;
import database.DBConnection;
import javax.swing.*;
import java.sql.Connection;
import java.util.logging.Logger;
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {

        Connection connection = DBConnection.getConnection();
        if (connection == null) {
         LOGGER.severe("Impossibile avviare l'applicazione: connessione al database fallita.");
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> {
            UtenteDAO utenteDAO = new PostgresUtenteDAO(connection);
            ToDoDAO toDoDAO = new PostgresToDoDAO(connection, utenteDAO);
            BoardDAO boardDAO = new PostgresBoardDAO(connection);
            UtenteManager utenteManager = new UtenteManager(utenteDAO) {
                @Override
                public boolean register(String username, String password) {

                    if (utenteDAO.getUtenteByUsername(username) != null) return false;
                    Utente nuovo = new Utente(username, password);
                    utenteDAO.addUtente(nuovo);
                    LOGGER.info("Registrato con id: " + nuovo.getId());
                    return true;
                }
            };
            BoardManager boardManager = new BoardManager(boardDAO, toDoDAO, utenteDAO);
            Controller controller = new Controller(utenteManager, boardManager);
            utenteManager.setController(controller);
            MainFrame mainFrame = new MainFrame(controller, utenteManager);

            mainFrame.setVisible(true);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> DBConnection.closeConnection()));
        });
    }
}