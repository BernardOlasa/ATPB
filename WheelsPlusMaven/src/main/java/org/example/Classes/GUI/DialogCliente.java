package org.example.Classes.GUI;

import org.example.Classes.Cliente;
import org.example.Classes.Util;

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

        // Painel de Inputs
        JPanel painelInputs = new JPanel(new GridBagLayout());
        painelInputs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // CPF
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelInputs.add(new JLabel("CPF:"), gbc);
        cpfInput = createCpfField();
        gbc.gridx = 1;
        painelInputs.add(cpfInput, gbc);

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelInputs.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(nomeInput, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelInputs.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(emailInput, gbc);

        // Score
        gbc.gridx = 0;
        gbc.gridy = 3;
        painelInputs.add(new JLabel("Score:"), gbc);
        gbc.gridx = 1;
        painelInputs.add(scoreInput, gbc);

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

        if (cliente != null) {
            preencherCampos();
            cpfInput.setEditable(false);
        }

        pack();
        setLocationRelativeTo(parent);
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