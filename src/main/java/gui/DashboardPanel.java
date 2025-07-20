// src/gui/DashboardPanel.java
/**
 * In questa classe viene implementato il pannello principale del dashboard
 * dell'applicazione, che mostra le bacheche dell'utente loggato.
 * * Il pannello include funzionalità per creare nuove bacheche, aggiungere ToDo,
 * eliminare bacheche e ToDo, e condividere ToDo con altri utenti.
 *
 */
package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Date;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DashboardPanel extends JPanel {
    private Controller boardController;
    private JPanel boards;
    private List<BoardPanel> boardPanels = new ArrayList<>();
    private MainFrame mainFrame;

    // Definizioni Colori Moderni
    private static final Color BACKGROUND_COLOR = new Color(248, 248, 248); // #F8F9FA
    private static final Color PANEL_BACKGROUND = new Color(255, 255, 255); // Bianco puro per bacheche
    // Colore unico per i bottoni, tranne elimina
    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 123, 255); // Blu primario #007BFF
    private static final Color HOVER_PRIMARY_BUTTON_COLOR = new Color(0, 99, 204); // Blu più scuro per hover
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69); // Rosso per elimina #DC3545
    private static final Color HOVER_DELETE_BUTTON_COLOR = new Color(179, 43, 56); // Rosso più scuro per hover
    private static final Color TEXT_DARK_GRAY = new Color(52, 58, 64); // Grigio scuro per testi #343A40
    private static final Color BORDER_LIGHT_GRAY = new Color(222, 226, 230); // Bordo leggero #DEE2E6

    /**
     * Costruttore della DashboardPanel.
     * Inizializza il pannello e carica le bacheche dell'utente loggato.
     *
     * @param mainFrame Riferimento al frame principale dell'applicazione.
     */
    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.boards = new JPanel();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);


        JPanel mainTopPanel = new JPanel();
        mainTopPanel.setLayout(new BoxLayout(mainTopPanel, BoxLayout.Y_AXIS));
        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        mainTopPanel.setBackground(BACKGROUND_COLOR);
        JPanel headerRowPanel = new JPanel();
        headerRowPanel.setLayout(new BorderLayout());
        headerRowPanel.setBackground(BACKGROUND_COLOR);
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeaderPanel.setBackground(BACKGROUND_COLOR);
        JLabel userLabel = new JLabel("Benvenuto, " + mainFrame.getUtenteLoggato().getUsername());
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setForeground(TEXT_DARK_GRAY);
        leftHeaderPanel.add(userLabel);
        JLabel header = new JLabel("Le tue bacheche");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(TEXT_DARK_GRAY);
        leftHeaderPanel.add(header);
        headerRowPanel.add(leftHeaderPanel, BorderLayout.WEST);
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtonsPanel.setBackground(BACKGROUND_COLOR);
        Dimension smallActionButtonSize = new Dimension(120, 35);
        Font smallActionButtonFont = new Font("Arial", Font.BOLD, 12);
        JButton newBoardButton = createStyledButton("➕ Nuova Bacheca", PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR, smallActionButtonSize, smallActionButtonFont);
        JButton newToDoButton = createStyledButton("➕ Nuovo ToDo", PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR, smallActionButtonSize, smallActionButtonFont);
        rightButtonsPanel.add(newBoardButton);
        rightButtonsPanel.add(newToDoButton);
        headerRowPanel.add(rightButtonsPanel, BorderLayout.EAST);
        mainTopPanel.add(headerRowPanel);
        mainTopPanel.add(Box.createVerticalStrut(15));
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        actionPanel.setBackground(BACKGROUND_COLOR);
        Dimension actionButtonSize = new Dimension(110, 35);
        Font actionButtonFont = new Font("Arial", Font.BOLD, 11);
        JButton saveButton = createStyledButton("Salva Bacheca", PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR, actionButtonSize, actionButtonFont);
        JButton cancelButton = createStyledButton("Cancella", PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR, actionButtonSize, actionButtonFont);
        JButton completeButton = createStyledButton("Completa", PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR, actionButtonSize, actionButtonFont);
        JButton deleteButton = createStyledButton("Elimina", DELETE_BUTTON_COLOR, HOVER_DELETE_BUTTON_COLOR, actionButtonSize, actionButtonFont);
        JButton shareToDoButton = createStyledButton("Condividi ToDo", PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR, actionButtonSize, actionButtonFont);
        this.boardController = mainFrame.getController();
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Salvataggio effettuato correttamente nel Database.");
        });
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Vuoi cancellare tutte le bacheche e i relativi ToDo?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Utente utenteLoggato = mainFrame.getUtenteLoggato();
                if (utenteLoggato != null) {
                    boardController.getBoardController().deleteAllBoardsByUserId(utenteLoggato.getId());
                    JOptionPane.showMessageDialog(this, "Tutte le bacheche sono state eliminate.");
                    loadUserBoards();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: Utente non loggato.");
                }
            }
        });
        completeButton.addActionListener(e -> {
            if (boardPanels.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna bacheca disponibile.");
                return;
            }
            String[] nomi = boardPanels.stream().map(BoardPanel::getBoardName).toArray(String[]::new);
            String selezionata = (String) JOptionPane.showInputDialog(this, "Scegli la bacheca da completare:", "Completa Bacheca",
                    JOptionPane.PLAIN_MESSAGE, null, nomi, nomi[0]);
            if (selezionata != null) {
                Utente utenteLoggato = mainFrame.getUtenteLoggato();
                Board boardToComplete = boardController.getBoardController().getBoardByTitoloAndUtente(selezionata, utenteLoggato.getId());
                if (boardToComplete != null) {
                    boardController.markAllToDosAsCompletedByBoardId(boardToComplete.getId());
                    JOptionPane.showMessageDialog(this, "Tutti i ToDo della bacheca '" + selezionata + "' sono stati contrassegnati come completati.");
                    loadUserBoards();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: Bacheca non trovata.");
                }
            }
        });
        deleteButton.addActionListener(e -> {
            if (boardPanels.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna bacheca da eliminare.");
                return;
            }
            String[] nomi = boardPanels.stream().map(BoardPanel::getBoardName).toArray(String[]::new);
            String selezionata = (String) JOptionPane.showInputDialog(this, "Scegli la bacheca da eliminare:", "Elimina Bacheca",
                    JOptionPane.PLAIN_MESSAGE, null, nomi, nomi[0]);
            if (selezionata != null) {
                Utente utenteLoggato = mainFrame.getUtenteLoggato();
                Board boardToDelete = boardController.getBoardController().getBoardByTitoloAndUtente(selezionata, utenteLoggato.getId());
                if (boardToDelete != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Sei sicuro di voler eliminare la bacheca '" + selezionata + "' e tutti i suoi ToDo?",
                            "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boardController.getBoardController().deleteBoard(boardToDelete.getId());
                        JOptionPane.showMessageDialog(this, "Bacheca eliminata con successo.");
                        loadUserBoards();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: Board non trovata per l'eliminazione.");
                }
            }
        });
        newBoardButton.addActionListener(e -> {
            TitoloBacheca[] opzioniTitolo = TitoloBacheca.values();
            String[] nomiTitolo = new String[opzioniTitolo.length];
            for (int i = 0; i < opzioniTitolo.length; i++) {
                nomiTitolo[i] = opzioniTitolo[i].name();
            }
            JComboBox<String> titoloComboBox = new JComboBox<>(nomiTitolo);
            titoloComboBox.setSelectedIndex(0);
            int result = JOptionPane.showConfirmDialog(this, titoloComboBox, "Scegli il nome della nuova Bacheca:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String nuovaBachecaTitolo = (String) titoloComboBox.getSelectedItem();
                if (nuovaBachecaTitolo != null && !nuovaBachecaTitolo.trim().isEmpty()) {
                    Utente utenteLoggato = mainFrame.getUtenteLoggato();
                    if (utenteLoggato == null) {
                        JOptionPane.showMessageDialog(this, "Errore: Utente non loggato.");
                        return;
                    }
                    Board existingBoard = boardController.getBoardController().getBoardByTitoloAndUtente(nuovaBachecaTitolo.trim(), utenteLoggato.getId());
                    if (existingBoard != null) {
                        JOptionPane.showMessageDialog(this, "Una bacheca con questo nome esiste già per il tuo utente.");
                    } else {
                        Board newBoard = new Board(0, nuovaBachecaTitolo.trim(), utenteLoggato.getId());
                        newBoard.setUsername(mainFrame.getUtenteLoggato().getUsername());
                        boardController.addBoard(newBoard);
                        JOptionPane.showMessageDialog(this, "Bacheca '" + nuovaBachecaTitolo + "' aggiunta con successo.");
                        loadUserBoards();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Il nome della bacheca non può essere vuoto.");
                }
            }
        });
        newToDoButton.addActionListener(e -> {
            List<Board> userBoards = boardController.getBoardController().getBoardsByUsername(mainFrame.getUtenteLoggato().getUsername());
            if (userBoards.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Crea prima una bacheca per aggiungere ToDo.");
                return;
            }
            String[] boardNames = userBoards.stream().map(Board::getTitolo).toArray(String[]::new);
            String selectedBoardName = (String) JOptionPane.showInputDialog(this, "Scegli la bacheca per il nuovo ToDo:", "Crea nuovo ToDo",
                    JOptionPane.PLAIN_MESSAGE, null, boardNames, boardNames[0]);
            if (selectedBoardName != null) {
                Board targetBoard = userBoards.stream()
                        .filter(b -> b.getTitolo().equals(selectedBoardName))
                        .findFirst().orElse(null);
                if (targetBoard == null) {
                    JOptionPane.showMessageDialog(this, "Errore: Bacheca selezionata non trovata.");
                    return;
                }
                JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                JTextField titoloField = new JTextField();
                JTextField descrizioneField = new JTextField();
                JTextField scadenzaField = new JTextField();
                JTextField urlField = new JTextField();
                JTextField posizioneField = new JTextField();
                Border inputBorder = BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_LIGHT_GRAY, 1),
                        new EmptyBorder(5, 8, 5, 8)
                );
                Font inputFont = new Font("Arial", Font.PLAIN, 14);
                titoloField.setBorder(inputBorder); titoloField.setFont(inputFont);
                descrizioneField.setBorder(inputBorder); descrizioneField.setFont(inputFont);
                scadenzaField.setBorder(inputBorder); scadenzaField.setFont(inputFont);
                urlField.setBorder(inputBorder); urlField.setFont(inputFont);
                posizioneField.setBorder(inputBorder); posizioneField.setFont(inputFont);
                String[] coloriPredefiniti = {"Bianco", "Rosso", "Verde", "Blu", "Giallo", "Ciano", "Magenta", "Grigio"};
                JComboBox<String> coloreComboBox = new JComboBox<>(coloriPredefiniti);
                coloreComboBox.setSelectedItem("Bianco");
                coloreComboBox.setFont(inputFont);
                coloreComboBox.setBorder(inputBorder);
                JTextField imgPathField = new JTextField();
                JButton selectImageButton = new JButton("Scegli File");
                selectImageButton.setFont(inputFont);
                selectImageButton.setBackground(PRIMARY_BUTTON_COLOR);
                selectImageButton.setForeground(Color.WHITE);
                selectImageButton.setFocusPainted(false);
                selectImageButton.setBorderPainted(false);
                selectImageButton.setOpaque(true);
                selectImageButton.setBorder(new CompoundBorder(new EmptyBorder(3, 8, 3, 8), new LineBorder(PRIMARY_BUTTON_COLOR.darker(), 1, true)));
                addHoverEffect(selectImageButton, PRIMARY_BUTTON_COLOR, HOVER_PRIMARY_BUTTON_COLOR);
                JLabel imagePreviewLabel = new JLabel();
                imagePreviewLabel.setPreferredSize(new Dimension(50, 50));
                imagePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePreviewLabel.setVerticalAlignment(SwingConstants.CENTER);
                imagePreviewLabel.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT_GRAY, 1));
                selectImageButton.addActionListener(fileChooserEvent -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Seleziona immagine ToDo");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini", "jpg", "jpeg", "png", "gif"));
                    int userSelection = fileChooser.showOpenDialog(DashboardPanel.this);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        imgPathField.setText(selectedFile.getAbsolutePath());
                        try {
                            ImageIcon originalIcon = new ImageIcon(selectedFile.toURI().toURL());
                            Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                        } catch (Exception ex) {
                            imagePreviewLabel.setIcon(null);
                            System.err.println("Errore caricamento anteprima: " + ex.getMessage());
                        }
                    }
                });
                panel.add(new JLabel("Titolo*")); panel.add(titoloField);
                panel.add(new JLabel("Descrizione*")); panel.add(descrizioneField);
                panel.add(new JLabel("Scadenza (YYYY-MM-DD)*")); panel.add(scadenzaField);
                panel.add(new JLabel("Colore sfondo*")); panel.add(coloreComboBox);
                panel.add(new JLabel("URL")); panel.add(urlField);
                JPanel imageInputPanel = new JPanel(new BorderLayout());
                imageInputPanel.add(imgPathField, BorderLayout.CENTER);
                imageInputPanel.add(selectImageButton, BorderLayout.EAST);
                panel.add(new JLabel("Immagine (URL o File)")); panel.add(imageInputPanel);
                panel.add(new JLabel("Anteprima Immagine")); panel.add(imagePreviewLabel);
                panel.add(new JLabel("Posizione")); panel.add(posizioneField);
                int creationResult;
                do {
                    creationResult = JOptionPane.showConfirmDialog(this, panel, "Crea nuovo ToDo in " + selectedBoardName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (creationResult == JOptionPane.CANCEL_OPTION || creationResult == JOptionPane.CLOSED_OPTION) return;
                    String titolo = titoloField.getText().trim();
                    String descrizione = descrizioneField.getText().trim();
                    String scadenzaStr = scadenzaField.getText().trim();
                    if (titolo.isEmpty() || descrizione.isEmpty() || scadenzaStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Titolo, Descrizione e Scadenza sono obbligatori.");
                        continue;
                    }
                    LocalDate scadenza;
                    try {
                        scadenza = LocalDate.parse(scadenzaStr);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Data scadenza non valida. Formato:YYYY-MM-DD.");
                        continue;
                    }
                    int posizione = -1;
                    String posizioneStr = posizioneField.getText().trim();
                    if (!posizioneStr.isEmpty()) {
                        try {
                            posizione = Integer.parseInt(posizioneStr);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Posizione non valida.");
                            continue;
                        }
                    }
                    String coloreSelezionatoNome = (String) coloreComboBox.getSelectedItem();
                    String coloreEsadecimale;
                    switch (coloreSelezionatoNome) {
                        case "Bianco": coloreEsadecimale = "#FFFFFF"; break; case "Rosso": coloreEsadecimale = "#FF0000"; break;
                        case "Verde": coloreEsadecimale = "#00FF00"; break; case "Blu": coloreEsadecimale = "#0000FF"; break;
                        case "Giallo": coloreEsadecimale = "#FFFF00"; break; case "Ciano": coloreEsadecimale = "#00FFFF"; break;
                        case "Magenta": coloreEsadecimale = "#FF00FF"; break; case "Grigio": coloreEsadecimale = "#808080"; break;
                        default: coloreEsadecimale = "#FFFFFF";
                    }
                    Date dataScadenzaUtil = Date.from(scadenza.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    ToDo nuovoToDo = new ToDo(titolo, descrizione, mainFrame.getController().getUtenteLoggato(), dataScadenzaUtil, coloreEsadecimale);
                    nuovoToDo.setStato(StatoToDo.NON_COMPLETATO);
                    if (!urlField.getText().trim().isEmpty()) nuovoToDo.setUrl(urlField.getText().trim());
                    if (!imgPathField.getText().trim().isEmpty()) nuovoToDo.setImg(imgPathField.getText().trim());
                    nuovoToDo.setPosizione(posizione);
                    nuovoToDo.setIdUtente(mainFrame.getUtenteLoggato().getId());
                    nuovoToDo.setIdBoard(targetBoard.getId());
                    boardController.createToDo(nuovoToDo);
                    JOptionPane.showMessageDialog(this, "ToDo '" + titolo + "' aggiunto con successo alla bacheca '" + selectedBoardName + "'.");
                    loadUserBoards();
                    break;
                } while (true);
            }
        });
        shareToDoButton.addActionListener(e -> {
            List<ToDo> allUserToDos = new ArrayList<>();
            List<Board> userBoards = boardController.getBoardController().getBoardsByUsername(mainFrame.getUtenteLoggato().getUsername());
            if (userBoards.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Non hai ToDo da condividere. Crea prima una bacheca e aggiungi ToDo.");
                return;
            }
            for (Board b : userBoards) {
                allUserToDos.addAll(boardController.getToDo(b));
            }
            if (allUserToDos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Non hai ToDo da condividere.");
                return;
            }
            String[] todoTitles = allUserToDos.stream().map(ToDo::getTitolo).toArray(String[]::new);
            String selectedToDoTitle = (String) JOptionPane.showInputDialog(this, "Scegli il ToDo da condividere:", "Condividi ToDo",
                    JOptionPane.PLAIN_MESSAGE, null, todoTitles, todoTitles[0]);
            if (selectedToDoTitle != null) {
                ToDo selectedToDo = allUserToDos.stream()
                        .filter(t -> t.getTitolo().equals(selectedToDoTitle))
                        .findFirst().orElse(null);
                if (selectedToDo == null) {
                    JOptionPane.showMessageDialog(this, "Errore: ToDo selezionato non trovato.");
                    return;
                }
                String recipientUsername = JOptionPane.showInputDialog(this, "Inserisci l'username del destinatario:");
                if (recipientUsername != null && !recipientUsername.trim().isEmpty()) {
                    if (recipientUsername.equals(mainFrame.getUtenteLoggato().getUsername())) {
                        JOptionPane.showMessageDialog(this, "Non puoi condividere un ToDo con te stesso.");
                        return;
                    }
                    Board originalBoard = boardController.getBoardController().getBoardById(selectedToDo.getIdBoard());
                    if (originalBoard != null) {
                        boardController.getBoardController().condividiToDo(selectedToDo, originalBoard.getTitolo(), recipientUsername);
                        JOptionPane.showMessageDialog(this, "ToDo '" + selectedToDo.getTitolo() + "' condiviso con " + recipientUsername + ".");
                        loadUserBoards();
                    } else {
                        JOptionPane.showMessageDialog(this, "Errore: Bacheca originale del ToDo non trovata.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Username destinatario non valido.");
                }
            }
        });
        actionPanel.add(shareToDoButton);
        actionPanel.add(completeButton);
        actionPanel.add(cancelButton);
        actionPanel.add(saveButton);
        actionPanel.add(deleteButton);
        mainTopPanel.add(actionPanel);
        add(mainTopPanel, BorderLayout.NORTH);

        JScrollPane boardsScrollPane = new JScrollPane(boards);
        boardsScrollPane.setPreferredSize(new Dimension(mainFrame.getWidth() - 40, 400));
        boardsScrollPane.setMinimumSize(new Dimension(mainFrame.getWidth() - 40, 200));
        boardsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        boardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        boardsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(boardsScrollPane, BorderLayout.CENTER);

        loadUserBoards();
    }

    /**
     * Crea un pulsante con uno stile personalizzato.
     * * @param text Il testo del pulsante.
     * * @param bgColor Il colore di sfondo del pulsante.
     * * @param hoverColor Il colore di sfondo quando il mouse è sopra il pulsante.
     * * @param size La dimensione preferita del pulsante.
     * * @param font Il font del testo del pulsante.
     * * @return Un JButton con lo stile personalizzato.
     */
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor, Dimension size, Font font) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 5, 10), new LineBorder(bgColor.darker(), 1, true)));
        addHoverEffect(button, bgColor, hoverColor);
        return button;
    }

    /**
     * Aggiunge un effetto hover a un pulsante.
     * @param button Il pulsante a cui aggiungere l'effetto hover.
     * @param normalColor Il colore di sfondo normale del pulsante.
     * @param hoverColor Il colore di sfondo quando il mouse è sopra il pulsante.
     */
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            /**
             * Gestisce l'evento di ingresso del mouse sul pulsante.
             * @param e L'evento del mouse.
             */
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            /**
             * Gestisce l'evento di uscita del mouse dal pulsante.
             * @param e L'evento del mouse.
             */
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    /** * Carica le bacheche dell'utente loggato e le visualizza nel pannello.
     * * Questo metodo recupera le bacheche associate all'utente loggato,
     * * le ordina per titolo e crea un pannello per ciascuna bacheca.
     * * Le bacheche vengono visualizzate in un layout a flusso con uno spazio
     * * uniforme tra di esse.
     */
    private void loadUserBoards() {
        boards.removeAll();
        boardPanels.clear();

        String username = mainFrame.getUtenteLoggato().getUsername();
        List<Board> userBoards = boardController.getBoardController().getBoardsByUsername(username);

        if (userBoards != null) {
            userBoards.sort(Comparator.comparing(Board::getTitolo));
            boards.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

            for (Board board : userBoards) {
                List<ToDo> todos = boardController.getToDo(board);
                BoardPanel boardPanel = new BoardPanel(
                        board.getTitolo(),
                        boardController,
                        username,
                        todos
                );
                boardPanel.setPreferredSize(new Dimension(300, 320));
                boardPanel.setMinimumSize(new Dimension(300, 320));
                boardPanel.setMaximumSize(new Dimension(300, 320));
                boardPanel.setBackground(PANEL_BACKGROUND);


                boardPanel.getAccessibleContext().setAccessibleName(board.getTitolo());
                boards.add(boardPanel);
                boardPanels.add(boardPanel);
            }
        }
        boards.revalidate();
        boards.repaint();
    }
}