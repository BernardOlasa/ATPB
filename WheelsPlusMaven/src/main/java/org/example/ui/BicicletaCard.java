package org.example.ui;

import org.example.domain.Bicicleta;
import org.example.util.PaletaCores;

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
        setBackground(PaletaCores.PANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel painelInformacoes = new JPanel(new GridLayout(0, 2, 5, 2));
        painelInformacoes.setBackground(PaletaCores.PANEL_BACKGROUND);

        painelInformacoes.add(createStyledLabel("ID:"));
        painelInformacoes.add(createStyledLabel(String.valueOf(bicicleta.getIdBicicleta())));
        painelInformacoes.add(createStyledLabel("Marca:"));
        painelInformacoes.add(createStyledLabel(bicicleta.getMarca()));
        painelInformacoes.add(createStyledLabel("Modelo:"));
        painelInformacoes.add(createStyledLabel(bicicleta.getModelo()));
        painelInformacoes.add(createStyledLabel("Preço:"));
        painelInformacoes.add(createStyledLabel(String.format("R$ %.2f", bicicleta.getValorAluguel())));

        add(painelInformacoes, BorderLayout.CENTER);

        if (onDoubleClick != null) {
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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(PaletaCores.TEXT_DARK);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }
}