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
        janelaPrincipal.setSize(800, 600);
        janelaPrincipal.setResizable(false);
    }

    public void executar(int nivel) {
        // --- PAINÉIS ---
        PainelAluguel painelAluguel = new PainelAluguel();
        painelPrincipal.add(painelAluguel, "ALUGUEL");

        PainelCriarBicicleta painelCriarBicicleta = null;
        PainelPromocao painelPromocao = null;

        if (nivel == 1) {
            painelCriarBicicleta = new PainelCriarBicicleta();
            painelPromocao = new PainelPromocao();
            painelPrincipal.add(painelCriarBicicleta, "CADASTRO_BIKE");
            painelPrincipal.add(painelPromocao, "PROMOCAO");
        }

        // --- BARRA DE NAVEGAÇÃO ---
        BarraNavegacao barraNavegacao = new BarraNavegacao(
                e -> cardLayout.show(painelPrincipal, "ALUGUEL"),
                nivel == 1 ? e -> cardLayout.show(painelPrincipal, "CADASTRO_BIKE") : null,
                nivel == 1 ? e -> cardLayout.show(painelPrincipal, "PROMOCAO") : null,
                nivel
        );
        janelaPrincipal.setJMenuBar(barraNavegacao.getBarraNavegacao());

        // --- FINALIZAÇÃO ---
        janelaPrincipal.add(painelPrincipal);
        janelaPrincipal.setLocationRelativeTo(null);
        cardLayout.show(painelPrincipal, "ALUGUEL"); // Mostra a tela de aluguel inicialmente
        janelaPrincipal.setVisible(true);
    }
}