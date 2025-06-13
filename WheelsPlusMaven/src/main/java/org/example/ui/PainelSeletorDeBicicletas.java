package org.example.ui;

import org.example.domain.Bicicleta;
import org.example.util.PaletaCores;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class PainelSeletorDeBicicletas extends JPanel {

    private final ArrayList<Bicicleta> bicicletasDisponiveis = new ArrayList<>();
    private final ArrayList<Bicicleta> bicicletasSelecionadas = new ArrayList<>();

    private final JPanel panelDisponiveis;
    private final JPanel panelSelecionadas;
    private final CardFactory cardFactory;

    public PainelSeletorDeBicicletas(String tituloBorda, CardFactory cardFactory) {
        super(new GridLayout(1, 2, 10, 10));
        this.cardFactory = cardFactory;

        setOpaque(false);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(tituloBorda);
        titledBorder.setTitleColor(PaletaCores.TEXT_DARK);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        setBorder(titledBorder);

        panelDisponiveis = new JPanel();
        panelDisponiveis.setBackground(PaletaCores.BACKGROUND);
        panelDisponiveis.setLayout(new BoxLayout(panelDisponiveis, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelDisponiveis));

        panelSelecionadas = new JPanel();
        panelSelecionadas.setBackground(PaletaCores.BACKGROUND);
        panelSelecionadas.setLayout(new BoxLayout(panelSelecionadas, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelSelecionadas));
    }

    private void moverBicicleta(Bicicleta bike) {
        if (bicicletasDisponiveis.contains(bike)) {
            bicicletasDisponiveis.remove(bike);
            bicicletasSelecionadas.add(bike);
        } else {
            bicicletasSelecionadas.remove(bike);
            bicicletasDisponiveis.add(bike);
        }
        atualizarPaineis();
    }

    public void atualizarPaineis() {
        panelDisponiveis.removeAll();
        bicicletasDisponiveis.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasDisponiveis) {
            JPanel card = cardFactory.createCard(bike);
            // Adiciona o listener de duplo clique para mover.
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        moverBicicleta(bike);
                    }
                }
            });
            panelDisponiveis.add(card);
            panelDisponiveis.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        panelSelecionadas.removeAll();
        bicicletasSelecionadas.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasSelecionadas) {
            JPanel card = cardFactory.createCard(bike);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        moverBicicleta(bike);
                    }
                }
            });
            panelSelecionadas.add(card);
            panelSelecionadas.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        revalidate();
        repaint();
    }

    public void setBicicletasDisponiveis(List<Bicicleta> bicicletas) {
        this.bicicletasDisponiveis.clear();
        this.bicicletasDisponiveis.addAll(bicicletas);
        this.bicicletasSelecionadas.clear();
        atualizarPaineis();
    }

    public List<Bicicleta> getBicicletasSelecionadas() {
        return this.bicicletasSelecionadas;
    }

    public List<Component> getCardsSelecionados() {
        return List.of(panelSelecionadas.getComponents());
    }

    public List<Component> getCardsDisponiveis() {
        return List.of(panelDisponiveis.getComponents());
    }
}