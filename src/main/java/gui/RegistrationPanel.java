// src/gui/RegistrationPanel.java
package gui;

import dao.postgresimpl.PostgresUtenteDAO;
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
 * RegistrattionPanel è una classe che rappresenta il pannello di registrazione
 * dell'utente nella GUI dell'applicazione.
 */
public class RegistrationPanel extends JPanel {
    protected JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private JLabel messageLabel;
    private MainFrame mainFrame;
    private UtenteManager utenteManager;

    private Color backgroundColor;
    private Color panelColor;
    private Color accentBlue;
    private Color lightGrayBorder;
    private Color textColor;


    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    /**
     * Costruttore della classe RegistrationPanel.
     * Inizializza il pannello di registrazione con i campi necessari e i pulsanti.
     *
     * @param mainFrame Riferimento al frame principale dell'applicazione.
     */
    public RegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.utenteManager = mainFrame.getUtenteManager();

        /**
         * Inizializza i colori e il layout del pannello di registrazione.
         */
        setupColorsAndLayout();

    /** * Crea un pannello per il modulo di registrazione e lo configura.
         */
        JPanel formPanel = createFormPanel();

/**
  * Aggiunge i componenti al pannello del modulo di registrazione.
         */
        addComponentsToForm(formPanel);

        /**
         * Aggiunge il pannello del modulo di registrazione al pannello principale.
         * @param formPanel Il pannello del modulo di registrazione da aggiungere.
         */
        addFormPanelToMain(formPanel);

        /**
         * Imposta le azioni dei pulsanti di registrazione e ritorno al login.
         * @param registerButton Il pulsante di registrazione.
         * @param backToLoginButton Il pulsante per tornare al login.
         */
        setupButtonActions();
    }

    /**
     * Imposta i colori e il layout del pannello di registrazione.
     */
    private void setupColorsAndLayout() {
        backgroundColor = new Color(248, 248, 248);
        panelColor = new Color(255, 255, 255);
        accentBlue = new Color(59, 130, 246);
        lightGrayBorder = new Color(200, 200, 200);
        textColor = new Color(50, 50, 50);

        setBackground(backgroundColor);
        setLayout(new GridBagLayout());
    }

    /**
     * Crea un pannello per il modulo di registrazione con un layout a griglia.
     * * @return Il pannello del modulo di registrazione configurato.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(panelColor);

        Border outsideBorder = new EmptyBorder(20, 20, 20, 20);
        Border line = new LineBorder(lightGrayBorder, 1);
        formPanel.setBorder(new CompoundBorder(line, outsideBorder));

        formPanel.setPreferredSize(new Dimension(380, 420));
        formPanel.setMinimumSize(new Dimension(380, 420));
        formPanel.setMaximumSize(new Dimension(380, 420));

        return formPanel;
    }

    /**
     * Aggiunge i componenti al pannello del modulo di registrazione.
     * @param formPanel Il pannello del modulo di registrazione a cui aggiungere i componenti.
     */
    private void addComponentsToForm(JPanel formPanel) {
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 15, 10, 15);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridwidth = GridBagConstraints.REMAINDER;
        gbcForm.weightx = 1.0;

        // Add message label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        gbcForm.gridy = 0;
        gbcForm.insets = new Insets(0, 0, 20, 0);
        formPanel.add(messageLabel, gbcForm);

        // Add input fields
        addInputField(formPanel, gbcForm, usernameField = createTextField(USERNAME), 1);
        addInputField(formPanel, gbcForm, passwordField = createPasswordField(PASSWORD), 2);
        addInputField(formPanel, gbcForm, confirmPasswordField = createPasswordField("Confirm Password"), 3);

        // Add buttons
        addButton(formPanel, gbcForm, registerButton = createRegisterButton(), 4);
        addButton(formPanel, gbcForm, backToLoginButton = createBackToLoginButton(), 5);
    }

    /**
     * Aggiunge il pannello del modulo di registrazione al pannello principale.
     * * @param formPanel Il pannello del modulo di registrazione da aggiungere al pannello principale.
     */
    private void addFormPanelToMain(JPanel formPanel) {
        GridBagConstraints gbcOuter = new GridBagConstraints();
        gbcOuter.gridx = 0;
        gbcOuter.gridy = 0;
        gbcOuter.anchor = GridBagConstraints.CENTER;
        gbcOuter.insets = new Insets(50, 0, 50, 0);
        add(formPanel, gbcOuter);
    }

    /**
     * Imposta le azioni dei pulsanti di registrazione e ritorno al login.
     */
    private void setupButtonActions() {
        registerButton.addActionListener(e -> handleRegistration());
        backToLoginButton.addActionListener(e -> mainFrame.showLogin());
    }

    /**
     * Crea un campo di testo con un placeholder e gestisce il focus per mostrare/nascondere il placeholder.
     * @param placeholder Il testo di placeholder da visualizzare nel campo di testo.
     * @return Il campo di testo configurato con il placeholder.
     */
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder));
        field.setBackground(panelColor);
        field.setForeground(Color.GRAY);
        field.setOpaque(true);
        field.setPreferredSize(new Dimension(300, 45));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setCaretColor(accentBlue);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(textColor);
                }
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBlue));
            }

            /**
             * Gestisce l'evento di perdita del focus per il campo di testo.
             * Se il campo è vuoto, ripristina il placeholder e il colore del testo.
             * @param e l'evento di focus perso
             */
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder));
            }
        });

        return field;
    }

    /**
     * Crea un campo di password con un placeholder e gestisce il focus per mostrare/nascondere il placeholder.
     * * @param placeholder Il testo di placeholder da visualizzare nel campo di password.
     * @return Il campo di password configurato con il placeholder.
     */
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder));
        field.setBackground(panelColor);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);
        field.setOpaque(true);
        field.setPreferredSize(new Dimension(300, 45));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setCaretColor(accentBlue);


        field.addFocusListener(new FocusAdapter() {
            @Override
            /**
             * Gestisce l'evento di focus guadagnato per il campo di password.
             * Se il campo contiene il placeholder, lo rimuove e imposta il colore del testo.
             * @param e l'evento di focus guadagnato
             */
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('*');
                    field.setForeground(textColor);
                }
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBlue));
            }

            @Override
            /**
             * Gestisce l'evento di perdita del focus per il campo di password.
             * Se il campo è vuoto, ripristina il placeholder e il colore del testo.
             * @param e l'evento di focus perso
             */
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setEchoChar((char) 0);
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGrayBorder));
            }
        });

        return field;
    }

    /**
     * Crea un pulsante di registrazione con stile e hover effect.
     * @return Il pulsante di registrazione configurato.
     */
    private JButton createRegisterButton() {

        JButton button = new JButton("Registrati");
        button.setPreferredSize(new Dimension(300, 50));
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setBackground(accentBlue);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        addHoverEffect(button, accentBlue, accentBlue.darker());
        return button;
    }

    /**
     * Crea un pulsante per tornare al login con stile e hover effect.
     * @return Il pulsante per tornare al login configurato.
     */
    private JButton createBackToLoginButton() {
        JButton button = new JButton("Indietro al Login");
        button.setPreferredSize(new Dimension(300, 50));
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setForeground(accentBlue);
        button.setBackground(panelColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new LineBorder(accentBlue, 1, true));
        addHoverEffect(button, panelColor, new Color(240, 240, 240));
        return button;
    }

    /**
     * Aggiunge un campo di input al pannello con le specifiche di layout.
     * @param panel Il pannello a cui aggiungere il campo di input.
     * @param gbc Il GridBagConstraints per il layout del pannello.
     * @param field Il campo di input da aggiungere (JTextField o JPasswordField).
     * @param gridy La posizione verticale del campo nel layout a griglia.
     */
    private void addInputField(JPanel panel, GridBagConstraints gbc, JComponent field, int gridy) {
        gbc.gridy = gridy;
        gbc.insets = new Insets(0, 0, gridy == 3 ? 30 : 15, 0);
        panel.add(field, gbc);
    }

    /**
     * Aggiunge un pulsante al pannello con le specifiche di layout.
     * @param panel Il pannello a cui aggiungere il pulsante.
     * @param gbc IL GridBagConstraints per il layout del pannello.
     * @param button Il pulsante da aggiungere (JButton).
     * @param gridy La posizione verticale del pulsante nel layout a griglia.
     */
    private void addButton(JPanel panel, GridBagConstraints gbc, JButton button, int gridy) {
        gbc.gridy = gridy;
        gbc.insets = new Insets(0, 0, gridy == 5 ? 0 : 15, 0);
        panel.add(button, gbc);
    }

    /**
     * Gestisce la registrazione dell'utente.
     * Controlla i campi di input, verifica se le password corrispondono e registra l'utente tramite UtenteManager.
     * Se la registrazione ha successo, mostra un messaggio di successo e torna al login.
     * Se fallisce, mostra un messaggio di errore.
     */
    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!validateRegistrationFields(username, password, confirmPassword)) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorMessage("Le password non corrispondono.");
            return;
        }

        if (utenteManager.register(username, password)) {
            showSuccessMessage("Registrazione avvenuta con successo! Puoi ora effettuare il login.");
            mainFrame.showLogin();
            clearFields();
        } else {
            showErrorMessage("Username già esistente.");
        }
    }

    /**
     * Verifica i campi di registrazione per assicurarsi che non siano vuoti
     * e che non contengano i valori predefiniti.
     * * @return true se tutti i campi sono validi, false altrimenti.
     * @param username L'username inserito dall'utente.
     * @param password La password inserita dall'utente.
     * @param confirmPassword La conferma della password inserita dall'utente.
     * @return true se tutti i campi sono corretti, false altrimenti.
     */
    private boolean validateRegistrationFields(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                username.equals(USERNAME) || password.equals(PASSWORD) || confirmPassword.equals("Confirm Password")) {
            showErrorMessage("Tutti i campi sono obbligatori.");
            return false;
        }
        return true;
    }

    /**
     * Mostra un messaggio di errore nel label dei messaggi.
     * Imposta il colore del testo su rosso per indicare un errore.
     * @param message Il messaggio di errore da visualizzare.
     */
    private void showErrorMessage(String message) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(message);
    }

    /**
     * Mostra un messaggio di successo nel label dei messaggi.
     * @param message Il messaggio di successo da visualizzare.
     */
    private void showSuccessMessage(String message) {
        messageLabel.setForeground(new Color(40, 167, 69));
        messageLabel.setText("Registrazione avvenuta con successo!");
        JOptionPane.showMessageDialog(this, message);
    }


    /**
     * Pulisce i campi di input del pannello di registrazione,
     */
    public void clearFields() {
        usernameField.setText(USERNAME);
        usernameField.setForeground(Color.GRAY);
        passwordField.setText(PASSWORD);
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.GRAY);
        confirmPasswordField.setText("Confirm Password");
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.setForeground(Color.GRAY);
        messageLabel.setText("");
    }

    /**
     * Aggiunge un effetto hover a un pulsante.
     * * Cambia il colore di sfondo del pulsante quando il mouse entra ed esce.
     * @param button Il pulsante a cui aggiungere l'effetto hover.
     * @param normalColor Il colore di sfondo normale del pulsante.
     * @param hoverColor Il colore di sfondo del pulsante quando il mouse è sopra.
     */
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            /**
             * Gestisce l'evento di ingresso del mouse sul pulsante.
             * Cambia il colore di sfondo del pulsante al colore di hover.
             * @param e l'evento del mouse
             */
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            /**
             * Gestisce l'evento di uscita del mouse dal pulsante.
             * Ripristina il colore di sfondo normale del pulsante.
             * @param e l'evento del mouse
             */
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }
}