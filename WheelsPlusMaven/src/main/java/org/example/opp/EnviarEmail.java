package org.example.opp;

import org.example.domain.Aluguel;
import org.example.domain.Cliente;
import org.example.data.GerenciadorDeDados;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit; // Import recomendado para clareza

public class EnviarEmail extends Thread {
    String remetente = "bernardo.pignatari@al.infnet.edu.br";
    String senhaApp = "qowo shhf iopr qspz";

    @Override
    public void run() {
        while (true) {
            System.out.println("ROTINA: Verificando aluguéis atrasados..."); // Log para saber que está rodando

            for (Aluguel aluguel : GerenciadorDeDados.alugueis) {
                if (aluguel.isBicicletasDevolvidas()) {
                    continue;
                }

                LocalDate dataInicio = aluguel.getDataAluguel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate dataDevolucao = dataInicio.plusDays(aluguel.getDiasAlugados());

                if (LocalDate.now().isAfter(dataDevolucao)) {
                    Cliente cliente = aluguel.getCliente();
                    System.out.println("ROTINA: Encontrado aluguel atrasado para o cliente: " + cliente.getNome() + ". Enviando email...");
                    cliente.enviarEmail(remetente, senhaApp);
                }
            }

            try {
                System.out.println("ROTINA: Verificação concluída. Próxima verificação em 24 horas.");
                TimeUnit.HOURS.sleep(24);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}