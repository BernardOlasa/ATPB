package org.example.ui;

import org.example.data.GerenciadorDeDados;
import org.example.domain.Usuario;
import org.example.util.PaletaCores;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TelaLogin {
    JFrame frame = new JFrame("Login Wheels+");
    JPanel painelPrincipal = new JPanel(new BorderLayout());
    JTextField inputNome = new JTextField(20);
    JPasswordField inputSenha = new JPasswordField(20);
    JButton botaoLogin = new JButton("Login");
    GUI gui = new GUI();

    public void executar() {
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 30, 30));
        frame.add(painelPrincipal);

        JPanel painelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        painelLogo.setBackground(PaletaCores.SECONDARY_ACCENT);
        JLabel lblLogo = new JLabel("Wheels+");
        lblLogo.setFont(new Font("Serif", Font.BOLD, 50));
        lblLogo.setForeground(PaletaCores.TEXT_LIGHT);
        painelLogo.add(lblLogo);
        painelPrincipal.add(painelLogo, BorderLayout.NORTH);

        JPanel painelInputs = new JPanel();
        painelInputs.setBackground(PaletaCores.BACKGROUND);
        painelInputs.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNome = new JLabel("Usuário:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 16));
        lblNome.setForeground(PaletaCores.TEXT_DARK);
        painelInputs.add(lblNome, gbc);

        gbc.gridy++;
        inputNome.setFont(new Font("Arial", Font.PLAIN, 16));
        inputNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        painelInputs.add(inputNome, gbc);

        gbc.gridy++;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 16));
        lblSenha.setForeground(PaletaCores.TEXT_DARK);
        painelInputs.add(lblSenha, gbc);

        gbc.gridy++;
        inputSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        inputSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        painelInputs.add(inputSenha, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2; // Ocupa duas colunas
        gbc.insets = new Insets(20, 10, 10, 10);
        botaoLogin.setFont(new Font("Arial", Font.BOLD, 18));
        botaoLogin.setBackground(PaletaCores.PRIMARY_ACCENT);
        botaoLogin.setForeground(PaletaCores.TEXT_LIGHT);
        botaoLogin.setFocusPainted(false);
        botaoLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoLogin.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        painelInputs.add(botaoLogin, gbc);

        painelPrincipal.add(painelInputs, BorderLayout.CENTER);

        botaoLogin.addActionListener(e -> efetuarLogin());

        frame.getRootPane().setDefaultButton(botaoLogin);

        frame.setVisible(true);
    }

    public void efetuarLogin() {
        String nomeUsuario = inputNome.getText();
        String senha = new String(inputSenha.getPassword());

        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Usuario u : GerenciadorDeDados.usuarios) {
            if (nomeUsuario.hashCode() == u.getNome() && senha.hashCode() == u.getSenha()) {
                gui.executar(u.getNivel());
                frame.dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(frame, "Nome de usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
    }
}