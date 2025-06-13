package org.example.ui;

import org.example.domain.Bicicleta;
import org.example.domain.Cliente;
import org.example.util.PaletaCores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BicicletaCardDinamico extends JPanel {

    private final Bicicleta bicicleta;
    private final JLabel diariaValorLabel = new JLabel();
    private final JLabel seguroValorLabel = new JLabel();
    private final JLabel descontoValorLabel = new JLabel();
    private final JLabel finalValorLabel = new JLabel();
    private final Font VALOR_FONT = new Font("Arial", Font.BOLD, 12);

    public BicicletaCardDinamico(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
        setLayout(new BorderLayout(10, 5));
        setBackground(PaletaCores.PANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Aumenta a altura

        JPanel infoEstaticaPanel = new JPanel(new GridLayout(0, 2, 10, 2));
        infoEstaticaPanel.setOpaque(false); // Fundo transparente para herdar do painel principal
        infoEstaticaPanel.add(createStyledLabel("ID:", Font.BOLD));
        infoEstaticaPanel.add(createStyledLabel(String.valueOf(bicicleta.getIdBicicleta()), Font.PLAIN));
        infoEstaticaPanel.add(createStyledLabel("Modelo:", Font.BOLD));
        infoEstaticaPanel.add(createStyledLabel(bicicleta.getMarca() + " " + bicicleta.getModelo(), Font.PLAIN));
        add(infoEstaticaPanel, BorderLayout.NORTH);

        JPanel valoresPanel = new JPanel(new GridLayout(0, 2, 10, 2));
        valoresPanel.setOpaque(false);
        valoresPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        valoresPanel.add(createStyledLabel("Diária:", Font.BOLD));
        diariaValorLabel.setFont(VALOR_FONT);
        diariaValorLabel.setForeground(PaletaCores.TEXT_DARK);
        valoresPanel.add(diariaValorLabel);

        valoresPanel.add(createStyledLabel("Seguro:", Font.BOLD));
        seguroValorLabel.setFont(VALOR_FONT);
        seguroValorLabel.setForeground(PaletaCores.TEXT_DARK);
        valoresPanel.add(seguroValorLabel);

        valoresPanel.add(createStyledLabel("Desconto:", Font.BOLD));
        descontoValorLabel.setFont(VALOR_FONT);
        descontoValorLabel.setForeground(PaletaCores.DANGER);
        valoresPanel.add(descontoValorLabel);

        valoresPanel.add(createStyledLabel("Valor Final:", Font.BOLD));
        finalValorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        finalValorLabel.setForeground(PaletaCores.SUCCESS);
        valoresPanel.add(finalValorLabel);

        add(valoresPanel, BorderLayout.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel createStyledLabel(String text, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", style, 12));
        label.setForeground(PaletaCores.TEXT_DARK);
        return label;
    }


    public Bicicleta getBicicleta() {
        return this.bicicleta;
    }

    public void updateValores(int dias, Cliente cliente, PainelAluguel.CalculoHelper helper) {
        double valorDiaria = bicicleta.getValorAluguel();
        double pctDesconto = bicicleta.pegarMaiorPromocao();

        double valorSeguro = (cliente != null) ? helper.calcularSeguroProporcional(bicicleta, cliente) : 0.0;

        double valorFinal = 0.0;
        if (dias > 0) {
            double valorAluguelTotal = (valorDiaria * dias) * (1 - (pctDesconto / 100.0));
            valorFinal = valorAluguelTotal + valorSeguro;
        }

        diariaValorLabel.setText(String.format("R$ %.2f", valorDiaria));
        descontoValorLabel.setText(String.format("%.1f %%", pctDesconto));
        seguroValorLabel.setText(String.format("R$ %.2f", valorSeguro));
        finalValorLabel.setText(String.format("R$ %.2f", valorFinal));
    }
}