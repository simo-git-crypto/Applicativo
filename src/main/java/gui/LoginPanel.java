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

public class LoginPanel extends JPanel {
    protected JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private Controller loginController;
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.loginController = mainFrame.getController();

        setBackground(new Color(240, 240, 240));
        setLayout(new GridBagLayout());

        GridBagConstraints gbcOuter = new GridBagConstraints();
        gbcOuter.gridx = 0;
        gbcOuter.gridy = 0;
        gbcOuter.anchor = GridBagConstraints.CENTER;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        formPanel.setPreferredSize(new Dimension(350, 280));
        formPanel.setMinimumSize(new Dimension(350, 280));
        formPanel.setMaximumSize(new Dimension(350, 280));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(0, 0, 0, 0); // Remove padding between fields to make them closer
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridwidth = GridBagConstraints.REMAINDER;
        gbcForm.weightx = 1.0;

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbcForm.gridy = 0;
        gbcForm.insets = new Insets(0, 0, 15, 0); // Space below message
        formPanel.add(messageLabel, gbcForm);

        // Username field (incorporating label text)
        usernameField = new JTextField("Username");
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        usernameField.setBackground(new Color(240, 240, 240)); // Match outer background for blending
        usernameField.setForeground(Color.GRAY);
        usernameField.setOpaque(false); // Make background transparent
        usernameField.setPreferredSize(new Dimension(280, 40)); // Increased height for visual spacing
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16)); // Larger font for input text
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });
        gbcForm.gridy = 1;
        gbcForm.insets = new Insets(0, 0, 10, 0); // Space below field
        formPanel.add(usernameField, gbcForm);

        // Password field (incorporating label text)
        passwordField = new JPasswordField("Password");
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        passwordField.setBackground(new Color(240, 240, 240)); // Match outer background
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.setOpaque(false);
        passwordField.setPreferredSize(new Dimension(280, 40)); // Increased height
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*');
                    passwordField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        gbcForm.gridy = 2;
        gbcForm.insets = new Insets(0, 0, 30, 0); // Space before buttons
        formPanel.add(passwordField, gbcForm);


        Color accentBlue = new Color(0, 120, 215);

        gbcForm.insets = new Insets(0, 0, 10, 0); // Space between buttons
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(280, 45)); // Larger button
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(accentBlue);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        gbcForm.gridy = 3;
        formPanel.add(loginButton, gbcForm);

        gbcForm.insets = new Insets(0, 0, 0, 0);
        registerButton = new JButton("Registrati");
        registerButton.setPreferredSize(new Dimension(280, 45)); // Same size as login button
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(accentBlue); // Same color as login button
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(true);
        gbcForm.gridy = 4;
        formPanel.add(registerButton, gbcForm);

        add(formPanel, gbcOuter);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || username.equals("Username") || password.equals("Password")) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Username e Password sono obbligatori.");
                return;
            }

            if (loginController.verifyCredentials(username, password)) {
                model.Utente utente = loginController.getUser(username);
                mainFrame.setUtenteLoggato(utente);
                messageLabel.setForeground(Color.GREEN);
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
}