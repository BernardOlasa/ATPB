package org.example.Classes.Rotina;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.Classes.Aluguel;
import org.example.Classes.Cliente;
import org.example.Classes.Dados.GerenciadorDeDados;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class EnviarEmail extends Thread {
    String remetente = "bernardo.pignatari@al.infnet.edu.br";
    String senhaApp = "qowo shhf iopr qspz";
    String destinatario;
    String assunto = "Atraso na Devolução";
    String corpo;


    @Override
    public void run() {

        while (true) {

            for (Aluguel aluguel : GerenciadorDeDados.alugueis) {
                LocalDate date = aluguel.getDataAluguel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                date = date.plusDays(aluguel.getDiasAlugados());
                if (date.isAfter(LocalDate.now())) {
                    Cliente cliente = aluguel.getCliente();
                    cliente.enviarEmail(remetente, senhaApp);
                }
                try {
                    Thread.sleep((long) 864e+7);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
