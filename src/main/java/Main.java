package org.example;

import java.util.Scanner;
import org.example.Bacheca;
import org.example.ToDo;
import org.example.Utente;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Utente utente = null;
        Bacheca bacheca = null;

        while (true) {
            System.out.println("1. Crea utente");
            System.out.println("2. Crea bacheca");
            System.out.println("3. Crea ToDo");
            System.out.println("4. Visualizza bacheca");
            System.out.println("5. Esci");

            System.out.print("Scegli un'opzione: ");
            int opzione = sc.nextInt();
            sc.nextLine(); // Consuma il newline

            switch (opzione) {
                case 1:
                    utente = Utente.creaDaTastiera();
                    System.out.println("Utente creato con successo!");
                    break;
                case 2:
                    if (utente == null) {
                        System.out.println("Devi creare un utente prima di creare una bacheca!");
                        break;
                    }
                    bacheca = Bacheca.creadatastiera();
                    bacheca.setTipo("Privata"); // Imposto il tipo di default
                    System.out.println("Bacheca creata con successo!");
                    break;
                case 3:
                    if (bacheca == null) {
                        System.out.println("Devi creare una bacheca e un utente prima di creare un ToDo!");
                        break;
                    }
                    ToDo toDo = new ToDo("Nuovo ToDo", utente);
                    toDo.inserisciDatastiera();
                    bacheca.creaToDo(toDo.getTitolo(), utente);
                    System.out.println("ToDo creato con successo!");
                    break;
                case 4:
                    if (bacheca == null) {
                        System.out.println("Devi creare una bacheca prima di visualizzarla!");
                        break;
                    }
                    bacheca.visualizzaBacheca();
                    break;
                case 5:
                    System.out.println("Uscita...");
                    return;
                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }
}
