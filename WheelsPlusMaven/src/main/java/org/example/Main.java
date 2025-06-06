package org.example;
import org.example.Classes.Dados.GerenciadorDeDados;
import org.example.Classes.GUI.*;
import org.example.Classes.Rotina.EnviarEmail;
import org.example.Classes.Usuario;


public class Main {

    public static void main(String[] args){

        EnviarEmail enviarEmail = new EnviarEmail();
        Thread t1 = new Thread(enviarEmail);
        t1.start();

        TelaLogin lg = new TelaLogin();
        lg.executar();



    }
}
