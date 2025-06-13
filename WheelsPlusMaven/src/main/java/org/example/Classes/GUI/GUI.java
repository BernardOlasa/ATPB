package org.example.Classes.GUI;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final JFrame janelaPrincipal;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel painelPrincipal = new JPanel(cardLayout);

    public GUI() {
        janelaPrincipal = new JFrame("Wheels+ Aluguel de Bicicletas");
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janelaPrincipal.setSize(900, 700);
        janelaPrincipal.setResizable(true);
    }

    public void executar(int nivel) {
        // --- PAINÉIS ---
        PainelAluguel painelAluguel = new PainelAluguel();
        painelPrincipal.add(painelAluguel, "ALUGUEL");

        PainelEntrega painelEntrega = new PainelEntrega();
        painelPrincipal.add(painelEntrega, "ENTREGA");

        PainelHistoricoAlugueis painelHistorico = null;
        PainelDashboard painelDashboard = null; // PAINEL NOVO
        PainelCriarBicicleta painelCriarBicicleta = null;
        PainelPromocao painelPromocao = null;
        PainelCRUDCliente painelCRUDCliente = null;
        PainelCRUDBicicleta painelCRUDBicicleta = null;

        if (nivel == 1) {
            painelHistorico = new PainelHistoricoAlugueis();
            painelDashboard = new PainelDashboard(); // Instancia o painel
            painelCriarBicicleta = new PainelCriarBicicleta();
            painelPromocao = new PainelPromocao();
            painelCRUDCliente = new PainelCRUDCliente(janelaPrincipal);
            painelCRUDBicicleta = new PainelCRUDBicicleta(janelaPrincipal);

            painelPrincipal.add(painelHistorico, "HISTORICO");
            painelPrincipal.add(painelDashboard, "DASHBOARD"); // Adiciona ao CardLayout
            painelPrincipal.add(painelCriarBicicleta, "CADASTRO_BIKE");
            painelPrincipal.add(painelPromocao, "PROMOCAO");
            painelPrincipal.add(painelCRUDCliente, "CRUD_CLIENTE");
            painelPrincipal.add(painelCRUDBicicleta, "CRUD_BICICLETA");
        }

        // --- BARRA DE NAVEGAÇÃO ---
        final PainelHistoricoAlugueis finalPainelHistorico = painelHistorico;
        final PainelDashboard finalPainelDashboard = painelDashboard;
        final PainelCRUDCliente finalPainelCRUDCliente = painelCRUDCliente;
        final PainelCRUDBicicleta finalPainelCRUDBicicleta = painelCRUDBicicleta;
        final PainelPromocao finalPainelPromocao = painelPromocao;

        BarraNavegacao barraNavegacao = new BarraNavegacao(
                e -> {
                    painelAluguel.refreshData();
                    cardLayout.show(painelPrincipal, "ALUGUEL");
                },
                e -> {
                    painelEntrega.carregarBicicletasAlugadas();
                    cardLayout.show(painelPrincipal, "ENTREGA");
                },
                nivel == 1 ? e -> {
                    if (finalPainelHistorico != null) finalPainelHistorico.refreshData();
                    cardLayout.show(painelPrincipal, "HISTORICO");
                } : null,
                nivel == 1 ? e -> { // AÇÃO PARA O NOVO MENU DASHBOARD
                    if (finalPainelDashboard != null) finalPainelDashboard.refreshData();
                    cardLayout.show(painelPrincipal, "DASHBOARD");
                } : null,
                nivel == 1 ? e -> cardLayout.show(painelPrincipal, "CADASTRO_BIKE") : null,
                nivel == 1 ? e -> {
                    if (finalPainelPromocao != null) finalPainelPromocao.refreshData();
                    cardLayout.show(painelPrincipal, "PROMOCAO");
                } : null,
                nivel == 1 ? e -> {
                    if (finalPainelCRUDCliente != null) finalPainelCRUDCliente.refreshData();
                    cardLayout.show(painelPrincipal, "CRUD_CLIENTE");
                } : null,
                nivel == 1 ? e -> {
                    if (finalPainelCRUDBicicleta != null) finalPainelCRUDBicicleta.refreshData();
                    cardLayout.show(painelPrincipal, "CRUD_BICICLETA");
                } : null,
                nivel
        );
        janelaPrincipal.setJMenuBar(barraNavegacao.getBarraNavegacao());

        // --- FINALIZAÇÃO ---
        janelaPrincipal.add(painelPrincipal);
        janelaPrincipal.setLocationRelativeTo(null);
        cardLayout.show(painelPrincipal, "ALUGUEL");
        janelaPrincipal.setVisible(true);
    }
}