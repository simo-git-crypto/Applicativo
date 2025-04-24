package org.example;
import java.util.Scanner;
import org.example.ToDo;
import org.example.Bacheca;
import org.example.Condivisione;
import org.example.ToDo_BACHECA;

public class Utente {
    private final String login;
    private String password;
    public Utente(String login, String password){
        this.login = login;
    this.password = password;
    }
    public static Utente creaDaTastiera(){
        Scanner sc =new Scanner(System.in);
        System.out.println("Inserire login : ");
        String login=sc.nextLine();
        System.out.println("Inserire password: ");
        String password=sc.nextLine();

        return new Utente(login, password);
    }
    public boolean ModificaPassword(String vecchiapassword, String nuovapassword){
        if (vecchiapassword == null || nuovapassword == null || vecchiapassword.isEmpty() || nuovapassword.isEmpty()) {
            System.out.println(" Errore: Le password non possono essere vuote.");
            return false;
        }
        if(this.password.equals(vecchiapassword)){
            this.password=nuovapassword;
            System.out.println("Password modificata correttamente !");
            return true; }
        System.out.println("La password inserita non è corretta !");
        return false; }
    private boolean attivo=true;
    public void eliminaUtente(){
        if(!attivo){
            System.out.println("Utente non esistente");
            return;
        }
        attivo=false;
        System.out.println("Utente eliminato");
    }
    public boolean isAttivo(){
        return attivo; }
    public String getLogin(){
        return login;
    } public String getPassword(){
        return password;
    }
}
