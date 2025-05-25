package model;

import gui.MainFrame;
import controller.Controller;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UtenteManager utenteManager = new UtenteManager();
            Controller controller = new Controller(utenteManager);
            utenteManager.setController(controller);
            MainFrame mainFrame = new MainFrame(controller, utenteManager);

            mainFrame.setVisible(true);
        });
    }
}