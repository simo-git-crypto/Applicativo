// MainFrame.java
package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private RegistrationPanel registrationPanel; // New: RegistrationPanel
    private Utente utenteLoggato;
    private model.UtenteManager utenteManager;
    private JPanel bottomPanelLogin;
    private JPanel bottomPanelDashboard;
    private JButton logoutButton;
    private JButton exitButton;

    private final Controller controller;

    public MainFrame(Controller controller, UtenteManager utenteManager) {
        this.utenteManager = utenteManager;
        setTitle("Gestore ToDo");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.controller = controller;

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        cardPanel.add(loginPanel, "login");

        registrationPanel = new RegistrationPanel(this);
        cardPanel.add(registrationPanel, "register");

        add(cardPanel, BorderLayout.CENTER);

        exitButton = new JButton("Esci");
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.addActionListener(e -> System.exit(0));

        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.addActionListener(e -> {
            if (utenteLoggato != null) {
                controller.getBoardController().salvaBacheche(utenteLoggato.getUsername());
            }
            setUtenteLoggato(null);
            showLogin();
        });

        bottomPanelLogin = new JPanel(new BorderLayout());
        bottomPanelLogin.add(exitButton, BorderLayout.CENTER);

        bottomPanelDashboard = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanelDashboard.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanelDashboard.add(logoutButton);
        bottomPanelDashboard.add(exitButton);

        add(bottomPanelLogin, BorderLayout.SOUTH);
        showLogin();
        setVisible(true);
    }

    public void showLogin() {
        remove(bottomPanelDashboard);
        add(bottomPanelLogin, BorderLayout.SOUTH);
        cardLayout.show(cardPanel, "login");
        loginPanel.usernameField.requestFocusInWindow();
        revalidate();
        repaint();
    }

    public void showDashboard() {
        remove(bottomPanelLogin);
        add(bottomPanelDashboard, BorderLayout.SOUTH);
        if (dashboardPanel != null) {
            cardPanel.remove(dashboardPanel);
        }
        dashboardPanel = new DashboardPanel(this);
        cardPanel.add(dashboardPanel, "dashboard");
        cardLayout.show(cardPanel, "dashboard");
        revalidate();
        repaint();
    }

    public void showRegistration() {
        remove(bottomPanelDashboard);
        add(bottomPanelLogin, BorderLayout.SOUTH);
        cardLayout.show(cardPanel, "register");
        registrationPanel.clearFields();
        registrationPanel.usernameField.requestFocusInWindow();
        revalidate();
        repaint();
    }

    public void setUtenteLoggato(Utente utente) {
        this.utenteLoggato = utente;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public Controller getController() {
        return controller;
    }

    public model.UtenteManager getUtenteManager() {
        return utenteManager;
    }
}