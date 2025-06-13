package org.example.ui;

import com.toedter.calendar.JDateChooser;
import org.example.data.GerenciadorDeDados;
import org.example.domain.Bicicleta;
import org.example.domain.Promocao;
import org.example.util.PaletaCores;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PainelPromocao extends JPanel {

    private final JDateChooser dataInicioPromocao = new JDateChooser();
    private final JDateChooser dataFimPromocao = new JDateChooser();
    private final JTextField porcentagemInput = new JTextField();
    private final JCheckBox estacavelCheckbox = new JCheckBox("Acumulativa");

    private final PainelSeletorDeBicicletas painelSeletor;

    public PainelPromocao() {
        super(new BorderLayout(10, 10));
        setBackground(PaletaCores.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelInputs = new JPanel(new GridBagLayout());
        painelInputs.setOpaque(false);
        TitledBorder titledBorderInputs = BorderFactory.createTitledBorder("Dados da Promoção");
        titledBorderInputs.setTitleColor(PaletaCores.TEXT_DARK);
        titledBorderInputs.setTitleFont(new Font("Arial", Font.BOLD, 14));
        painelInputs.setBorder(BorderFactory.createCompoundBorder(
                titledBorderInputs,
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; painelInputs.add(createStyledLabel("Data de Início:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; painelInputs.add(dataInicioPromocao, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelInputs.add(createStyledLabel("Data de Fim:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelInputs.add(dataFimPromocao, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelInputs.add(createStyledLabel("Porcentagem (%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelInputs.add(porcentagemInput, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        estacavelCheckbox.setOpaque(false);
        estacavelCheckbox.setForeground(PaletaCores.TEXT_DARK);
        estacavelCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        painelInputs.add(estacavelCheckbox, gbc);

        add(painelInputs, BorderLayout.NORTH);

        CardFactory fabricaDeCardsPromocao = bicicleta -> new BicicletaCard(bicicleta, null);

        painelSeletor = new PainelSeletorDeBicicletas("Aplicar em", fabricaDeCardsPromocao);
        add(painelSeletor, BorderLayout.CENTER);

        JButton criarPromocaoBtn = new JButton("Criar Promoção");
        criarPromocaoBtn.setBackground(PaletaCores.PRIMARY_ACCENT);
        criarPromocaoBtn.setForeground(PaletaCores.TEXT_LIGHT);
        criarPromocaoBtn.setFont(new Font("Arial", Font.BOLD, 14));
        criarPromocaoBtn.addActionListener(e -> criarPromocao());
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        painelBotao.setOpaque(false);
        painelBotao.add(criarPromocaoBtn);
        add(painelBotao, BorderLayout.SOUTH);

        refreshData();
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(PaletaCores.TEXT_DARK);
        return label;
    }

    public void refreshData() {
        List<Bicicleta> bicicletasAtivas = GerenciadorDeDados.bicicletas.stream()
                .filter(b -> !b.isExcluida())
                .collect(Collectors.toList());
        painelSeletor.setBicicletasDisponiveis(bicicletasAtivas);
    }

    private void criarPromocao() {
        if (dataInicioPromocao.getDate() == null || dataFimPromocao.getDate() == null || porcentagemInput.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos da promoção.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (painelSeletor.getBicicletasSelecionadas().isEmpty()) {
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

            for (Bicicleta b : painelSeletor.getBicicletasSelecionadas()) {
                b.getPromocoes().add(novaPromocao);
            }

            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
            JOptionPane.showMessageDialog(this, "Promoção aplicada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            refreshData();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O valor da porcentagem é inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}