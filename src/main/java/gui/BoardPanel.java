// src/gui/BoardPanel.java
package gui;

import model.*;
import controller.Controller;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Panel che rappresenta una Board con i suoi ToDo.
 * Contiene metodi per visualizzare, modificare e gestire i ToDo all'interno della Board.
 * Questa classe estende JPanel e utilizza un layout BorderLayout per organizzare i componenti.
 * * La BoardPanel visualizza i ToDo in un elenco verticale e fornisce funzionalità per modificare
 * i dettagli dei ToDo, come titolo, colore di sfondo, descrizione, URL e immagine.
 * * Inoltre, consente di completare o eliminare i ToDo e visualizza i dettagli in un dialogo.
 * * La classe gestisce anche gli eventi di interazione dell'utente, come il passaggio del mouse sui bottoni
 * e le azioni di clic sui bottoni per modificare i ToDo.
 */
public class BoardPanel extends JPanel {
    private String boardName;
    private transient Controller controller; //transient perchè non va serializzato perchè contiene riferimenti a oggetti complessi
    private JPanel todoListPanel;
    private String username;
    private transient List<ToDo> todos;
    private transient Board board;
    private static final Logger logger = Logger.getLogger(BoardPanel.class.getName());
    // Da aggiungere all'inizio della classe BoardPanel
    private static final String COLORE_BIANCO = "Bianco";
    private static final String COLORE_ROSSO = "Rosso";
    private static final String COLORE_VERDE = "Verde";
    private static final String COLORE_BLU = "Blu";
    private static final String COLORE_GIALLO = "Giallo";
    private static final String COLORE_CIANO = "Ciano";
    private static final String COLORE_MAGENTA = "Magenta";
    private static final String COLORE_GRIGIO = "Grigio";
    private static final String HEX_BIANCO = "#FFFFFF";
    // Definizioni Colori per coerenza
    private static final Color PRIMARY_TEXT = new Color(50, 50, 50);
    private static final Color BUTTON_GENERAL_BG = new Color(255, 255, 255);
    private static final Color BUTTON_GENERAL_HOVER = new Color(240, 240, 240);
    private static final Color LIGHT_BORDER_GRAY = new Color(222, 226, 230);

    // Colori per indicatori di scadenza
    private static final Color SEMAFORO_VERDE = new Color(40, 167, 69);
    private static final Color SEMAFORO_GIALLO = new Color(255, 193, 7);
    private static final Color SEMAFORO_ROSSO = new Color(220, 53, 69);

    /**
     * Costruttore della classe BoardPanel.
     * Inizializza il pannello con il nome della board, il controller, l'username e la lista dei ToDo.
     *
     * @param boardName Il nome della board.
     * @param controller Il controller dell'applicazione.
     * @param username Il nome utente dell'utente corrente.
     * @param todos La lista dei ToDo da visualizzare nella board.
     */
    public BoardPanel(String boardName, Controller controller, String username, List<ToDo> todos) {
        this.boardName = boardName;
        this.controller = controller;
        this.username = username;
        this.board = controller.getBoardByNameAndUser(boardName, controller.getUtenteLoggato().getId());
        this.todos = todos;
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createTitledBorder(this.boardName));

        todoListPanel = new JPanel();
        todoListPanel.setLayout(new BoxLayout(todoListPanel, BoxLayout.Y_AXIS));
        todoListPanel.setBackground(new Color(255, 255, 255));
        JScrollPane scrollPane = new JScrollPane(todoListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    /**
     * Aggiorna il nome della board e il bordo del pannello.
     *
     * @param nuovoNome Il nuovo nome della board.
     */
    public void aggiornaNome(String nuovoNome) {
        this.boardName = nuovoNome;
        setBorder(BorderFactory.createTitledBorder(nuovoNome));
        repaint();
    }
/**
     * Questa funzione rstituisce il nome della board.
 * E' necessario poichè, essendo il nome della board un campo privato,
 * non è accessibile direttamente con il semplice costruttore,
 * quindi serve un metodo getter per poterlo leggere.
     * @return Il nome della board.
     */
    public String getBoardName() {
        return boardName;
    }

    /**
     * Imposta il controller per questa BoardPanel.
     * Questo metodo aggiunge l'effetto hover ai bottoni del pannello.
      * @param button Il bottone su cui applicare l'effetto hover.
      * @param normalColor Il colore normale del bottone.
      * @param hoverColor Il colore del bottone quando il mouse è sopra.
     */
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            /**
             * Gestisce l'evento di ingresso del mouse sul bottone.
             * * @param e L'evento del mouse.
             * * Quando il mouse entra sul bottone, cambia il colore di sfondo del bottone al colore di hover.
             */
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }


            @Override
            /**
             * Gestisce l'evento di uscita del mouse dal bottone.
             * * @param e L'evento del mouse.
             * * Quando il mouse esce dal bottone, cambia il colore di sfondo del bottone al colore normale.
             */
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    /**
     * Carica un'icona di anteprima da un file locale.
     * Ridimensiona l'immagine a 100x100 pixel per l'anteprima.
     *
     * @param file Il file dell'immagine da caricare.
     * @return L'icona dell'immagine ridimensionata.
     * @throws MalformedURLException Se l'URL dell'immagine non è valido.
     */
    private ImageIcon loadImagePreview(File file) throws MalformedURLException {
        ImageIcon newIcon = new ImageIcon(file.toURI().toURL());
        Image scaledImage = newIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Ottiene un'icona di anteprima da un percorso di immagine.
     * Se l'immagine non è valida o non può essere caricata, restituisce null.
     *
     * @param imgPath Il percorso dell'immagine.
     * @return L'icona dell'immagine ridimensionata o null se non valida.
     */
    private ImageIcon getPreviewIcon(String imgPath) {
        try {
            ImageIcon originalIcon = loadOriginalIcon(imgPath);
            if (originalIcon != null && originalIcon.getIconWidth() != -1) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception ignored) { /* non fare nulla se fallisce */ }
        return null;
    }

    /**
     * Carica l'icona originale da un percorso di immagine.
     * Gestisce sia i file locali che gli URL.
     *
     * @param imgPath Il percorso dell'immagine.
     * @return L'icona dell'immagine originale o null se non valida.
     */
    private ImageIcon loadOriginalIcon(String imgPath) {
        try {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                return new ImageIcon(imgFile.toURI().toURL());
            } else {
                return new ImageIcon(new URI(imgPath).toURL());
            }
        } catch (URISyntaxException | MalformedURLException ex) {
            logger.warning("URL immagine non valido nel modifica: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Ricarica i ToDo dalla board e aggiorna il pannello.
     */
    private void refresh() {
        loadTodos();
        refreshTodoListPanel();
    }

    /**
     * Carica i ToDo dalla board utilizzando il controller.
     */
    private void loadTodos() {
        this.todos = controller.getToDo(board);
        logger.info("REFRESH " + boardName + " - ToDo trovati: " + (todos != null ? todos.size() : "null"));
    }

    /**
     * Rinfresca il pannello della lista dei ToDo.
     * Rimuove tutti i componenti esistenti e li ricrea in base alla lista dei ToDo caricati.
     */
    private void refreshTodoListPanel() {
        todoListPanel.removeAll();

        if (todos != null) {
            for (final ToDo todo : this.todos) {
                JPanel todoItemPanel = createTodoItemPanel(todo);
                todoListPanel.add(todoItemPanel);
                todoListPanel.add(Box.createVerticalStrut(5));
            }
        }

        todoListPanel.revalidate();
        todoListPanel.repaint();
    }

    /**
     * Crea un pannello per visualizzare un singolo ToDo.
     * Il pannello include il titolo, i bottoni per modificare, completare, visualizzare i dettagli ed eliminare il ToDo.
     *
     * @param todo Il ToDo da visualizzare.
     * @return Il pannello contenente le informazioni del ToDo.
     */
    private JPanel createTodoItemPanel(ToDo todo) {
        final JPanel todoItemPanel = new JPanel();
        todoItemPanel.setLayout(new BoxLayout(todoItemPanel, BoxLayout.Y_AXIS));
        todoItemPanel.setBorder(createTodoItemBorder());
        todoItemPanel.setPreferredSize(new Dimension(280, 140));
        todoItemPanel.setMaximumSize(new Dimension(280, 140));

        setTodoItemBackground(todoItemPanel, todo);

        JPanel topTextPanel = createTopTextPanel(todo);
        JPanel buttonsGridPanel = createButtonsGridPanel(todo);

        todoItemPanel.add(topTextPanel);
        todoItemPanel.add(buttonsGridPanel);
        todoItemPanel.add(Box.createVerticalGlue());

        return todoItemPanel;
    }

    /**
     * Crea il bordo per un singolo ToDo.
     * @return Il bordo creato per il ToDo.
     */
    private Border createTodoItemBorder() {
        return BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new LineBorder(LIGHT_BORDER_GRAY, 1, true)
        );
    }

    /**
     * Imposta il colore di sfondo del pannello del ToDo in base al colore specificato nel ToDo.
     * Se il colore non è valido, viene utilizzato un colore di default (bianco).
     *
     * @param panel Il pannello da colorare.
     * @param todo Il ToDo che contiene il colore da utilizzare.
     */
    private void setTodoItemBackground(JPanel panel, ToDo todo) {
        String colore = todo.getColore();
        Color defaultColor = Color.WHITE;
        if (colore != null && !colore.isEmpty()) {
            try {
                // Aggiunge il # se manca (es: "FF0000" -> "#FF0000")
                if (!colore.startsWith("#")) {
                    colore = "#" + colore;
                }
                panel.setBackground(Color.decode(colore));
            } catch (Exception e) {
                // Log dell'errore (opzionale)
                logger.warning("Colore non valido: " + colore + ". Uso colore di default.");
                panel.setBackground(defaultColor);
            }
        } else {
            panel.setBackground(defaultColor);
        }
    }

    /**
     * Crea un pannello superiore che contiene il titolo del ToDo e altre informazioni.
     * Include anche un'etichetta per indicare se il ToDo è condiviso da un altro utente.
     *
     * @param todo Il ToDo da visualizzare.
     * @return Il pannello contenente le informazioni del ToDo.
     */
    private JPanel createTopTextPanel(ToDo todo) {
        final JPanel topTextPanel = new JPanel();
        topTextPanel.setLayout(new BoxLayout(topTextPanel, BoxLayout.Y_AXIS));
        topTextPanel.setOpaque(false);
        topTextPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel titleAndImagePanel = createTitleAndImagePanel(todo);
        topTextPanel.add(titleAndImagePanel);

        if (todo.getSharedByUsername() != null && !todo.getSharedByUsername().isEmpty() &&
                !todo.getSharedByUsername().equals(username)) {
            addSharedByLabel(topTextPanel, todo);
        }

        topTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return topTextPanel;
    }

    /**
     * Crea un pannello che contiene il titolo del ToDo e un'icona se disponibile.
     * Il titolo viene colorato in base alla scadenza del ToDo.
     *
     * @param todo Il ToDo da visualizzare.
     * @return Il pannello contenente il titolo e l'icona del ToDo.
     */
    private JPanel createTitleAndImagePanel(ToDo todo) {
        final JPanel titleAndImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titleAndImagePanel.setOpaque(false);

        JLabel todoLabel = createTodoLabel(todo);
        titleAndImagePanel.add(todoLabel);

        return titleAndImagePanel;
    }

    /**
     * Crea un'etichetta per il titolo del ToDo.
     * Il titolo viene colorato in base alla scadenza del ToDo.
     * * Se il ToDo ha una scadenza, viene aggiunta un'informazione sulla scadenza al titolo.
     * * @return L'etichetta contenente il titolo del ToDo.
     * * @param todo Il ToDo da visualizzare.
     */
    private JLabel createTodoLabel(ToDo todo) {
        String titleText = todo.getTitolo();
        Color todoTitleColor = PRIMARY_TEXT;

        if (todo.getScadenza() != null) {
            titleText = addDueDateInfo(todo, titleText);
            todoTitleColor = getDueDateColor(todo);
        }

        final JLabel todoLabel = new JLabel(titleText);
        todoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        todoLabel.setForeground(todoTitleColor);
        return todoLabel;
    }

    /**
     * Aggiunge informazioni sulla scadenza al titolo del ToDo.
     *  Calcola i giorni rimanenti fino alla scadenza e aggiunge un semaforo colorato
     *  in base alla prossimità della scadenza:
     *  - Rosso se la scadenza è passata
     *  - Arancione se la scadenza è entro 7 giorni
     *  - Verde se la scadenza è oltre 7 giorni
     *  @param todo Il ToDo da visualizzare.
     *  @param titleText Il testo del titolo da modificare.
     * @return Il titolo modificato con le informazioni sulla scadenza.
     */
    private String addDueDateInfo(ToDo todo, String titleText) {
        LocalDate scadenzaDate = ((java.sql.Date) todo.getScadenza()).toLocalDate();
        LocalDate today = LocalDate.now();
        long daysUntilDue = java.time.temporal.ChronoUnit.DAYS.between(today, scadenzaDate);

        String semaforo = "";
        SimpleDateFormat dateFormatterYear = new SimpleDateFormat("yyyy");

        if (todo.getStato() != StatoToDo.COMPLETATO) {
            if (daysUntilDue < 0) {
                semaforo = " 🔴";
            } else if (daysUntilDue <= 7) {
                semaforo = " 🟠";
            } else {
                semaforo = " 🟢";
            }
        }

        return titleText + " (Scadenza: " + dateFormatterYear.format(todo.getScadenza()) + semaforo + ")";
    }

    /**
     * Determina il colore da utilizzare per il titolo del ToDo in base alla scadenza.
     * * Se il ToDo è completato, restituisce il colore del testo primario.
     * * Se la scadenza è passata, restituisce il colore rosso.
     * * Se la scadenza è entro 7 giorni, restituisce il colore giallo.
     * * Altrimenti, restituisce il colore verde.
     * * @param todo Il ToDo da visualizzare.
     * @return Il colore da utilizzare per il titolo del ToDo.
     */
    private Color getDueDateColor(ToDo todo) {
        if (todo.getStato() == StatoToDo.COMPLETATO) {
            return PRIMARY_TEXT;
        }

        LocalDate scadenzaDate = ((java.sql.Date) todo.getScadenza()).toLocalDate();
        LocalDate today = LocalDate.now();
        long daysUntilDue = java.time.temporal.ChronoUnit.DAYS.between(today, scadenzaDate);

        if (daysUntilDue < 0) {
            return SEMAFORO_ROSSO;
        } else if (daysUntilDue <= 7) {
            return SEMAFORO_GIALLO;
        }
        return SEMAFORO_VERDE;
    }

    /**
     * Aggiunge un'etichetta per indicare che il ToDo è condiviso da un altro utente.
     * L'etichetta mostra il nome utente di chi ha condiviso il ToDo.
     *
     * @param panel Il pannello in cui aggiungere l'etichetta.
     * @param todo Il ToDo da cui ottenere il nome utente del condivisore.
     */
    private void addSharedByLabel(JPanel panel, ToDo todo) {
        final JLabel sharedByLabel = new JLabel("Condiviso da: " + todo.getSharedByUsername());
        sharedByLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        sharedByLabel.setForeground(PRIMARY_TEXT);
        sharedByLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sharedByLabel);
    }

    /**
     * Crea un pannello con i bottoni per modificare, completare, visualizzare i dettagli ed eliminare il ToDo.
     * I bottoni sono disposti in una griglia 2x2.
     *
     * @param todo Il ToDo per cui creare i bottoni.
     * @return Il pannello contenente i bottoni.
     */
    private JPanel createButtonsGridPanel(ToDo todo) {
        final JPanel buttonsGridPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonsGridPanel.setOpaque(false);
        buttonsGridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton completeButton = createCompleteButton(todo);
        JButton editButton = createEditButton(todo);
        JButton detailsButton = createDetailsButton(todo);
        JButton deleteButton = createDeleteButton(todo);

        buttonsGridPanel.add(completeButton);
        buttonsGridPanel.add(editButton);
        buttonsGridPanel.add(detailsButton);
        buttonsGridPanel.add(deleteButton);

        buttonsGridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return buttonsGridPanel;
    }

    /**
     * Crea un bottone per visualizzare i dettagli del ToDo.
     * * Quando cliccato, mostra un dialogo con le informazioni dettagliate del ToDo,
     * * inclusi titolo, colore di sfondo, descrizione, URL e immagine.
     * * @param todo Il ToDo per cui creare il bottone.
     * * @return Il bottone creato per visualizzare i dettagli del ToDo.
     */
    private JButton createCompleteButton(ToDo todo) {
        final JButton completeButton = new JButton(todo.getStato() == StatoToDo.COMPLETATO ? "Non Completare" : "Completa");
        completeButton.setEnabled(true);
        styleButton(completeButton);
        completeButton.addActionListener(e -> toggleTodoStatus(todo));
        return completeButton;
    }

    /**
     * Crea un bottone per visualizzare i dettagli del ToDo.
     * Quando cliccato, mostra un dialogo con le informazioni dettagliate del ToDo,
     * inclusi titolo, colore di sfondo, descrizione, URL e immagine.
     *
     * @param todo Il ToDo per cui creare il bottone.
     * @return Il bottone creato per visualizzare i dettagli del ToDo.
     */
    private void toggleTodoStatus(ToDo todo) {
        if (todo.getStato() == StatoToDo.COMPLETATO) {
            todo.setStato(StatoToDo.NON_COMPLETATO);
        } else {
            todo.setStato(StatoToDo.COMPLETATO);
        }
        controller.updateToDo(todo);
        refresh();
    }

    /**
     * Crea un bottone per modificare il ToDo.
     * Quando cliccato, mostra un dialogo con opzioni per modificare il titolo,
     * il colore di sfondo, la descrizione, l'URL e l'immagine del ToDo.
     * @param todo Il ToDo da modificare.
     * @return Il bottone creato per modificare il ToDo.
     */
    private JButton createEditButton(ToDo todo) {
        final JButton editButton = new JButton("Modifica");
        styleButton(editButton);
        editButton.addActionListener(e -> showEditDialog(todo));
        return editButton;
    }

    /**
     * Crea un bottone per modificare il dialogo dei dettagli del ToDo.
     * Quando cliccato, mostra un dialogo con le informazioni dettagliate del ToDo,
     * inclusi titolo, colore di sfondo, descrizione, URL e immagine.
     * @param todo Il ToDo per cui creare il bottone.
     *  @return Il bottone creato per visualizzare i dettagli del ToDo.
     */
    private void showEditDialog(ToDo todo) {
        final JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] options = {"Modifica titolo", "Modifica colore sfondo", "Modifica descrizione",
                "Modifica URL", "Modifica URL Immagine", "Modifica Posizione"};

        for (String option : options) {
            JButton btn = createOptionButton(option);
            btn.addActionListener(e -> handleEditOption(option, todo));
            panel.add(btn);
        }

        showOptionDialog(panel, "Modifica ToDo");
    }

    /**
     * Crea un bottone per le opzioni di modifica del ToDo.
     * @param text Il testo del bottone.
     * @return Il bottone creato per le opzioni di modifica del ToDo.
     */
    private JButton createOptionButton(String text) {
        final JButton btn = new JButton(text);
        btn.setForeground(PRIMARY_TEXT);
        btn.setBackground(BUTTON_GENERAL_BG);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER_GRAY, 1, true));
        btn.setFocusPainted(false);
        addHoverEffect(btn, BUTTON_GENERAL_BG, BUTTON_GENERAL_HOVER);
        return btn;
    }

    /** Gestisce l'evento di clic su un'opzione di modifica del ToDo.
      * A seconda dell'opzione selezionata, chiama il metodo appropriato per modificare il ToDo.
      * @param option L'opzione selezionata dall'utente.
      * @param todo Il ToDo da modificare.
      * Mostra un messaggio di errore se l'opzione non è riconosciuta.
     */
    private void handleEditOption(String option, ToDo todo) {
        switch(option) {
            case "Modifica titolo":
                editTodoTitle(todo);
                break;
            case "Modifica colore sfondo":
                editTodoColor(todo);
                break;
            case "Modifica descrizione":
                editTodoDescription(todo);
                break;
            case "Modifica URL":
                editTodoUrl(todo);
                break;
            case "Modifica URL Immagine":
                editTodoImageUrl(todo);
                break;
            case "Modifica Posizione":
                editTodoPosition(todo);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Opzione di modifica non riconosciuta: " + option);
                break;
        }
    }

    /**
     * Mostra  un dialogo per modificare il titolo del ToDo.
      * Chiede all'utente di inserire un nuovo titolo e lo aggiorna nel ToDo.
      * Se il titolo è vuoto, mostra un messaggio di errore.
      * @param todo Il ToDo da modificare.
     * Mostra un messaggio di errore se il titolo è vuoto o non valido.
     */
    private void editTodoTitle(ToDo todo) {
        String nuovoTitolo;
        do {
            nuovoTitolo = JOptionPane.showInputDialog(this, "Nuovo titolo (obbligatorio):", todo.getTitolo());
            if (nuovoTitolo == null || nuovoTitolo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Il titolo è obbligatorio.");
            }
        } while (nuovoTitolo == null || nuovoTitolo.trim().isEmpty());
        todo.setTitolo(nuovoTitolo.trim());
        controller.updateToDo(todo);
        refresh();
    }

    /**
     * Mostra un dialogo per modificare il colore di sfondo del ToDo.
      * Presenta un JComboBox con colori predefiniti.
     * Se l'utente seleziona un colore, lo aggiorna nel ToDo e lo salva.
        * Se l'utente annulla, non fa nulla.
     * Permette di spostare il ToDo in un'altra board.
     * @param todo IL nome del ToDo da modificare.
     */
    private void editTodoColor(ToDo todo) {
        String[] coloriPredefiniti = {COLORE_BIANCO, COLORE_ROSSO, COLORE_VERDE, COLORE_BLU,
                COLORE_GIALLO, COLORE_CIANO, COLORE_MAGENTA, COLORE_GRIGIO};
        final JComboBox<String> nuovoColoreComboBox = new JComboBox<>(coloriPredefiniti);

        String coloreAttualeHex = todo.getColore();
        String coloreAttualeNome = getCurrentColorName(coloreAttualeHex);
        nuovoColoreComboBox.setSelectedItem(coloreAttualeNome);

        int optionResult = JOptionPane.showConfirmDialog(this, nuovoColoreComboBox,
                "Nuovo colore sfondo:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (optionResult == JOptionPane.OK_OPTION) {
            String coloreSelezionatoNome = (String) nuovoColoreComboBox.getSelectedItem();
            String coloreEsadecimale = getColorHex(coloreSelezionatoNome);
            todo.setColore(coloreEsadecimale);
            controller.updateToDo(todo);
            refresh();
        }
    }

    /**
     * Restituisce il nome del colore corrente in base al codice esadecimale.
     * * Se il colore è null, restituisce "Bianco" come colore di default, altrimenti restituisce il colore attuale.
     * * @param coloreAttualeHex Il codice esadecimale del colore
     * @return Il nome del colore corrente.
     */
    private String getCurrentColorName(String coloreAttualeHex) {
        if (coloreAttualeHex == null) return COLORE_BIANCO;

        switch (coloreAttualeHex.toUpperCase()) {
            case HEX_BIANCO: return COLORE_BIANCO;
            case "#FF0000": return COLORE_ROSSO;
            case "#00FF00": return COLORE_VERDE;
            case "#0000FF": return COLORE_BLU;
            case "#FFFF00": return COLORE_GIALLO;
            case "#00FFFF": return COLORE_CIANO;
            case "#FF00FF": return COLORE_MAGENTA;
            case "#808080": return COLORE_GRIGIO;
            default: return COLORE_BIANCO;
        }
    }

    /**
     * Restituisce il codice esadecimale del colore
     * in base al nome del colore selezionato.
     * Se il colore non è riconosciuto, restituisce il colore bianco come default.
     * @param colorName Il nome del colore selezionato.
     * @return Il codice esadecimale del colore
     */
    private String getColorHex(String colorName) {
        switch (colorName) {
            case COLORE_BIANCO: return HEX_BIANCO;
            case COLORE_ROSSO: return "#FF0000";
            case COLORE_VERDE: return "#00FF00";
            case COLORE_BLU: return "#0000FF";
            case COLORE_GIALLO: return "#FFFF00";
            case COLORE_CIANO: return "#00FFFF";
            case COLORE_MAGENTA: return "#FF00FF";
            case COLORE_GRIGIO: return "#808080";
            default: return HEX_BIANCO;
        }
    }

    /**
     * Questo medoto permette di modificare la descrizione del ToDo.
      * Mostra un dialogo di input per l'utente per inserire una nuova descrizione.
      * Se l'utente inserisce una descrizione vuota o annulla, mostra un messaggio di errore.
      * Aggiorna la descrizione del ToDo e lo salva tramite il controller.
     * @param todo Il ToDo da cui modificare la descrizione.


     */
    private void editTodoDescription(ToDo todo) {
        String newDescription;
        do {
            newDescription = JOptionPane.showInputDialog(this, "Nuova descrizione (obbligatoria):", todo.getDescrizione());
            if (newDescription == null || newDescription.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descrizione è obbligatoria.");
            }
        } while (newDescription == null || newDescription.trim().isEmpty());
        todo.setDescrizione(newDescription.trim());
        controller.updateToDo(todo);
        refresh();
    }

    /**
     * Mostra un dialogo di input per modificare l'URL del ToDo.
     * * Se l'utente inserisce un URL vuoto, lo rimuove dal ToDo.
     * * Se l'utente annulla, non fa nulla.
     * * Aggiorna l'URL del ToDo e lo salva tramite il controller.
     * @param todo Il ToDo da cui modificare l'url.
     */
    private void editTodoUrl(ToDo todo) {
        String newUrl = JOptionPane.showInputDialog(this, "Nuovo URL (lascia vuoto per rimuovere):", todo.getUrl());
        if (newUrl != null) {
            todo.setUrl(newUrl.trim());
            controller.updateToDo(todo);
            refresh();
        }
    }
/**
     * Mostra un dialogo per modificare l'URL dell'immagine del ToDo.
     * Permette di selezionare un file locale o inserire un URL.
     * Se l'utente seleziona un file, mostra un'anteprima dell'immagine.
     * Aggiorna l'URL dell'immagine del ToDo e lo salva tramite il controller.
     *
     * @param todo Il ToDo da cui modificare l'URL dell'immagine.
     */
    private void editTodoImageUrl(ToDo todo) {
        final JPanel imageEditPanel = new JPanel(new GridLayout(0, 1));
        final JTextField currentImgField = new JTextField(todo.getImg());
        final JButton selectFileButton = new JButton("Scegli File Locale...");
        final JLabel previewLabel = new JLabel();
        previewLabel.setPreferredSize(new Dimension(100, 100));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setVerticalAlignment(SwingConstants.CENTER);
        previewLabel.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER_GRAY, 1));

        if (todo.getImg() != null && !todo.getImg().isEmpty()) {
            ImageIcon previewIcon = getPreviewIcon(todo.getImg());
            if (previewIcon != null) {
                previewLabel.setIcon(previewIcon);
            }
        }

        selectFileButton.addActionListener(fileChooserEvent -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleziona Immagine");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini", "jpg", "jpeg", "png", "gif"));
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();
                currentImgField.setText(selectedFile.getAbsolutePath());
                try {
                    ImageIcon previewIcon = loadImagePreview(selectedFile);
                    previewLabel.setIcon(previewIcon);
                } catch (MalformedURLException ex) {
                    logger.warning("Errore creazione URL da file: " + ex.getMessage());
                    previewLabel.setIcon(null);
                } catch (Exception ex) {
                    previewLabel.setIcon(null);
                    logger.warning("Errore caricamento anteprima: " + ex.getMessage());
                }
            }
        });

        imageEditPanel.add(new JLabel("Nuovo URL Immagine o Percorso File:"));
        imageEditPanel.add(currentImgField);
        imageEditPanel.add(selectFileButton);
        imageEditPanel.add(new JLabel("Anteprima:"));
        imageEditPanel.add(previewLabel);

        int optionResult = JOptionPane.showConfirmDialog(this, imageEditPanel,
                "Modifica URL Immagine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (optionResult == JOptionPane.OK_OPTION) {
            String newImageUrl = currentImgField.getText().trim();
            todo.setImg(newImageUrl.isEmpty() ? null : newImageUrl);
            controller.updateToDo(todo);
            refresh();
        }
    }
/**
     * Mostra un dialogo per modificare la posizione del ToDo.
     * Permette di inserire un numero intero per la nuova posizione.
     * Se l'utente inserisce -1, rimuove la posizione dal ToDo.
     * Aggiorna la posizione del ToDo e lo salva tramite il controller.
     *
     * @param todo Il ToDo da cui modificare la posizione.
     */
    private void editTodoPosition(ToDo todo) {
        String newPosStr = JOptionPane.showInputDialog(this, "Nuova Posizione (numero intero, -1 per rimuovere):",
                String.valueOf(todo.getPosizione()));
        if (newPosStr != null && !newPosStr.trim().isEmpty()) {
            try {
                int newPos = Integer.parseInt(newPosStr.trim());
                todo.setPosizione(newPos);
                controller.updateToDo(todo);
                refresh();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Posizione non valida.");
            }
        } else if (newPosStr != null && newPosStr.trim().isEmpty()) {
            todo.setPosizione(-1);
            controller.updateToDo(todo);
            refresh();
        }
    }
/**
     * Crea un bottone per visualizzare i dettagli del ToDo.
     * @return Il bottone creato per visualizzare i dettagli del ToDo.
     */
    private JButton createDetailsButton(ToDo todo) {
        final JButton detailsButton = new JButton("Dettagli");
        styleButton(detailsButton);
        detailsButton.addActionListener(e -> showTodoDetails(todo));
        return detailsButton;
    }

    /**
     * Metodo per visualizzare i dettagli di un ToDo.
     * Crea un pannello con le informazioni dettagliate del ToDo,
     * inclusi titolo, colore di sfondo, descrizione, URL e immagine.
     *  Mostra un dialogo con queste informazioni
     * @param todo Il todo per il quale viene richiesto di visualizzare i dettagli
     *
     */
    private void showTodoDetails(ToDo todo) {
        final JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 2);

        addDetailRow(detailsPanel, gbc, "Titolo:", todo.getTitolo());
        addDetailRow(detailsPanel, gbc, "Descrizione:", todo.getDescrizione());
        addDateDetailRow(detailsPanel, gbc, todo);
        addColorDetailRow(detailsPanel, gbc, todo);
        addPositionDetailRow(detailsPanel, gbc, todo);
        addUrlDetailRow(detailsPanel, gbc, todo);
        addImageDetailRow(detailsPanel, gbc, todo);

        JOptionPane.showMessageDialog(this, detailsPanel, "Dettagli ToDo", JOptionPane.PLAIN_MESSAGE);
    }

    /**

     * Aggiunge una riga  ad un pannello, composta da un'etichetta e un valore.
     * Se il valore è nullo o vuoto, mostra "N.D.".
     * @param panel Il pannello a cui aggiungere la riga.
     * @param gbc Le constraint di layout GridBagConstraints da aggiornare.
     * @param label Il testo dell'etichetta da mostrare.
     * @param value Il valore da mostrare accanto all'etichetta.
     */

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(new JLabel(value != null && !value.isEmpty() ? value : "N.D."), gbc);

        gbc.gridy++;
    }

    /**
     * Aggiunge una riga al pannello con la data di scadenza del ToDo.
     * Se la scadenza è nulla, mostra "N.D.".
     *
     * @param panel Il pannello a cui aggiungere la riga.
     * @param gbc Le constraint di layout GridBagConstraints da aggiornare.
     * @param todo Il ToDo di cui mostrare la scadenza.
     */
    private void addDateDetailRow(JPanel panel, GridBagConstraints gbc, ToDo todo) {
        SimpleDateFormat dateFormatterFull = new SimpleDateFormat("yyyy-MM-dd");

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Scadenza:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(new JLabel(todo.getScadenza() != null ? dateFormatterFull.format(todo.getScadenza()) : "N.D."), gbc);

        gbc.gridy++;
    }
    /**
     * Aggiunge una riga al pannello con il colore di sfondo del ToDo.
     * Mostra un quadrato colorato che rappresenta il colore selezionato.
     * Se il colore non è valido, viene mostrato bianco.
     *
     * @param panel Il pannello a cui aggiungere la riga.
     * @param gbc Le constraint di layout GridBagConstraints da aggiornare.
     * @param todo Il ToDo di cui mostrare il colore di sfondo.
     */
    private void addColorDetailRow(JPanel panel, GridBagConstraints gbc, ToDo todo) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Colore Sfondo:"), gbc);

        final JPanel colorSquare = new JPanel();
        colorSquare.setPreferredSize(new Dimension(20, 20));
        colorSquare.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        try {
            colorSquare.setBackground(Color.decode(todo.getColore()));
        } catch (Exception ex) {
            colorSquare.setBackground(Color.WHITE);
        }

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        final JPanel colorAndTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        colorAndTextPanel.add(colorSquare);
        panel.add(colorAndTextPanel, gbc);

        gbc.gridy++;
    }

    /**
     * Aggiunge una riga al pannello con la posizione del ToDo.
     * @param panel Il pannello a cui aggiungere la riga.
     * @param gbc Le constraint di layout GridBagConstraints da aggiornare.
     * @param todo Il ToDo di cui mostrare la posizione.
     */
    private void addPositionDetailRow(JPanel panel, GridBagConstraints gbc, ToDo todo) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Posizione:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(new JLabel(todo.getPosizione() != -1 ? String.valueOf(todo.getPosizione()) : "N.D."), gbc);

        gbc.gridy++;
    }

    /**
     * Aggiunge una riga al pannello con l'URL del ToDo.
     * @param panel Il pannello a cui aggiungere la riga.
     * @param gbc Le constraint di layout GridBagConstraints da aggiornare.
     * @param todo Il ToDo di cui mostrare l'URL.
     */
    private void addUrlDetailRow(JPanel panel, GridBagConstraints gbc, ToDo todo) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("URL:"), gbc);

        if (todo.getUrl() != null && !todo.getUrl().isEmpty()) {
            final JLabel urlLabel = createClickableUrlLabel(todo.getUrl());
            gbc.gridx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(urlLabel, gbc);
        } else {
            gbc.gridx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(new JLabel("N.D."), gbc);
        }

        gbc.gridy++;
    }
    /**
     * Crea un'etichetta cliccabile per l'URL del ToDo.
     * Quando l'etichetta viene cliccata, apre l'URL nel browser predefinito.
     *
     * @param url L'URL da visualizzare nell'etichetta.
     * @return L'etichetta cliccabile con l'URL.
     */

    private JLabel createClickableUrlLabel(String url) {
        final JLabel urlLabel = new JLabel("<html><a href=\"" + url + "\">" + url + "</a></html>");
        urlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        urlLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(BoardPanel.this,
                                "Impossibile aprire l'URL: " + ex.getMessage(),
                                "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        return urlLabel;
    }
    /**
     * Aggiunge una riga al pannello con l'immagine del ToDo.
     * Se l'immagine è disponibile, mostra un'icona cliccabile che apre l'immagine in un dialogo.
     * Se l'immagine non è disponibile, mostra "N.D.".
     *
     * @param panel Il pannello a cui aggiungere la riga.
     * @param gbc Le constraint di layout GridBagConstraints da aggiornare.
     * @param todo Il ToDo di cui mostrare l'immagine.
     */
    private void addImageDetailRow(JPanel panel, GridBagConstraints gbc, ToDo todo) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Immagine:"), gbc);

        final JLabel clickableImageDisplay = createImageDisplay(todo);

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(clickableImageDisplay, gbc);
    }

    /**
     * Crea un'etichetta per visualizzare l'immagine del ToDo.
     * Se l'immagine è disponibile, la mostra come icona cliccabile.
     * Se l'immagine non è disponibile, mostra "N.D.".
     *
     * @param todo Il ToDo di cui mostrare l'immagine.
     * @return L'etichetta con l'immagine o il testo "N.D.".
     */
    private JLabel createImageDisplay(ToDo todo) {
        final JLabel clickableImageDisplay = new JLabel();
        clickableImageDisplay.setPreferredSize(new Dimension(200, 150));
        clickableImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        clickableImageDisplay.setVerticalAlignment(SwingConstants.CENTER);
        clickableImageDisplay.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        clickableImageDisplay.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (todo.getImg() != null && !todo.getImg().isEmpty()) {
            URL imageURL = getImageUrl(todo.getImg());
            ImageIcon imageIcon = imageURL != null ? new ImageIcon(imageURL) : null;

            if (imageIcon != null && imageIcon.getIconWidth() != -1) {
                Image scaledImage = scaleImageToFit(imageIcon.getImage(),
                        clickableImageDisplay.getPreferredSize());
                clickableImageDisplay.setIcon(new ImageIcon(scaledImage));
            } else {
                clickableImageDisplay.setText("Immagine non caricabile");
            }

            final URL finalImageURL = imageURL;
            clickableImageDisplay.addMouseListener(createImageClickListener(finalImageURL, todo.getTitolo()));
        } else {
            clickableImageDisplay.setText("N.D.");
        }

        return clickableImageDisplay;
    }

    /**
     * Restituisce un URL per l'immagine del ToDo.
     * * Se il percorso dell'immagine è un file locale, lo converte in URL.
     * * Se il percorso è un URL valido, lo restituisce direttamente.
     * * Se il percorso non è valido, restituisce null e registra un avviso.
     * * @throws MalformedURLException Se il percorso dell'immagine non è un URL valido.
     * * @throws URISyntaxException Se il percorso dell'immagine non è un URI valido.
     * * Questo metodo gestisce sia percorsi di file locali che URL remoti.
     * * @return L'URL dell'immagine, o null se il percorso non è valido.
     * @param imagePath Il percorso dell'immagine, che può essere un file locale o un URL.
     */
    private URL getImageUrl(String imagePath) {
        try {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                return imgFile.toURI().toURL();
            }
            return new URI(imagePath).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            logger.warning("URL immagine non valido: " + imagePath + " - " + ex.getMessage());
            return null;
        }
    }

    /**
     * Scala un'immagine per adattarla a una dimensione specificata.
     * * Calcola il fattore di scala in base alla larghezza e all'altezza originali dell'immagine
     * * e alla dimensione target.
     * * @param image L'immagine da scalare.
     * * @param targetSize La dimensione target a cui adattare l'immagine.
     * @return L'immagine scalata per adattarsi alla dimensione target.
     */
    private Image scaleImageToFit(Image image, Dimension targetSize) {
        int originalWidth = image.getWidth(null);
        int originalHeight = image.getHeight(null);
        double scaleX = (double) targetSize.width / originalWidth;
        double scaleY = (double) targetSize.height / originalHeight;
        double scale = Math.min(scaleX, scaleY);
        return image.getScaledInstance(
                (int)(originalWidth * scale),
                (int)(originalHeight * scale),
                Image.SCALE_SMOOTH);
    }

    /**
     * Crea un listener per il clic sull'immagine del ToDo.
     * Quando l'immagine viene cliccata, apre un dialogo con l'immagine a dimensione intera.
     * Se l'immagine non è disponibile, mostra un messaggio di informazione.
     *
     * @param imageURL L'URL dell'immagine da visualizzare.
     * @param title Il titolo del ToDo associato all'immagine.
     * @return Il MouseAdapter che gestisce il clic sull'immagine.
     */
    private MouseAdapter createImageClickListener(URL imageURL, String title) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (imageURL != null) {
                    showImageInDialog(imageURL, title);
                } else {
                    JOptionPane.showMessageDialog(BoardPanel.this,
                            "Nessuna immagine disponibile per la visualizzazione.",
                            "Informazione", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
    }

    /**
     * Mostra l'immagine del ToDo in un dialogo.
     * Se l'immagine è disponibile, la visualizza a dimensione intera in un JFrame.
     * Se l'immagine non è disponibile, apre l'URL nel browser predefinito.
     *
     * @param imageURL L'URL dell'immagine da visualizzare.
     * @param title Il titolo del ToDo associato all'immagine.
     */
    private void showImageInDialog(URL imageURL, String title) {
        try {
            ImageIcon fullSizeIcon = new ImageIcon(imageURL);
            if (fullSizeIcon.getIconWidth() != -1) {
                JLabel fullSizeLabel = new JLabel(fullSizeIcon);
                JFrame imageFrame = new JFrame("Immagine ToDo: " + title);
                imageFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                JScrollPane scrollPane = new JScrollPane(fullSizeLabel);
                imageFrame.add(scrollPane);
                imageFrame.pack();
                imageFrame.setLocationRelativeTo(null);
                imageFrame.setVisible(true);
            } else {
                openImageInBrowser(imageURL);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(BoardPanel.this,
                    "Errore visualizzazione immagine: " + ex.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Apre l'immagine del ToDo in un browser esterno.
     * Se il browser non è disponibile o l'URL non è valido, mostra un messaggio di errore.
     *
     * @param imageURL L'URL dell'immagine da aprire nel browser.
     */
    private void openImageInBrowser(URL imageURL) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(imageURL.toURI());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(BoardPanel.this,
                        "Immagine non caricabile o visualizzatore esterno non disponibile.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Crea un bottone per eliminare il ToDo.
     * Quando cliccato, chiede conferma all'utente e, se confermato, elimina il ToDo.
     * @param todo Il ToDo da eliminare.
     * @return Il bottone creato per eliminare il ToDo.
     */
    private JButton createDeleteButton(ToDo todo) {
        final JButton deleteButton = new JButton("Elimina");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> {
            controller.getBoardController().deleteToDo(todo);
            refresh();
        });
        return deleteButton;
    }

    /**
     * Applica lo stile al bottone.
     * Imposta il colore del testo, il colore di sfondo, rende il bottone opaco,
      * rimuove la vernice di focus e imposta un bordo personalizzato.
      * Aggiunge un effetto hover per cambiare il colore di sfondo quando il mouse passa sopra il bottone.
     * @param button Il bottone a cui applicare lo stile.
     */
    private void styleButton(JButton button) {
        button.setForeground(PRIMARY_TEXT);
        button.setBackground(BUTTON_GENERAL_BG);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(createButtonBorder());
        addHoverEffect(button, BUTTON_GENERAL_BG, BUTTON_GENERAL_HOVER);
    }
/*
        * Crea un bordo personalizzato per i bottoni.
        * Utilizza un bordo vuoto con margini e un bordo di linea con colore grigio chiaro.
        * Questo bordo viene utilizzato per i bottoni all'interno del pannello dei bottoni.
        * @return Il bordo creato per i bottoni.
 */
    private Border createButtonBorder() {
        return BorderFactory.createCompoundBorder(
                new EmptyBorder(2, 5, 2, 5),
                new LineBorder(LIGHT_BORDER_GRAY, 1, true)
        );
    }

    /**
     * Mostra un dialogo con le opzioni di modifica del ToDo.
     * @param panel Il pannello contenente i bottoni delle opzioni di modifica.
     * @param title Il titolo del dialogo.

     */
    private void showOptionDialog(JPanel panel, String title) {
        final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        final JDialog dialog = optionPane.createDialog(this, title);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

}