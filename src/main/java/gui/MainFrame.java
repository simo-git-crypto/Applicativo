package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
/** * Classe principale che rappresenta la finestra principale dell'applicazione.
 * Gestisce il layout a schede per le diverse viste (login, dashboard, registrazione).
 */

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private RegistrationPanel registrationPanel;
    private Utente utenteLoggato;
    private UtenteManager utenteManager;
    private JPanel bottomPanelLogin;
    private JPanel bottomPanelDashboard;
    private JButton logoutButton;
    private JButton exitButton;

    private final Controller controller;

    /**
     * Costruttore della classe MainFrame.
     * Inizializza la finestra principale, i pannelli di login, dashboard e registrazione,
     * e i pulsanti di logout ed exit.
     * @param controller Controller per gestire le operazioni dell'applicazione.
     * @param utenteManager Gestore degli utenti per le operazioni di login e registrazione.
     */
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

    /**
     * Mostra il pannello di login.
     * Rimuove il pannello della dashboard se presente e aggiunge il pannello di login.
     * Imposta il layout a schede per visualizzare il pannello di login.
     * * Rende visibile il campo di input per il nome utente e lo mette a fuoco.
     */
    public void showLogin() {
        remove(bottomPanelDashboard);
        add(bottomPanelLogin, BorderLayout.SOUTH);
        cardLayout.show(cardPanel, "login");
        loginPanel.usernameField.requestFocusInWindow();
        revalidate();
        repaint();
    }

    /**
     * Mostra il pannello della dashboard.
     * Rimuove il pannello di login se presente e aggiunge il pannello della dashboard.
     * Crea un nuovo pannello DashboardPanel e lo aggiunge al layout a schede.
     * Rende visibile il pannello della dashboard e lo mette in primo piano.
     */
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

    /**
     * Mostra il pannello di registrazione.
     * Rimuove il pannello della dashboard se presente e aggiunge il pannello di login.
     * Imposta il layout a schede per visualizzare il pannello di registrazione.
     * Pulisce i campi del pannello di registrazione e mette a fuoco il campo nome utente.
     */
    public void showRegistration() {
        remove(bottomPanelDashboard);
        add(bottomPanelLogin, BorderLayout.SOUTH);
        cardLayout.show(cardPanel, "register");
        registrationPanel.clearFields();
        registrationPanel.usernameField.requestFocusInWindow();
        revalidate();
        repaint();
    }

    /**
     * Imposta l'utente attualmente loggato.
     * Aggiorna il controller con l'utente loggato.
     * @param utente L'utente da impostare come loggato.
     */
    public void setUtenteLoggato(Utente utente) {
        this.utenteLoggato = utente;
        controller.setUtenteLoggato(utente); // Set in controller as well
    }

    /**
     * Ottiene l'utente attualmente loggato.
     * @return L'utente loggato, o null se nessun utente è loggato.
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Ottiene il controller associato a questa finestra principale.
     * @return Il controller dell'applicazione.
     */
    public Controller getController() {
        return controller;
    }

    /** * Ottiene il gestore degli utenti associato a questa finestra principale.
     * Questo gestore è utilizzato per le operazioni di login e registrazione degli utenti.
     * @return Il gestore degli utenti (UtenteManager) associato a questa finestra principale.
     */
    public UtenteManager getUtenteManager() {
        return utenteManager;
    }
}