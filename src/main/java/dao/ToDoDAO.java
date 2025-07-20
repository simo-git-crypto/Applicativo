// src/dao/ToDoDAO.java
package dao;

import model.ToDo;
import java.util.List;
/**
  * Interfaccia per la gestione delle operazioni sui ToDo.
  * Definisce i metodi per aggiungere, recuperare, aggiornare ed eliminare ToDo,
 *  * oltre a metodi specifici per la gestione dei ToDo associati a una bacheca.
  * Questa interfaccia consente di separare le operazioni di creazione, lettura, aggiornamento
 * ed eliminazione (CRUD) dei ToDo dalla loro implementazione concreta,
  * facilitando la manutenibilità e l'estensibilità del codice.
  * Inoltre, include un metodo per marcare tutti i ToDo di una bacheca come completati,
  * utile per funzionalità come "Completa Bacheca".
 */

public interface ToDoDAO { // Interfaccia per la gestione delle operazioni sui ToDo
    /**
     * Aggiunge un nuovo ToDo al sistema.
     * @param todo Il ToDo da aggiungere.
     */
    void addToDo(ToDo todo);// Aggiunge un nuovo ToDo
  /**
     * Recupera un ToDo per ID.
     * @param id L'ID del ToDo da recuperare.
     * @return Il ToDo corrispondente all'ID specificato.
     */
    ToDo getToDoById(int id); // Recupera un ToDo per ID
   /**
     * Recupera tutti i ToDo presenti nel sistema.
     * @return Una lista di tutti i ToDo.
     */
    List<ToDo> getAllToDos(); // Recupera tutti i ToDo
    /**
     * Aggiorna un ToDo esistente.
     * @param todo Il ToDo con i dati aggiornati.
     */
    void updateToDo(ToDo todo); // Aggiorna un ToDo esistente
    /**
     * Elimina un ToDo per ID.
     * @param id L'ID del ToDo da eliminare.
     */
    void deleteToDo(int id);// Elimina un ToDo per ID
    /**
     * Recupera tutti i ToDo associati a un ID di bacheca specifico.
     * @param boardId L'ID della bacheca per cui recuperare i ToDo.
     * @return Una lista di ToDo associati all'ID di bacheca specificato.
     */
    List<ToDo> getToDosByBoardId(int boardId);// Recupera i ToDo per ID bacheca

    /**
     * Segna tutti i ToDo di una bacheca come completati.
     * @param boardId L'ID della bacheca per cui segnare i ToDo come completati.
     */
    void markAllToDosAsCompletedByBoardId(int boardId); // New method for "Completa Bacheca"
}