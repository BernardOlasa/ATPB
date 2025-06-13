package org.example.Classes.GUI;

import org.example.Classes.Bicicleta;
import org.example.Classes.Dados.GerenciadorDeDados;
import org.example.Classes.Promocao;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PainelCriarBicicleta extends JPanel {

    private final JTextField marcaInput = new JTextField();
    private final JTextField modeloInput = new JTextField();
    private final JComboBox<String> aroInput = new JComboBox<>(new String[]{"", "12", "16", "20", "24", "26", "29", "650", "700"});
    private final JComboBox<String> aroMaterialInput = new JComboBox<>(new String[]{"", "Alumínio Carbono", "Aço Carbono", "Alumínio", "Fibra de Carbono", "Titânio", "Magnésio"});
    private final JComboBox<String> bikeMaterialInput = new JComboBox<>(new String[]{"", "Aço", "Alumínio", "Fibra de Carbono", "Titânio"});
    private final JTextField valorDiariaInput = new JTextField();
    private final JTextField valorSeguroInput = new JTextField();
    private final JTextField corInput = new JTextField();
    private final JCheckBox specialistInput = new JCheckBox();

    public PainelCriarBicicleta() {
        super(new GridLayout(0, 2, 20, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Marca:"));
        add(marcaInput);
        add(new JLabel("Modelo:"));
        add(modeloInput);
        add(new JLabel("Aro:"));
        add(aroInput);
        add(new JLabel("Material do Aro:"));
        add(aroMaterialInput);
        add(new JLabel("Material da Bicicleta:"));
        add(bikeMaterialInput);
        add(new JLabel("Valor Diária (R$):"));
        add(valorDiariaInput);
        add(new JLabel("Valor Seguro (R$):"));
        add(valorSeguroInput);
        add(new JLabel("Cor:"));
        add(corInput);
        add(new JLabel("Especialista:"));
        add(specialistInput);

        add(new JLabel()); // Placeholder
        JButton criarBikeBtn = new JButton("Criar Bicicleta");
        criarBikeBtn.addActionListener(e -> criarBicicleta());
        add(criarBikeBtn);
    }

    private void criarBicicleta() {
        if (marcaInput.getText().isEmpty() || modeloInput.getText().isEmpty() || corInput.getText().isEmpty() ||
                aroInput.getSelectedItem().toString().isEmpty() || aroMaterialInput.getSelectedItem().toString().isEmpty() ||
                bikeMaterialInput.getSelectedItem().toString().isEmpty() || valorDiariaInput.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Confira se todos os campos estão preenchidos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int novoId = GerenciadorDeDados.bicicletas.stream().mapToInt(Bicicleta::getIdBicicleta).max().orElse(0) + 1;

            Bicicleta novaBike = new Bicicleta(
                    novoId,
                    Integer.parseInt(aroInput.getSelectedItem().toString()),
                    Double.parseDouble(valorDiariaInput.getText()),
                    Double.parseDouble(valorSeguroInput.getText().isEmpty() ? "0" : valorSeguroInput.getText()),
                    specialistInput.isSelected(),
                    false, false,
                    new ArrayList<Promocao>(),
                    marcaInput.getText(),
                    modeloInput.getText(),
                    corInput.getText(),
                    aroMaterialInput.getSelectedItem().toString(),
                    bikeMaterialInput.getSelectedItem().toString()
            );

            GerenciadorDeDados.bicicletas.add(novaBike);
            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);

            JOptionPane.showMessageDialog(this, "Bicicleta criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos (diária, seguro).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        marcaInput.setText("");
        modeloInput.setText("");
        aroInput.setSelectedIndex(0);
        aroMaterialInput.setSelectedIndex(0);
        bikeMaterialInput.setSelectedIndex(0);
        valorDiariaInput.setText("");
        valorSeguroInput.setText("");
        corInput.setText("");
        specialistInput.setSelected(false);
    }
}