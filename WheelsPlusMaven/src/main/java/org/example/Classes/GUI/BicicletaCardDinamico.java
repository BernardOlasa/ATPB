package org.example.Classes.GUI;

import org.example.Classes.Bicicleta;
import org.example.Classes.Cliente;

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
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Aumenta a altura

        // Painel de Informações Estáticas
        JPanel infoEstaticaPanel = new JPanel(new GridLayout(0, 2, 10, 2));
        infoEstaticaPanel.add(new JLabel("ID:"));
        infoEstaticaPanel.add(new JLabel(String.valueOf(bicicleta.getIdBicicleta())));
        infoEstaticaPanel.add(new JLabel("Modelo:"));
        infoEstaticaPanel.add(new JLabel(bicicleta.getMarca() + " " + bicicleta.getModelo()));
        add(infoEstaticaPanel, BorderLayout.NORTH);

        // Painel de Valores Dinâmicos
        JPanel valoresPanel = new JPanel(new GridLayout(0, 2, 10, 2));
        valoresPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Diária
        valoresPanel.add(new JLabel("Diária:"));
        diariaValorLabel.setFont(VALOR_FONT);
        valoresPanel.add(diariaValorLabel);

        // Seguro
        valoresPanel.add(new JLabel("Seguro:"));
        seguroValorLabel.setFont(VALOR_FONT);
        valoresPanel.add(seguroValorLabel);

        // Desconto
        valoresPanel.add(new JLabel("Desconto:"));
        descontoValorLabel.setFont(VALOR_FONT);
        valoresPanel.add(descontoValorLabel);

        // Valor Final
        valoresPanel.add(new JLabel("Valor Final:"));
        finalValorLabel.setFont(VALOR_FONT);
        finalValorLabel.setForeground(new Color(0, 100, 0)); // Verde escuro
        valoresPanel.add(finalValorLabel);

        add(valoresPanel, BorderLayout.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public Bicicleta getBicicleta() {
        return this.bicicleta;
    }

    /**
     * Atualiza os valores exibidos no card com base nos dias e no cliente selecionado.
     * @param dias a quantidade de dias do aluguel.
     * @param cliente o cliente que está alugando.
     * @param helper uma instância da classe que contém a lógica de cálculo.
     */
    public void updateValores(int dias, Cliente cliente, PainelAluguel.CalculoHelper helper) {
        double valorDiaria = bicicleta.getValorAluguel();
        double pctDesconto = bicicleta.pegarMaiorPromocao(); //

        // Calcula o seguro apenas se um cliente for selecionado
        double valorSeguro = (cliente != null) ? helper.calcularSeguroProporcional(bicicleta, cliente) : 0.0;

        // Calcula o valor final apenas se os dias forem selecionados
        double valorFinal = 0.0;
        if (dias > 0) {
            double valorAluguelTotal = (valorDiaria * dias) * (1 - (pctDesconto / 100.0));
            valorFinal = valorAluguelTotal + valorSeguro;
        }

        // Atualiza os JLabels
        diariaValorLabel.setText(String.format("R$ %.2f", valorDiaria));
        descontoValorLabel.setText(String.format("%.1f %%", pctDesconto));
        seguroValorLabel.setText(String.format("R$ %.2f", valorSeguro));
        finalValorLabel.setText(String.format("R$ %.2f", valorFinal));
    }
}