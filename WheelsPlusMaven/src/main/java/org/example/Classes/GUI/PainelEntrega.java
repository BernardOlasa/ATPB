package org.example.Classes.GUI;

import org.example.Classes.Aluguel;
import org.example.Classes.Bicicleta;
import org.example.Classes.Dados.GerenciadorDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PainelEntrega extends JPanel {

    private final JPanel listaBikesPanel;

    public PainelEntrega() {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Bicicletas Aguardando Entrega", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        listaBikesPanel = new JPanel();
        listaBikesPanel.setLayout(new BoxLayout(listaBikesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listaBikesPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void carregarBicicletasAlugadas() {
        listaBikesPanel.removeAll();

        List<Bicicleta> bicicletasAlugadas = GerenciadorDeDados.bicicletas.stream()
                .filter(Bicicleta::isAlugada)
                .sorted(Comparator.comparingInt(Bicicleta::getIdBicicleta))
                .collect(Collectors.toList());

        if (bicicletasAlugadas.isEmpty()) {
            listaBikesPanel.add(new JLabel("Nenhuma bicicleta alugada no momento."));
        } else {
            for (Bicicleta bike : bicicletasAlugadas) {
                Optional<Aluguel> aluguelOpt = GerenciadorDeDados.alugueis.stream()
                        .filter(aluguel -> !aluguel.isBicicletasDevolvidas())
                        .filter(aluguel -> aluguel.getBicicletas().stream().anyMatch(b -> b.getIdBicicleta() == bike.getIdBicicleta()))
                        .max(Comparator.comparing(Aluguel::getDataAluguel));

                listaBikesPanel.add(criarCardEntrega(bike, aluguelOpt.orElse(null)));
                listaBikesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel criarCardEntrega(Bicicleta bike, Aluguel aluguel) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 2));
        infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        infoPanel.add(new JLabel("ID da Bicicleta:"));
        infoPanel.add(new JLabel(String.valueOf(bike.getIdBicicleta())));
        infoPanel.add(new JLabel("Modelo:"));
        infoPanel.add(new JLabel(bike.getMarca() + " " + bike.getModelo()));
        infoPanel.add(new JLabel("Cliente:"));
        infoPanel.add(new JLabel(aluguel != null ? aluguel.getCliente().getNome() : "Não encontrado"));

        JButton entregarBtn = new JButton("Confirmar Entrega");
        entregarBtn.addActionListener(e -> entregarBicicleta(bike));

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(entregarBtn, BorderLayout.EAST);

        return card;
    }

    private void entregarBicicleta(Bicicleta bike) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja confirmar a entrega da bicicleta ID " + bike.getIdBicicleta() + "?",
                "Confirmação de Entrega",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            bike.setAlugada(false); // Marca a bicicleta como disponível

            // Encontra o aluguel ao qual esta bicicleta pertence
            Aluguel aluguelDaBike = GerenciadorDeDados.alugueis.stream()
                    .filter(a -> !a.isBicicletasDevolvidas()) // Apenas aluguéis ativos
                    .filter(a -> a.getBicicletas().stream().anyMatch(b -> b.getIdBicicleta() == bike.getIdBicicleta()))
                    .max(Comparator.comparing(Aluguel::getDataAluguel)) // Pega o mais recente
                    .orElse(null);

            if (aluguelDaBike != null) {
                // Verifica se todas as bicicletas deste aluguel específico já foram devolvidas
                boolean aluguelCompleto = aluguelDaBike.getBicicletas().stream()
                        .allMatch(b -> !b.isAlugada());

                if (aluguelCompleto) {
                    aluguelDaBike.setBicicletasDevolvidas(true); // Marca o aluguel como devolvido
                    GerenciadorDeDados.salvarDados("Alugueis", GerenciadorDeDados.alugueis);
                }
            }

            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
            JOptionPane.showMessageDialog(this, "Bicicleta entregue com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarBicicletasAlugadas();
        }
    }
}