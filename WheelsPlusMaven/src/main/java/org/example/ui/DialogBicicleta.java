package org.example.ui;

import org.example.domain.Bicicleta;
import org.example.domain.Promocao;
import org.example.util.PaletaCores;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DialogBicicleta extends JDialog {

    private final JTextField marcaInput = new JTextField(20);
    private final JTextField modeloInput = new JTextField(20);
    private final JComboBox<String> aroInput = new JComboBox<>(new String[]{"", "12", "16", "20", "24", "26", "29", "650", "700"});
    private final JComboBox<String> aroMaterialInput = new JComboBox<>(new String[]{"", "Alumínio Carbono", "Aço Carbono", "Alumínio", "Fibra de Carbono", "Titânio", "Magnésio"});
    private final JComboBox<String> bikeMaterialInput = new JComboBox<>(new String[]{"", "Aço", "Alumínio", "Fibra de Carbono", "Titânio"});
    private final JTextField valorDiariaInput = new JTextField(20);
    private final JTextField valorSeguroInput = new JTextField(20);
    private final JTextField corInput = new JTextField(20);
    private final JCheckBox specialistInput = new JCheckBox("Especialista");
    private final JCheckBox emManutencaoInput = new JCheckBox("Em Manutenção");

    private Bicicleta bicicleta;
    private boolean confirmado = false;

    public DialogBicicleta(JFrame parent, Bicicleta bicicleta) {
        super(parent, true);
        this.bicicleta = bicicleta;

        setTitle(bicicleta == null ? "Adicionar Bicicleta" : "Editar Bicicleta");
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(PaletaCores.BACKGROUND);

        JPanel painelInputs = new JPanel(new GridBagLayout());
        painelInputs.setOpaque(false);
        painelInputs.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; painelInputs.add(createStyledLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; painelInputs.add(marcaInput, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelInputs.add(createStyledLabel("Modelo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelInputs.add(modeloInput, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelInputs.add(createStyledLabel("Cor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelInputs.add(corInput, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelInputs.add(createStyledLabel("Aro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelInputs.add(aroInput, gbc);

        gbc.gridx = 0; gbc.gridy = 4; painelInputs.add(createStyledLabel("Material do Aro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; painelInputs.add(aroMaterialInput, gbc);

        gbc.gridx = 0; gbc.gridy = 5; painelInputs.add(createStyledLabel("Material da Bicicleta:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; painelInputs.add(bikeMaterialInput, gbc);

        gbc.gridx = 0; gbc.gridy = 6; painelInputs.add(createStyledLabel("Valor Diária (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; painelInputs.add(valorDiariaInput, gbc);

        gbc.gridx = 0; gbc.gridy = 7; painelInputs.add(createStyledLabel("Valor Seguro (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; painelInputs.add(valorSeguroInput, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0; gbc.gridwidth = 2;
        styleCheckBox(specialistInput);
        painelInputs.add(specialistInput, gbc);

        gbc.gridy = 9;
        styleCheckBox(emManutencaoInput);
        painelInputs.add(emManutencaoInput, gbc);


        add(painelInputs, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        styleButton(btnSalvar, PaletaCores.PRIMARY_ACCENT);
        styleButton(btnCancelar, PaletaCores.SECONDARY_ACCENT);

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        if (bicicleta != null) {
            preencherCampos();
        }

        pack();
        setLocationRelativeTo(parent);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(PaletaCores.TEXT_LIGHT);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setOpaque(false);
        checkBox.setForeground(PaletaCores.TEXT_DARK);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(PaletaCores.TEXT_DARK);
        return label;
    }

    private void preencherCampos() {
        marcaInput.setText(bicicleta.getMarca());
        modeloInput.setText(bicicleta.getModelo());
        corInput.setText(bicicleta.getCor());
        aroInput.setSelectedItem(String.valueOf(bicicleta.getAroDaRoda()));
        aroMaterialInput.setSelectedItem(bicicleta.getMaterialDoAro());
        bikeMaterialInput.setSelectedItem(bicicleta.getMaterialDaBicicleta());
        valorDiariaInput.setText(String.valueOf(bicicleta.getValorAluguel()));
        valorSeguroInput.setText(String.valueOf(bicicleta.getValorSeguro()));
        specialistInput.setSelected(bicicleta.isSpecialist());
        emManutencaoInput.setSelected(bicicleta.isEmManutencao());
    }

    private boolean validarCampos() {
        if (marcaInput.getText().isBlank() || modeloInput.getText().isBlank() || corInput.getText().isBlank() ||
                aroInput.getSelectedItem().toString().isBlank() || valorDiariaInput.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Campos obrigatórios: Marca, Modelo, Cor, Aro e Valor da Diária.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(valorDiariaInput.getText());
            if (!valorSeguroInput.getText().isBlank()) {
                Double.parseDouble(valorSeguroInput.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores de diária e seguro devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void salvar() {
        if (validarCampos()) {
            double valorSeguro = valorSeguroInput.getText().isBlank() ? 0.0 : Double.parseDouble(valorSeguroInput.getText());

            if (this.bicicleta == null) {
                this.bicicleta = new Bicicleta(
                        0,
                        Integer.parseInt(aroInput.getSelectedItem().toString()),
                        Double.parseDouble(valorDiariaInput.getText()),
                        valorSeguro,
                        specialistInput.isSelected(),
                        false,
                        emManutencaoInput.isSelected(),
                        false,
                        new ArrayList<Promocao>(),
                        marcaInput.getText(),
                        modeloInput.getText(),
                        corInput.getText(),
                        aroMaterialInput.getSelectedItem().toString(),
                        bikeMaterialInput.getSelectedItem().toString()
                );
            } else {
                this.bicicleta.setMarca(marcaInput.getText());
                this.bicicleta.setModelo(modeloInput.getText());
                this.bicicleta.setCor(corInput.getText());
                this.bicicleta.setAroDaRoda(Integer.parseInt(aroInput.getSelectedItem().toString()));
                this.bicicleta.setMaterialDoAro(aroMaterialInput.getSelectedItem().toString());
                this.bicicleta.setMaterialDaBicicleta(bikeMaterialInput.getSelectedItem().toString());
                this.bicicleta.setValorAluguel(Double.parseDouble(valorDiariaInput.getText()));
                this.bicicleta.setValorSeguro(valorSeguro);
                this.bicicleta.setSpecialist(specialistInput.isSelected());
                this.bicicleta.setEmManutencao(emManutencaoInput.isSelected());
            }
            confirmado = true;
            dispose();
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }
}