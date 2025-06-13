package org.example.Classes.GUI;

import com.toedter.calendar.JDateChooser;
import org.example.Classes.Bicicleta;
import org.example.Classes.Dados.GerenciadorDeDados;
import org.example.Classes.Promocao;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class PainelPromocao extends JPanel {

    private final JDateChooser dataInicioPromocao = new JDateChooser();
    private final JDateChooser dataFimPromocao = new JDateChooser();
    private final JTextField porcentagemInput = new JTextField();
    private final JCheckBox estacavelCheckbox = new JCheckBox();

    private final JPanel innerPanelDisponiveisPromocao;
    private final JPanel innerPanelSelecionadasPromocao;

    private final ArrayList<Bicicleta> bicicletasTodasTemp = new ArrayList<>();
    private final ArrayList<Bicicleta> bicicletasSelecionadas = new ArrayList<>();

    public PainelPromocao() {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL DE INPUTS DA PROMOÇÃO ---
        JPanel painelInputs = new JPanel(new GridLayout(0, 2, 10, 10));
        painelInputs.setBorder(BorderFactory.createTitledBorder("Dados da Promoção"));
        painelInputs.add(new JLabel("Data de Início:"));
        painelInputs.add(dataInicioPromocao);
        painelInputs.add(new JLabel("Data de Fim:"));
        painelInputs.add(dataFimPromocao);
        painelInputs.add(new JLabel("Porcentagem (%):"));
        painelInputs.add(porcentagemInput);
        painelInputs.add(new JLabel("Acumulativa:"));
        painelInputs.add(estacavelCheckbox);
        add(painelInputs, BorderLayout.NORTH);

        // --- PAINEL DE SELEÇÃO DE BICICLETAS ---
        JPanel painelSelecao = new JPanel(new GridLayout(1, 2, 10, 10));
        painelSelecao.setBorder(BorderFactory.createTitledBorder("Aplicar em"));

        innerPanelDisponiveisPromocao = new JPanel();
        innerPanelDisponiveisPromocao.setLayout(new BoxLayout(innerPanelDisponiveisPromocao, BoxLayout.Y_AXIS));
        painelSelecao.add(new JScrollPane(innerPanelDisponiveisPromocao));

        innerPanelSelecionadasPromocao = new JPanel();
        innerPanelSelecionadasPromocao.setLayout(new BoxLayout(innerPanelSelecionadasPromocao, BoxLayout.Y_AXIS));
        painelSelecao.add(new JScrollPane(innerPanelSelecionadasPromocao));
        add(painelSelecao, BorderLayout.CENTER);

        // --- BOTÃO DE CRIAR PROMOÇÃO ---
        JButton criarPromocaoBtn = new JButton("Criar Promoção");
        criarPromocaoBtn.addActionListener(e -> criarPromocao());
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.add(criarPromocaoBtn);
        add(painelBotao, BorderLayout.SOUTH);

        carregarBicicletas();
        atualizarPaineisDeBicicletas();
    }

    private void carregarBicicletas() {
        bicicletasTodasTemp.clear();
        bicicletasTodasTemp.addAll(GerenciadorDeDados.bicicletas);
        bicicletasSelecionadas.clear();
    }

    private void moverBicicleta(Bicicleta bike) {
        if (bicicletasTodasTemp.contains(bike)) {
            bicicletasTodasTemp.remove(bike);
            bicicletasSelecionadas.add(bike);
        } else {
            bicicletasSelecionadas.remove(bike);
            bicicletasTodasTemp.add(bike);
        }
        atualizarPaineisDeBicicletas();
    }

    private void atualizarPaineisDeBicicletas() {
        innerPanelDisponiveisPromocao.removeAll();
        bicicletasTodasTemp.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasTodasTemp) {
            innerPanelDisponiveisPromocao.add(new BicicletaCard(bike, e -> moverBicicleta(bike)));
        }

        innerPanelSelecionadasPromocao.removeAll();
        bicicletasSelecionadas.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasSelecionadas) {
            innerPanelSelecionadasPromocao.add(new BicicletaCard(bike, e -> moverBicicleta(bike)));
        }

        revalidate();
        repaint();
    }

    private void criarPromocao() {
        if (dataInicioPromocao.getDate() == null || dataFimPromocao.getDate() == null || porcentagemInput.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos da promoção.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (bicicletasSelecionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos uma bicicleta para a promoção.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Promocao novaPromocao = new Promocao(
                    dataInicioPromocao.getDate(),
                    dataFimPromocao.getDate(),
                    Double.parseDouble(porcentagemInput.getText()),
                    estacavelCheckbox.isSelected()
            );

            for (Bicicleta b : bicicletasSelecionadas) {
                b.getPromocoes().add(novaPromocao);
            }

            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
            JOptionPane.showMessageDialog(this, "Promoção aplicada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Resetar painel
            carregarBicicletas();
            atualizarPaineisDeBicicletas();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O valor da porcentagem é inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}