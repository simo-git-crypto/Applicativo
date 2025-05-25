package gui;

import model.StatoToDo;
import model.ToDo;
import controller.Controller;
import model.Utente;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoardPanel extends JPanel {
    private String boardName;
    private Controller controller;
    private JPanel todoListPanel;
    private String username;
    private List<ToDo> todos;

    private List<ToDo> getTodos(){
        return todos;
    }
    public BoardPanel(String boardName, Controller controller, String username, List<ToDo> todos) {
        this.boardName = boardName;
        this.controller = controller;
        this.username = username;
        this.todos = todos;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(boardName));

        todoListPanel = new JPanel();
        todoListPanel.setLayout(new BoxLayout(todoListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(todoListPanel);
        add(scrollPane, BorderLayout.CENTER);
        //Creazione nuovo ToDo
        JButton addToDoButton = new JButton("➕ Nuovo ToDo");
        addToDoButton.addActionListener(e -> {
            String titoloToDo;
            do {
                titoloToDo = JOptionPane.showInputDialog(this, "Inserisci il titolo del nuovo ToDo (obbligatorio):");
                if (titoloToDo == null || titoloToDo.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Il titolo del ToDo è obbligatorio.");
                }
            } while (titoloToDo == null || titoloToDo.trim().isEmpty());

            String descrizioneToDo;
            do {
                descrizioneToDo = JOptionPane.showInputDialog(this, "Inserisci Descrizione (obbligatoria):");
                if (descrizioneToDo == null || descrizioneToDo.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La descrizione del ToDo è obbligatoria.");
                }
            } while (descrizioneToDo == null || descrizioneToDo.trim().isEmpty());

            String dataScadenzaStr;
            java.time.LocalDate scadenza = null;
            do {
                dataScadenzaStr = JOptionPane.showInputDialog(this, "Inserisci la scadenza (YYYY-MM-DD, obbligatoria):");
                if (dataScadenzaStr == null || dataScadenzaStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La data di scadenza è obbligatoria.");
                } else {
                    try {
                        scadenza = java.time.LocalDate.parse(dataScadenzaStr.trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Data non valida. Formato:YYYY-MM-DD.");
                        scadenza = null;
                    }
                }
            } while (scadenza == null);


            String colore;
            do {
                colore = JOptionPane.showInputDialog(this, "Inserisci colore sfondo (es: #FFAAAA, obbligatorio):");
                if (colore == null || colore.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Il colore dello sfondo è obbligatorio.");
                }
            } while (colore == null || colore.trim().isEmpty());

            String urlToDo = JOptionPane.showInputDialog(this, "Inserisci URL (opzionale):");
            String imageUrlToDo = JOptionPane.showInputDialog(this, "Inserisci URL Immagine (opzionale):");

            int posizioneToDo = -1;
            String posizioneStr = JOptionPane.showInputDialog(this, "Inserisci Posizione (numero intero, opzionale):");
            if (posizioneStr != null && !posizioneStr.trim().isEmpty()) {
                try {
                    posizioneToDo = Integer.parseInt(posizioneStr.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Posizione non valida. Ignorata.");
                }
            }

            java.util.Date dataScadenzaUtil = java.util.Date.from(scadenza.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());

            ToDo nuovoToDo = new ToDo(titoloToDo.trim(), descrizioneToDo.trim(), controller.getUser(username), dataScadenzaUtil, colore.trim());
            nuovoToDo.setStato(StatoToDo.NON_COMPLETATO);

            if (urlToDo != null && !urlToDo.trim().isEmpty()) nuovoToDo.setUrl(urlToDo.trim());
            if (imageUrlToDo != null && !imageUrlToDo.trim().isEmpty()) nuovoToDo.setImg(imageUrlToDo.trim());
            if (posizioneToDo != -1) nuovoToDo.setPosizione(posizioneToDo);


            controller.createToDo(username, boardName, nuovoToDo);
            refresh();
        });
        add(addToDoButton, BorderLayout.SOUTH);

        refresh();
        //Condividi ToDo
        JButton shareToDoButton = new JButton("Condividi ToDo");
        shareToDoButton.addActionListener(e -> {
            String loginUtente = JOptionPane.showInputDialog(this, "Login dell'utente con cui condividere il ToDo:");
            if (loginUtente != null && !loginUtente.trim().isEmpty()) {
                List<ToDo> todosCondivisibili = controller.getToDo(username, boardName);
                if (todosCondivisibili == null || todosCondivisibili.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Non ci sono ToDo da condividere.");
                    return;
                }
                String[] titoli = todosCondivisibili.stream().map(ToDo::getTitolo).toArray(String[]::new);
                String titoloSelezionato = (String) JOptionPane.showInputDialog(this, "Scegli il ToDo da condividere:", "Condivisione ToDo",
                        JOptionPane.PLAIN_MESSAGE, null, titoli, titoli[0]);

                if (titoloSelezionato != null) {
                    ToDo todoSelezionato = todosCondivisibili.stream()
                            .filter(t -> t.getTitolo().equals(titoloSelezionato))
                            .findFirst()
                            .orElse(null);
                    if (todoSelezionato != null) {
                        Utente utenteDestinatario = controller.getUser(loginUtente);
                        if (utenteDestinatario != null) {
                            controller.getBoardController().condividiToDo(todoSelezionato, boardName, utenteDestinatario.getUsername());
                            JOptionPane.showMessageDialog(this, "ToDo condiviso con successo.");
                            refresh();
                        } else {
                            JOptionPane.showMessageDialog(this, "Utente non trovato.");
                        }
                    }
                }
            }
        });
        add(shareToDoButton, BorderLayout.NORTH);
    }

    public void aggiornaNome(String nuovoNome) {
        this.boardName = nuovoNome;
        setBorder(BorderFactory.createTitledBorder(nuovoNome));
        repaint();
    }

    public String getBoardName() {
        return boardName;
    }
    private void refresh() {
        this.todos = controller.getToDo(username, boardName);
        System.out.println("REFRESH " + boardName + " - ToDo trovati: " + (todos != null ? todos.size() : "null"));
        todoListPanel.removeAll();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (ToDo todo : this.todos) {
            JPanel todoItemPanel = new JPanel();
            todoItemPanel.setLayout(new BoxLayout(todoItemPanel, BoxLayout.Y_AXIS));
            todoItemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            todoItemPanel.setPreferredSize(new Dimension(300, 100));
            todoItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            String colore = todo.getColore();
            if (colore != null && !colore.isEmpty()) {
                try {
                    todoItemPanel.setBackground(Color.decode(colore));
                } catch (Exception ignored) {}
            }

            Color buttonFg = Color.BLACK;
            Color buttonBg = Color.WHITE;
            Border buttonBorder = BorderFactory.createLineBorder(Color.GRAY, 1);

            JPanel topTextPanel = new JPanel();
            topTextPanel.setLayout(new BoxLayout(topTextPanel, BoxLayout.Y_AXIS));
            topTextPanel.setOpaque(false);
            topTextPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

            String labelText = todo.getTitolo();
            if (todo.getScadenza() != null) {
                labelText += " (scade: " + dateFormatter.format(todo.getScadenza()) + ")"; // Format date
            }
            if (todo.getStato() == StatoToDo.COMPLETATO) {
                labelText += " ✔";
            }

            JLabel todoLabel = new JLabel(labelText);
            todoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            topTextPanel.add(todoLabel);

            if (todo.getSharedByUsername() != null && !todo.getSharedByUsername().isEmpty() &&
                    !todo.getSharedByUsername().equals(username)) {
                JLabel sharedByLabel = new JLabel("Condiviso da: " + todo.getSharedByUsername());
                sharedByLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                sharedByLabel.setForeground(Color.BLACK);
                sharedByLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                topTextPanel.add(sharedByLabel);
            }
            topTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            todoItemPanel.add(topTextPanel);

            JPanel buttonsGridPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            buttonsGridPanel.setOpaque(false);
            buttonsGridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            //Completa
            JButton completeButton = new JButton("Completa");
            completeButton.setEnabled(todo.getStato() != StatoToDo.COMPLETATO);
            completeButton.setForeground(buttonFg);
            completeButton.setBackground(buttonBg);
            completeButton.setOpaque(true);
            completeButton.setContentAreaFilled(true);
            completeButton.setBorderPainted(true);
            completeButton.setBorder(buttonBorder);
            completeButton.addActionListener(e -> {
                todo.setStato(StatoToDo.COMPLETATO);
                controller.markAsCompleted(todo);
                refresh();
            });
            buttonsGridPanel.add(completeButton);

            // Modifica
            JButton editButton = new JButton("Modifica");
            editButton.setForeground(buttonFg);
            editButton.setBackground(buttonBg);
            editButton.setOpaque(true);
            editButton.setContentAreaFilled(true);
            editButton.setBorderPainted(true);
            editButton.setBorder(buttonBorder);
            editButton.addActionListener(e -> {
                JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                String[] options = {"Modifica titolo", "Modifica colore sfondo", "Modifica descrizione", "Modifica URL", "Modifica URL Immagine", "Modifica Posizione"};
                JButton[] optionButtons = new JButton[options.length];

                Color optionButtonFg = Color.BLACK;
                Color optionButtonBg = Color.WHITE;

                for (int i = 0; i < options.length; i++) {
                    JButton btn = new JButton(options[i]);
                    btn.setForeground(optionButtonFg);
                    btn.setBackground(optionButtonBg);
                    btn.setOpaque(true);
                    btn.setContentAreaFilled(true);
                    btn.setBorderPainted(true);
                    btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    btn.setFocusPainted(false);
                    optionButtons[i] = btn;
                    panel.add(btn);
                }

                JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                JDialog dialog = optionPane.createDialog(this, "Modifica ToDo");
                dialog.setModal(true);
                dialog.pack();
                dialog.setLocationRelativeTo(this);

                for (int i = 0; i < options.length; i++) {
                    final int selectionIndex = i;
                    optionButtons[i].addActionListener(innerE -> {
                        dialog.dispose();

                        if (selectionIndex == 0) { // Modifica titolo
                            String nuovoTitolo;
                            do {
                                nuovoTitolo = JOptionPane.showInputDialog(this, "Nuovo titolo (obbligatorio):", todo.getTitolo());
                                if (nuovoTitolo == null || nuovoTitolo.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Il titolo è obbligatorio.");
                                }
                            } while (nuovoTitolo == null || nuovoTitolo.trim().isEmpty());
                            todo.setTitolo(nuovoTitolo.trim());
                            refresh();
                        } else if (selectionIndex == 1) { // Modifica colore sfondo
                            String nuovoColore;
                            do {
                                nuovoColore = JOptionPane.showInputDialog(this, "Nuovo colore sfondo (es: #FFAAAA, obbligatorio):", todo.getColore());
                                if (nuovoColore == null || nuovoColore.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Il colore è obbligatorio.");
                                }
                            } while (nuovoColore == null || nuovoColore.trim().isEmpty());
                            todo.setColore(nuovoColore.trim());
                            refresh();
                        } else if (selectionIndex == 2) { // Modifica descrizione
                            String newDescription;
                            do {
                                newDescription = JOptionPane.showInputDialog(this, "Nuova descrizione (obbligatoria):", todo.getDescrizione());
                                if (newDescription == null || newDescription.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "La descrizione è obbligatoria.");
                                }
                            } while (newDescription == null || newDescription.trim().isEmpty());
                            todo.setDescrizione(newDescription.trim());
                        } else if (selectionIndex == 3) { // Modifica URL
                            String newUrl = JOptionPane.showInputDialog(this, "Nuovo URL (lascia vuoto per rimuovere):", todo.getUrl());
                            if (newUrl != null) {
                                todo.setUrl(newUrl.trim());
                            }
                        } else if (selectionIndex == 4) { // Modifica URL Immagine
                            String newImageUrl = JOptionPane.showInputDialog(this, "Nuovo URL Immagine (lascia vuoto per rimuovere):", todo.getImg());
                            if (newImageUrl != null) {
                                todo.setImg(newImageUrl.trim());
                            }
                        } else if (selectionIndex == 5) { // Modifica Posizione
                            String newPosStr = JOptionPane.showInputDialog(this, "Nuova Posizione (numero intero, -1 per rimuovere):", String.valueOf(todo.getPosizione()));
                            if (newPosStr != null && !newPosStr.trim().isEmpty()) {
                                try {
                                    int newPos = Integer.parseInt(newPosStr.trim());
                                    todo.setPosizione(newPos);
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(this, "Posizione non valida. Ignorata.");
                                }
                            } else if (newPosStr != null && newPosStr.trim().isEmpty()) {
                                todo.setPosizione(-1);
                            }
                        }
                    });
                }
                dialog.setVisible(true);
            });
            buttonsGridPanel.add(editButton);

            // Visualizza Dettagli, descrizione, posizione ecc...
            JButton detailsButton = new JButton("Dettagli");
            detailsButton.setForeground(buttonFg);
            detailsButton.setBackground(buttonBg);
            detailsButton.setOpaque(true);
            detailsButton.setContentAreaFilled(true);
            detailsButton.setBorderPainted(true);
            detailsButton.setBorder(buttonBorder);
            detailsButton.addActionListener(e -> {
                String desc = (todo.getDescrizione() != null && !todo.getDescrizione().isEmpty() ? todo.getDescrizione() : "Nessuna descrizione.");
                String url = (todo.getUrl() != null && !todo.getUrl().isEmpty() ? "\nURL: " + todo.getUrl() : "");
                String imageUrl = (todo.getImg() != null && !todo.getImg().isEmpty() ? "\nURL Immagine: " + todo.getImg() : "");
                String posizione = (todo.getPosizione() != -1 ? "\nPosizione: " + todo.getPosizione() : "");
                JOptionPane.showMessageDialog(this, "Dettagli ToDo:\n" + desc + url + imageUrl + posizione);
            });
            buttonsGridPanel.add(detailsButton);

            // Elimina
            JButton deleteButton = new JButton("Elimina");
            deleteButton.setForeground(buttonFg);
            deleteButton.setBackground(buttonBg);
            deleteButton.setOpaque(true);
            deleteButton.setContentAreaFilled(true);
            deleteButton.setBorderPainted(true);
            deleteButton.setBorder(buttonBorder);
            deleteButton.addActionListener(e -> {
                controller.getBoardController().deleteToDo(username, boardName, todo);
                refresh();
            });
            buttonsGridPanel.add(deleteButton);

            buttonsGridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            todoItemPanel.add(buttonsGridPanel);

            todoItemPanel.add(Box.createVerticalGlue());


            todoListPanel.add(todoItemPanel);
            todoListPanel.add(Box.createVerticalStrut(5));
        }

        todoListPanel.revalidate();
        todoListPanel.repaint();

        controller.getBoardController().salvaBacheche(username);
    }
    //Completa i todo di una bacheca
    public void completaTutti() {
        List<ToDo> todos = controller.getToDo(username, boardName);
        for (ToDo todo : todos) {
            if (todo.getStato() != StatoToDo.COMPLETATO) {
                todo.setStato(StatoToDo.COMPLETATO);
                controller.markAsCompleted(todo);
            }
        }
        refresh();
    }
}