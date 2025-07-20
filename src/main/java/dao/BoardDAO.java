// src/dao/BoardDAO.java
package dao;

import model.Board;
import java.util.List;
/** * Interfaccia per la gestione delle operazioni di creazione, lettura, aggiornamento ed eliminazione(CRUD) sulle bacheche (Board).
 * Definisce i metodi per aggiungere, recuperare, aggiornare ed eliminare bacheche.
 */
public interface BoardDAO {
    /**
     * Aggiunge una nuova bacheca al sistema.
     * @param board La bacheca da aggiungere.
     */
    void addBoard(Board board); // Aggiunge una nuova bacheca
    /**
     * Recupera una bacheca per ID.
     * @param id L'ID della bacheca da recuperare.
     * @return La bacheca corrispondente all'ID specificato.
     */
    Board getBoardById(int id); // Recupera una bacheca per ID
    /**
     * Recupera tutte le bacheche.
     * @return Una lista di tutte le bacheche presenti nel sistema.
     */
    List<Board> getAllBoards();// Recupera tutte le bacheche
    /**
     * Aggiorna una bacheca esistente.
     * @param board La bacheca con i dati aggiornati.
     */
    void updateBoard(Board board);// Aggiorna una bacheca esistente
    /**
     * Elimina una bacheca per ID.
     * @param id L'ID della bacheca da eliminare.
     */
    void deleteBoard(int id); // Elimina una bacheca per ID
    /**
     * Recupera una bacheca per titolo e ID utente.
     * @param titolo Il titolo della bacheca da recuperare.
     * @param idUtente L'ID dell'utente proprietario della bacheca.
     * @return La bacheca corrispondente al titolo e all'ID utente specificati.
     */
    Board getBoardByTitoloAndUtente(String titolo, int idUtente); // Recupera una bacheca per titolo e ID utente

    /**
     * Recupera tutte le bacheche associate a un ID di bacheca specifico.
     * @param bachecaId L'ID della bacheca per cui recuperare le bacheche.
     * @return Una lista di bacheche associate all'ID specificato.
     */
    List<Board> getBoardsByBachecaId(int bachecaId); // Recupera le bacheche per ID utente

    /**
     * Recupera tutte le bacheche associate a un nome utente specifico.
     * @param username Il nome utente per cui recuperare le bacheche.
     * @return Una lista di bacheche associate al nome utente specificato.
     */
    List<Board> getBoardsByUsername(String username); // Recupera le bacheche per nome utente

    /**
     * Recupera tutte le bacheche associate a un ID utente specifico.
     * @param userId L'ID dell'utente per cui recuperare le bacheche.
     * @return Una lista di bacheche associate all'ID utente specificato.
     */
    void deleteAllBoardsByUserId(int userId); // New method for "Cancella" all boards
}
/*In questo caso le interfacce sono utilizzate per:
 1)definire un insieme di regole che le classi concrete devono implementare.
2)Usare il polimorfismo, permettendo di trattare oggetti di classi diverse in modo uniforme.
3)Favorire la separazione tra cosa fa(definizione) una classe e come lo fa(implementazione),
  rendendo il codice più modulare e manutenibile.
  4)Facilitare  la manutenzione e l'estensibilità del codice,
 poiché le classi concrete possono essere modificate o sostituite senza influenzare il codice
  che le utilizza.
 5)Favorire la testabilità del codice, poiché le interfacce possono essere facilmente
  sostituite con mock o stub(implementazioni fittizie) durante i test unitari.


 */

