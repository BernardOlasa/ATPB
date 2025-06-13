package org.example.ui;

import org.example.domain.Cliente;
import org.example.util.PaletaCores;
import org.example.util.Util;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class DialogCliente extends JDialog {

    private final JTextField nomeInput = new JTextField(20);
    private final JTextField emailInput = new JTextField(20);
    private final JFormattedTextField cpfInput;
    private final JTextField scoreInput = new JTextField(20);

    private Cliente cliente;
    private boolean confirmado = false;

    public DialogCliente(JFrame parent, Cliente cliente) {
        super(parent, true);
        this.cliente = cliente;

        setTitle(cliente == null ? "Adicionar Cliente" : "Editar Cliente");
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(PaletaCores.BACKGROUND);

        JPanel painelInputs = new JPanel(new GridBagLayout());
        painelInputs.setOpaque(false);
        painelInputs.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; painelInputs.add(createStyledLabel("CPF:"), gbc);
        cpfInput = createCpfField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; painelInputs.add(cpfInput, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelInputs.add(createStyledLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelInputs.add(nomeInput, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelInputs.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelInputs.add(emailInput, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelInputs.add(createStyledLabel("Score:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelInputs.add(scoreInput, gbc);

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

        if (cliente != null) {
            preencherCampos();
            cpfInput.setEditable(false);
        }

        pack();
        setLocationRelativeTo(parent);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(PaletaCores.TEXT_LIGHT);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(PaletaCores.TEXT_DARK);
        return label;
    }

    private JFormattedTextField createCpfField() {
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter(' ');
            return new JFormattedTextField(mask);
        } catch (ParseException e) {
            return new JFormattedTextField();
        }
    }

    private void preencherCampos() {
        cpfInput.setText(cliente.getCpf());
        nomeInput.setText(cliente.getNome());
        emailInput.setText(cliente.getEmail());
        scoreInput.setText(String.valueOf(cliente.getScore()));
    }

    private boolean validarCampos() {
        Util util = new Util();
        if (cpfInput.getText().isBlank() || nomeInput.getText().isBlank() || emailInput.getText().isBlank() || scoreInput.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!util.conferirEmail(emailInput.getText())) {
            JOptionPane.showMessageDialog(this, "Formato de email inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(scoreInput.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Score deve ser um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void salvar() {
        if (validarCampos()) {
            if (this.cliente == null) {
                this.cliente = new Cliente(
                        cpfInput.getText(),
                        nomeInput.getText(),
                        emailInput.getText(),
                        Double.parseDouble(scoreInput.getText())
                );
            } else {
                this.cliente.setNome(nomeInput.getText());
                this.cliente.setEmail(emailInput.getText());
                this.cliente.setScore(Double.parseDouble(scoreInput.getText()));
            }
            confirmado = true;
            dispose();
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Cliente getCliente() {
        return cliente;
    }
}