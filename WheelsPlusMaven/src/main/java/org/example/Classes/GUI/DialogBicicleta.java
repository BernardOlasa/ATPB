package org.example.Classes.GUI;

import org.example.Classes.Bicicleta;
import org.example.Classes.Promocao;

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
    private final JCheckBox specialistInput = new JCheckBox();
    private final JCheckBox emManutencaoInput = new JCheckBox();

    private Bicicleta bicicleta;
    private boolean confirmado = false;

    public DialogBicicleta(JFrame parent, Bicicleta bicicleta) {
        super(parent, true);
        this.bicicleta = bicicleta;

        setTitle(bicicleta == null ? "Adicionar Bicicleta" : "Editar Bicicleta");
        setLayout(new BorderLayout(10, 10));

        JPanel painelInputs = new JPanel(new GridBagLayout());
        painelInputs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridy = 0;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(marcaInput, gbc);

        // Linha 1
        gbc.gridy = 1;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(modeloInput, gbc);

        // Linha 2
        gbc.gridy = 2;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Cor:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(corInput, gbc);

        // Linha 3
        gbc.gridy = 3;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Aro:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(aroInput, gbc);

        // Linha 4
        gbc.gridy = 4;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Material do Aro:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(aroMaterialInput, gbc);

        // Linha 5
        gbc.gridy = 5;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Material da Bicicleta:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(bikeMaterialInput, gbc);

        // Linha 6
        gbc.gridy = 6;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Valor Diária (R$):"), gbc);
        gbc.gridx = 1;
        painelInputs.add(valorDiariaInput, gbc);

        // Linha 7
        gbc.gridy = 7;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Valor Seguro (R$):"), gbc);
        gbc.gridx = 1;
        painelInputs.add(valorSeguroInput, gbc);

        // Linha 8
        gbc.gridy = 8;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Especialista:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(specialistInput, gbc);

        // Linha 9
        gbc.gridy = 9;
        gbc.gridx = 0;
        painelInputs.add(new JLabel("Em Manutenção:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(emManutencaoInput, gbc);

        add(painelInputs, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
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
                        0, // ID será definido no PainelCRUDBicicleta
                        Integer.parseInt(aroInput.getSelectedItem().toString()),
                        Double.parseDouble(valorDiariaInput.getText()),
                        valorSeguro,
                        specialistInput.isSelected(),
                        false, // Nova bicicleta não está alugada
                        emManutencaoInput.isSelected(),
                        false, // Nova bicicleta não está excluída
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
                // O status de 'excluida' não é alterado aqui
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