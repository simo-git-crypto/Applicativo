// src/gui/LoginPanel.java
package gui;

import controller.Controller;
import model.UtenteManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter; // Import for hover effect
import java.awt.event.MouseEvent; // Import for hover effect

/**
 * LoginPanel è una classe che estende JPanel e rappresenta il pannello di login dell'applicazione.
 * Contiene campi per l'inserimento del nome utente e della password,
 * insieme a pulsanti per il login e la registrazione.
 */
public class LoginPanel extends JPanel {
    protected JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private Controller loginController;
    private MainFrame mainFrame;
    private static final String PASSWORD = "Password";
    // All'inizio della classe RegistrationPanel
    private static final String USERNAME = "Username";
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.loginController = mainFrame.getController();

        // Colori moderni
        Color backgroundColor = new Color(248, 248, 248); // Grigio chiaro quasi bianco
        Color panelColor = new Color(255, 255, 255); // Bianco puro
        Color accentBlue = new Color(59, 130, 246); // Blu più vibrante
        Color lightGrayBorder = new Color(200, 200, 200); // Bordo grigio chiaro
        Color textColor = new Color(50, 50, 50); // Testo grigio scuro

        setBackground(backgroundColor);
        setLayout(new GridBagLayout());

        GridBagConstraints gbcOuter = new GridBagConstraints();
        gbcOuter.gridx = 0;
        gbcOuter.gridy = 0;
        gbcOuter.anchor = GridBagConstraints.CENTER;
        gbcOuter.insets = new Insets(50, 0, 50, 0); // Spaziatura esterna

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(panelColor); // Sfondo bianco per il pannello
        // Ombre leggere (simulazione con bordi)
        Border outsideBorder = new EmptyBorder(20, 20, 20, 20); // Spazio interno
        Border line = new LineBorder(lightGrayBorder, 1); // Bordo sottile
        // CompoundBorder per una leggera "ombra" o profondità
        formPanel.setBorder(new CompoundBorder(line, outsideBorder));


        formPanel.setPreferredSize(new Dimension(380, 320)); // Aumentata dimensione
        formPanel.setMinimumSize(new Dimension(380, 320));
        formPanel.setMaximumSize(new Dimension(380, 320));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 15, 10, 15); // Spaziatura interna migliorata
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridwidth = GridBagConstraints.REMAINDER; // Prende tutta la riga
        gbcForm.weightx = 1.0;

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13)); // Font leggermente più grande
        gbcForm.gridy = 0;
        gbcForm.insets = new Insets(0, 0, 20, 0); // Spazio sotto il messaggio
        formPanel.add(messageLabel, gbcForm);

        // Username field
        usernameField = new JTextField(USERNAME);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder)); // Bordo inferiore
        usernameField.setBackground(panelColor); // Corrisponde al colore del pannello
        usernameField.setForeground(Color.GRAY);
        usernameField.setOpaque(true); // Assicurati che sia opaco per il colore di sfondo
        usernameField.setPreferredSize(new Dimension(300, 45)); // Altezza aumentata
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setCaretColor(accentBlue); // Colore del cursore
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            /**
             * Gestisce l'evento di focus sul campo username.
             * Se il testo è il placeholder, lo rimuove e cambia il colore del testo.
             * Imposta un bordo accentuato quando il campo è attivo.
             * @param e  L'evento di focus.
             */
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(USERNAME)) {
                    usernameField.setText("");
                    usernameField.setForeground(textColor); // Colore testo normale
                }
                usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBlue)); // Bordo accentuato al focus
            }

            /**
             * Gestisce l'evento di perdita del focus sul campo username.
             * Se il campo è vuoto, ripristina il placeholder e il colore del testo.
             * Imposta un bordo normale quando il campo non è attivo.
             * @param e L'evento di focus.
             */
            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText(USERNAME);
                    usernameField.setForeground(Color.GRAY);
                }
                usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder)); // Bordo normale
            }
        });
        gbcForm.gridy = 1;
        gbcForm.insets = new Insets(0, 0, 15, 0); // Spazio sotto il campo
        formPanel.add(usernameField, gbcForm);

        // Password field
        passwordField = new JPasswordField(PASSWORD);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder));
        passwordField.setBackground(panelColor);
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.setOpaque(true);
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setCaretColor(accentBlue);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            /**
             * Gestisce l'evento di focus sul campo password.
             * Se il testo è il placeholder, lo rimuove e cambia il colore del testo.
             * Imposta un bordo accentuato quando il campo è attivo.
             * @param e L'evento di focus.
             */
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(PASSWORD)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*');
                    passwordField.setForeground(textColor);
                }
                passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBlue));
            }
            @Override
            /**
             * Gestisce l'evento di perdita del focus sul campo password.
             * Se il campo è vuoto, ripristina il placeholder e il colore del testo.
             * Imposta un bordo normale quando il campo non è attivo.
             * @param e L'evento di focus.
             */
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(PASSWORD);
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
                passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder));
            }
        });
        gbcForm.gridy = 2;
        gbcForm.insets = new Insets(0, 0, 30, 0); // Spazio prima dei bottoni
        formPanel.add(passwordField, gbcForm);


        Dimension buttonSize = new Dimension(300, 50); // Bottoni più grandi
        Font buttonFont = new Font("Arial", Font.BOLD, 17); // Font più grande e audace


        loginButton = new JButton("Login");
        loginButton.setPreferredSize(buttonSize);
        loginButton.setFont(buttonFont);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(accentBlue);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false); // Rimuove il bordo predefinito
        loginButton.setOpaque(true);
        // Bordi arrotondati (simulati con empty border)
        loginButton.setBorder(new EmptyBorder(10, 25, 10, 25)); // Aggiunge padding per un aspetto "spazioso"
        // Effetto hover
        addHoverEffect(loginButton, accentBlue, accentBlue.darker());

        gbcForm.gridy = 3;
        gbcForm.insets = new Insets(0, 0, 15, 0); // Spazio tra i bottoni
        formPanel.add(loginButton, gbcForm);

        // Register Button
        registerButton = new JButton("Registrati");
        registerButton.setPreferredSize(buttonSize);
        registerButton.setFont(buttonFont);
        registerButton.setForeground(accentBlue); // Testo blu
        registerButton.setBackground(panelColor); // Sfondo bianco
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(true);
        registerButton.setBorder(new LineBorder(accentBlue, 1, true)); // Bordo blu arrotondato (true per arrotondamento)
        // Effetto hover
        addHoverEffect(registerButton, panelColor, new Color(240, 240, 240)); // Sfondo più scuro al hover

        gbcForm.gridy = 4;
        gbcForm.insets = new Insets(0, 0, 0, 0);
        formPanel.add(registerButton, gbcForm);

        add(formPanel, gbcOuter);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || username.equals("Username") || password.equals(PASSWORD)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Username e Password sono obbligatori.");
                return;
            }

            if (loginController.verifyCredentials(username, password)) {
                model.Utente utente = loginController.getUserByUsername(username);                mainFrame.setUtenteLoggato(utente);
                messageLabel.setForeground(new Color(40, 167, 69)); // Verde per successo
                messageLabel.setText("Login effettuato");
                mainFrame.showDashboard();
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Credenziali errate");
            }
        });

        registerButton.addActionListener(e -> {
            mainFrame.showRegistration();
        });
    }

    /**
     * Aggiunge un effetto hover ai pulsanti per migliorare l'interazione utente.
     * Cambia il colore di sfondo quando il mouse entra ed esce dal pulsante.
     *
     * @param button Il pulsante a cui aggiungere l'effetto hover.
     * @param normalColor Il colore di sfondo normale del pulsante.
     * @param hoverColor Il colore di sfondo quando il mouse è sopra il pulsante.
     */
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            /**
             * Gestisce l'evento di entrata del mouse sul pulsante.
             * Cambia il colore di sfondo del pulsante al colore di hover.
             *
             * @param e L'evento del mouse.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
/**
             * Gestisce l'evento di uscita del mouse dal pulsante.
             * Ripristina il colore di sfondo normale del pulsante.
             *
             * @param e L'evento del mouse.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }
}