package org.example.Classes.GUI;

import org.example.Classes.Dados.GerenciadorDeDados;
import org.example.Classes.Usuario;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class TelaLogin {
    JFrame frame = new JFrame("Login Wheels+");
    JPanel painelLogin = new JPanel();
    JPanel painelInputNome = new JPanel();
    JPanel painelInputSenha = new JPanel();
    JPanel painelBotaoLogin = new JPanel();
    JLabel lblNome = new JLabel("Nome de Usuario");
    JTextField inputNome = new JTextField();
    JLabel lblSenha = new JLabel("Senha");
    JPasswordField inputSenha = new JPasswordField();
    JButton botaoLogin = new JButton("Login");
    JPanel painelLogo;
    JLabel lblLogo;
    GUI gui = new GUI();

    public void executar() {
        frame.setSize(500, 400);
        painelLogin.setLayout(new GridLayout(4, 1, 25,25));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(painelLogin);
        painelLogo = new JPanel();
        painelLogo.setBackground(Color.black);
        painelLogo.setLayout(new FlowLayout(FlowLayout.CENTER));
        lblLogo = new JLabel("Wheels +");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        painelLogo.add(lblLogo);
        painelLogin.add(painelLogo);


        painelInputNome.setLayout(new GridBagLayout());
        painelLogin.add(painelInputNome);
        painelInputSenha.setLayout(new GridBagLayout());
        painelLogin.add(painelInputSenha);
        painelBotaoLogin.setLayout(new GridBagLayout());
        painelLogin.add(painelBotaoLogin);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(2, 2, 2, 2);
        painelInputSenha.add(lblSenha,c);
        painelInputNome.add(lblNome,c);
        painelBotaoLogin.add(botaoLogin,c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(2, 2, 2, 2);
        painelInputSenha.add(inputSenha, c);
        painelInputNome.add(inputNome,c);




        botaoLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                efetuarLogin();

            }
        });


        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 30, 30));

        lblLogo.setFont(new Font("Times New Roman", Font.BOLD, 40));
        lblLogo.setForeground(Color.orange);
        painelInputNome.setBackground(Color.black);
        painelInputSenha.setBackground(Color.black);
        painelLogin.setBackground(Color.black);
        painelBotaoLogin.setBackground(Color.black);


        lblNome.setHorizontalAlignment(SwingConstants.CENTER);
        lblNome.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNome.setForeground(Color.orange);
        inputNome.setHorizontalAlignment(SwingConstants.CENTER);
        inputNome.setBackground(Color.DARK_GRAY);
        inputNome.setFont(new Font("Times New Roman", Font.BOLD, 20));
        inputNome.setForeground(Color.white);
        inputNome.setPreferredSize(new Dimension(300, 30));
        inputNome.setMaximumSize(inputNome.getPreferredSize());
        inputNome.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.BLACK));





        lblSenha.setHorizontalAlignment(SwingConstants.CENTER);
        lblSenha.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblSenha.setForeground(Color.orange);
        inputSenha.setHorizontalAlignment(SwingConstants.CENTER);
        inputSenha.setBackground(Color.DARK_GRAY);
        inputSenha.setFont(new Font("Times New Roman", Font.BOLD, 20));
        inputSenha.setForeground(Color.white);
        inputSenha.setPreferredSize(new Dimension(300, 30));
        inputSenha.setMaximumSize(inputSenha.getPreferredSize());
        inputSenha.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.BLACK));

        botaoLogin.setBackground(Color.ORANGE);
        botaoLogin.setForeground(Color.WHITE);
        botaoLogin.setVerticalAlignment(SwingConstants.CENTER);
        botaoLogin.setFont(new Font("Times New Roman", Font.BOLD, 20));
        botaoLogin.setPreferredSize(new Dimension(150, 150));
        botaoLogin.setMaximumSize(inputNome.getPreferredSize());

        frame.setVisible(true);

    }
    public void efetuarLogin() {
        if (!(inputNome.getText().equals("") || inputSenha.getText().equals(""))) {
            for (Usuario u : GerenciadorDeDados.usuarios) {
                if (inputNome.getText().hashCode() == u.getNome() && inputSenha.getText().hashCode() == u.getSenha()) {
                    gui.executar(u.getNivel());
                    frame.dispose();
                    return;
                    }
                }
            }
        }
    }
