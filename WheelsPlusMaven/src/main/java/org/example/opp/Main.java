package org.example.opp;
import org.example.ui.TelaLogin;


public class Main {

    public static void main(String[] args){

        EnviarEmail enviarEmail = new EnviarEmail();
        Thread t1 = new Thread(enviarEmail);
        t1.start();

        TelaLogin lg = new TelaLogin();
        lg.executar();



    }
}
