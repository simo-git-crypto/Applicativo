package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardPanel extends JPanel {
    private Controller boardController;
    private JPanel boards;
    private List<BoardPanel> boardPanels = new ArrayList<>();
    private MainFrame mainFrame;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        model.UtenteManager utenteManager = mainFrame.getUtenteManager();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String username = mainFrame.getUtenteLoggato().getUsername();

        JLabel userLabel = new JLabel("Benvenuto, " + username);
        userLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel header = new JLabel("Le tue bacheche");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(userLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(header);

        boards = new JPanel(new GridLayout(1, 3, 10, 10));
        boards.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton newBoardButton = new JButton("➕ Nuova Bacheca");
        newBoardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newBoardButton.addActionListener(e -> {
            List<String> titoliFissi = List.of("Università", "Lavoro", "Tempo Libero");
            List<String> titoliPresenti = boardPanels.stream().map(BoardPanel::getBoardName).toList();

            List<String> titoliMancanti = titoliFissi.stream()
                    .filter(titolo -> !titoliPresenti.contains(titolo))
                    .toList();

            if (titoliMancanti.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Puoi aggiungere una nuova bacheca solo dopo aver eliminato una delle bacheche fisse.");
                return;
            }

            String nuovaBacheca = (String) JOptionPane.showInputDialog(
                    this,
                    "Scegli una bacheca da aggiungere:",
                    "Nuova Bacheca",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    titoliMancanti.toArray(),
                    titoliMancanti.get(0)
            );

            if (nuovaBacheca != null && !nuovaBacheca.trim().isEmpty()) {
                BoardPanel nuova = new BoardPanel(
                        nuovaBacheca.trim(),
                        boardController,
                        mainFrame.getUtenteLoggato().getUsername(),
                        boardController.getToDo(mainFrame.getUtenteLoggato().getUsername(), nuovaBacheca.trim())
                );
                boards.add(nuova);
                boardPanels.add(nuova);
                boards.revalidate();
                boards.repaint();
            }
        });

        JButton saveButton = new JButton("Salva Bacheca");
        JButton cancelButton = new JButton("Cancella");
        JButton completeButton = new JButton("Completa");
        JButton editButton = new JButton("Edita");
        JButton deleteButton = new JButton("Elimina");

        this.boardController = mainFrame.getController();

        saveButton.addActionListener(e -> {
            String currentUsername = mainFrame.getUtenteLoggato().getUsername();
            boolean success = boardController.salvaBacheche(currentUsername);
            if (success) {
                File f = new File(currentUsername + "_bacheche.txt");
                System.out.println("Salvataggio completato in: " + f.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Bacheche salvate correttamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Errore nel salvataggio delle bacheche.");
            }
        });

        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Vuoi cancellare tutte le bacheche?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boards.removeAll();
                boardPanels.clear();
                boards.revalidate();
                boards.repaint();
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
                boardPanels.stream()
                        .filter(bp -> bp.getBoardName().equals(selezionata))
                        .findFirst()
                        .ifPresent(BoardPanel::completaTutti);
            }
        });

        editButton.addActionListener(e -> {
            if (boardPanels.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna bacheca da modificare.");
                return;
            }
            String[] nomi = boardPanels.stream().map(BoardPanel::getBoardName).toArray(String[]::new);
            String selezionata = (String) JOptionPane.showInputDialog(this, "Scegli la bacheca da rinominare:", "Modifica Bacheca",
                    JOptionPane.PLAIN_MESSAGE, null, nomi, nomi[0]);
            if (selezionata != null) {
                String nuovoNome = JOptionPane.showInputDialog(this, "Nuovo nome per '" + selezionata + "':");
                if (nuovoNome != null && !nuovoNome.trim().isEmpty()) {
                    boardPanels.stream()
                            .filter(bp -> bp.getBoardName().equals(selezionata))
                            .findFirst()
                            .ifPresent(bp -> bp.aggiornaNome(nuovoNome.trim()));
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
                BoardPanel daEliminare = null;
                for (BoardPanel bp : boardPanels) {
                    if (bp.getBoardName().equals(selezionata)) {
                        daEliminare = bp;
                        break;
                    }
                }
                if (daEliminare != null) {
                    boardPanels.remove(daEliminare);
                    boards.remove(daEliminare);
                    boards.revalidate();
                    boards.repaint();
                }
            }
        });

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        actionPanel.add(saveButton);
        actionPanel.add(cancelButton);
        actionPanel.add(completeButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);

        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(newBoardButton);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(actionPanel);

        add(topPanel, BorderLayout.NORTH);


        String[] titoliFissi = {"Università", "Lavoro", "Tempo Libero"};
        for (String titolo : titoliFissi) {
            BoardPanel bachecaFissa = new BoardPanel(
                    titolo,
                    boardController,
                    username,
                    boardController.getToDo(username, titolo)
            );
            boards.add(bachecaFissa);
            boardPanels.add(bachecaFissa);
            boardController.addBoard(username, new Board(titolo));
        }

        add(boards, BorderLayout.CENTER);
    }

}