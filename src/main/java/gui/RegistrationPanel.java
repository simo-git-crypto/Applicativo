package gui;

import model.UtenteManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegistrationPanel extends JPanel {
    protected JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private JLabel messageLabel;
    private MainFrame mainFrame;
    private UtenteManager utenteManager;

    public RegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.utenteManager = mainFrame.getUtenteManager();

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

        formPanel.setPreferredSize(new Dimension(350, 380)); // Enlarged
        formPanel.setMinimumSize(new Dimension(350, 380));
        formPanel.setMaximumSize(new Dimension(350, 380));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 0, 5, 0);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridwidth = GridBagConstraints.REMAINDER;
        gbcForm.weightx = 1.0;

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbcForm.gridy = 0;
        gbcForm.insets = new Insets(0, 0, 15, 0);
        formPanel.add(messageLabel, gbcForm);

        // Username field
        usernameField = new JTextField("Username");
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        usernameField.setBackground(new Color(240, 240, 240));
        usernameField.setForeground(Color.GRAY);
        usernameField.setOpaque(false);
        usernameField.setPreferredSize(new Dimension(280, 40));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
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
        gbcForm.insets = new Insets(0, 0, 10, 0);
        formPanel.add(usernameField, gbcForm);

        // Password field
        passwordField = new JPasswordField("Password");
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        passwordField.setBackground(new Color(240, 240, 240));
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.setOpaque(false);
        passwordField.setPreferredSize(new Dimension(280, 40));
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
        gbcForm.insets = new Insets(0, 0, 10, 0);
        formPanel.add(passwordField, gbcForm);

        // Confirm Password field
        confirmPasswordField = new JPasswordField("Confirm Password");
        confirmPasswordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        confirmPasswordField.setBackground(new Color(240, 240, 240));
        confirmPasswordField.setForeground(Color.GRAY);
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.setOpaque(false);
        confirmPasswordField.setPreferredSize(new Dimension(280, 40));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(confirmPasswordField.getPassword()).equals("Confirm Password")) {
                    confirmPasswordField.setText("");
                    confirmPasswordField.setEchoChar('*');
                    confirmPasswordField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (new String(confirmPasswordField.getPassword()).isEmpty()) {
                    confirmPasswordField.setText("Confirm Password");
                    confirmPasswordField.setEchoChar((char) 0);
                    confirmPasswordField.setForeground(Color.GRAY);
                }
            }
        });
        gbcForm.gridy = 3;
        gbcForm.insets = new Insets(0, 0, 30, 0);
        formPanel.add(confirmPasswordField, gbcForm);

        Color accentBlue = new Color(0, 120, 215);

        gbcForm.insets = new Insets(10, 0, 10, 0);
        registerButton = new JButton("Registrati");
        registerButton.setPreferredSize(new Dimension(280, 45));
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(accentBlue);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(true);
        gbcForm.gridy = 4;
        formPanel.add(registerButton, gbcForm);

        gbcForm.insets = new Insets(0, 0, 0, 0);
        backToLoginButton = new JButton("Indietro al Login");
        backToLoginButton.setPreferredSize(new Dimension(280, 45));
        backToLoginButton.setFont(new Font("Arial", Font.BOLD, 16));
        backToLoginButton.setForeground(Color.WHITE);
        backToLoginButton.setBackground(accentBlue);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setOpaque(true);
        gbcForm.gridy = 5;
        formPanel.add(backToLoginButton, gbcForm);

        add(formPanel, gbcOuter);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                    username.equals("Username") || password.equals("Password") || confirmPassword.equals("Confirm Password")) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Tutti i campi sono obbligatori.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Le password non corrispondono.");
                return;
            }

            if (utenteManager.register(username, password)) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Registrazione avvenuta con successo!");
                JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo! Puoi ora effettuare il login.");
                mainFrame.showLogin();
                clearFields();
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Username già esistente.");
            }
        });

        backToLoginButton.addActionListener(e -> {
            mainFrame.showLogin();
        });
    }

    public void clearFields() {
        usernameField.setText("Username");
        usernameField.setForeground(Color.GRAY);
        passwordField.setText("Password");
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.GRAY);
        confirmPasswordField.setText("Confirm Password");
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.setForeground(Color.GRAY);
        messageLabel.setText("");
    }
}