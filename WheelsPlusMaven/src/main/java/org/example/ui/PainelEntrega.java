package org.example.ui;

import org.example.domain.Aluguel;
import org.example.domain.Bicicleta;
import org.example.data.GerenciadorDeDados;
import org.example.util.PaletaCores;

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
        setBackground(PaletaCores.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Bicicletas Aguardando Entrega", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(PaletaCores.TEXT_DARK);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        listaBikesPanel = new JPanel();
        listaBikesPanel.setBackground(PaletaCores.BACKGROUND);
        listaBikesPanel.setLayout(new BoxLayout(listaBikesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listaBikesPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void carregarBicicletasAlugadas() {
        listaBikesPanel.removeAll();

        List<Bicicleta> bicicletasAlugadas = GerenciadorDeDados.bicicletas.stream()
                .filter(Bicicleta::isAlugada)
                .sorted(Comparator.comparingInt(Bicicleta::getIdBicicleta))
                .collect(Collectors.toList());

        if (bicicletasAlugadas.isEmpty()) {
            JLabel aviso = new JLabel("Nenhuma bicicleta alugada no momento.");
            aviso.setFont(new Font("Arial", Font.ITALIC, 16));
            aviso.setForeground(PaletaCores.TEXT_DARK);
            listaBikesPanel.add(aviso);
        } else {
            for (Bicicleta bike : bicicletasAlugadas) {
                Optional<Aluguel> aluguelOpt = GerenciadorDeDados.alugueis.stream()
                        .filter(aluguel -> !aluguel.isBicicletasDevolvidas())
                        .filter(aluguel -> aluguel.getBicicletas().stream().anyMatch(b -> b.getIdBicicleta() == bike.getIdBicicleta()))
                        .max(Comparator.comparing(Aluguel::getDataAluguel));

                listaBikesPanel.add(criarCardEntrega(bike, aluguelOpt.orElse(null)));
                listaBikesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel criarCardEntrega(Bicicleta bike, Aluguel aluguel) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(PaletaCores.PANEL_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        infoPanel.setOpaque(false);
        infoPanel.add(createStyledLabel("ID da Bicicleta:"));
        infoPanel.add(createStyledLabel(String.valueOf(bike.getIdBicicleta())));
        infoPanel.add(createStyledLabel("Modelo:"));
        infoPanel.add(createStyledLabel(bike.getMarca() + " " + bike.getModelo()));
        infoPanel.add(createStyledLabel("Cliente:"));
        infoPanel.add(createStyledLabel(aluguel != null ? aluguel.getCliente().getNome() : "Não encontrado"));

        JButton entregarBtn = new JButton("Confirmar Entrega");
        entregarBtn.setBackground(PaletaCores.PRIMARY_ACCENT);
        entregarBtn.setForeground(PaletaCores.TEXT_LIGHT);
        entregarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        entregarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        entregarBtn.addActionListener(e -> entregarBicicleta(bike));

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(entregarBtn, BorderLayout.EAST);

        return card;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(PaletaCores.TEXT_DARK);
        return label;
    }

    private void entregarBicicleta(Bicicleta bike) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja confirmar a entrega da bicicleta ID " + bike.getIdBicicleta() + "?",
                "Confirmação de Entrega",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            bike.setAlugada(false);

            Aluguel aluguelDaBike = GerenciadorDeDados.alugueis.stream()
                    .filter(a -> !a.isBicicletasDevolvidas())
                    .filter(a -> a.getBicicletas().stream().anyMatch(b -> b.getIdBicicleta() == bike.getIdBicicleta()))
                    .max(Comparator.comparing(Aluguel::getDataAluguel))
                    .orElse(null);

            if (aluguelDaBike != null) {
                boolean aluguelCompleto = aluguelDaBike.getBicicletas().stream()
                        .allMatch(b -> !b.isAlugada());

                if (aluguelCompleto) {
                    aluguelDaBike.setBicicletasDevolvidas(true);
                    GerenciadorDeDados.salvarDados("Alugueis", GerenciadorDeDados.alugueis);
                }
            }

            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
            JOptionPane.showMessageDialog(this, "Bicicleta entregue com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarBicicletasAlugadas();
        }
    }
}