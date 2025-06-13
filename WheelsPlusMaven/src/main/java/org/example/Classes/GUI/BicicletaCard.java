package org.example.Classes.GUI;

import org.example.Classes.Bicicleta;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BicicletaCard extends JPanel {

    public BicicletaCard(Bicicleta bicicleta, ActionListener onDoubleClick) {
        super(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(300, 90));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel painelInformacoes = new JPanel(new GridLayout(0, 2, 5, 2));
        painelInformacoes.add(new JLabel("ID:"));
        painelInformacoes.add(new JLabel(String.valueOf(bicicleta.getIdBicicleta())));
        painelInformacoes.add(new JLabel("Marca:"));
        painelInformacoes.add(new JLabel(bicicleta.getMarca()));
        painelInformacoes.add(new JLabel("Modelo:"));
        painelInformacoes.add(new JLabel(bicicleta.getModelo()));
        painelInformacoes.add(new JLabel("Preço:"));
        painelInformacoes.add(new JLabel(String.format("R$ %.2f", bicicleta.getValorAluguel())));

        add(painelInformacoes, BorderLayout.CENTER);

        // Ação de clique duplo para mover o card
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onDoubleClick.actionPerformed(null);
                }
            }
        });
    }
}