package org.example.Classes.GUI;

import org.example.Classes.Cliente;
import org.example.Classes.Util;
import org.example.Classes.Dados.GerenciadorDeDados;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class PainelCadastro {


    static JFrame frame = new JFrame();
    JPanel painelCadastro = new JPanel();
    JPanel inputs;
    JPanel cpfPanel;
    JFormattedTextField cpfInput;
    JLabel cpfErrorMsg;
    JPanel nomePanel;
    JTextField nomeInput;
    JLabel nomeErrorMsg;
    JPanel emailPanel;
    JTextField emailInput;
    JLabel emailErrorMsg;
    JPanel botaoCadastroPanel;
    JButton botaoCadastrar;



    public PainelCadastro() {

        new JFrame();
        frame.setSize(400, 200);
        frame.setResizable(false);



        painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridLayout(1,2));


        inputs = new JPanel();
        inputs.setLayout(new GridLayout(2, 2, 50, 50));
        painelCadastro.add(inputs);


        //region CampoCPF

        cpfPanel = new JPanel();

        cpfPanel.setLayout(new GridLayout(2, 2));
        cpfPanel.add(new JLabel("CPF"));

        try {
            MaskFormatter maskFormatter = new MaskFormatter("###.###.###-##");
            maskFormatter.setValidCharacters("0123456789");
            cpfInput = new JFormattedTextField(maskFormatter);
            cpfInput.setColumns(14);
            cpfPanel.add(cpfInput);
            inputs.add(cpfPanel);
            cpfErrorMsg = new JLabel("CPF invalido");
            cpfErrorMsg.setForeground(Color.RED);
            cpfPanel.add(cpfErrorMsg);
        }catch(ParseException e){}

        //endregion CampoCPF

        //region CampoNome

        nomePanel = new JPanel();

        nomePanel.setLayout(new GridLayout(2, 2));
        nomePanel.add(new JLabel("Nome"));
        nomeInput = new JTextField("");
        nomeInput.setColumns(30);
        nomePanel.add(nomeInput);
        nomeErrorMsg = new JLabel("Nome invalido");
        nomeErrorMsg.setForeground(Color.RED);
        nomePanel.add(nomeErrorMsg);
        inputs.add(nomePanel);

        //endregion CampoNome

        //region CampoEmail

        emailPanel = new JPanel();

        emailPanel.setLayout(new GridLayout(2, 2));
        emailPanel.add(new JLabel("Email"));
        emailInput = new JTextField("");
        emailInput.setColumns(30);
        emailPanel.add(emailInput);
        emailErrorMsg = new JLabel("Email invalido");
        emailErrorMsg.setForeground(Color.RED);
        emailPanel.add(emailErrorMsg);
        inputs.add(emailPanel);

        //endregion CampoEmail

        //region CadastrarButon

        botaoCadastroPanel = new JPanel();

        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastroPanel.add(botaoCadastrar);
        inputs.add(botaoCadastroPanel);

        //endregion Cadastrarbuton

        botaoCadastrar.setActionCommand("Alugar");
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Alugar")) {
                    if(conferirDados()){
                        Cliente cliente =  new Cliente(cpfInput.getText(),nomeInput.getText(),emailInput.getText(),500);
                        if(!GerenciadorDeDados.checarClienteExistente(cliente)) {
                            GerenciadorDeDados.clientes.add(cliente);
                            GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
                            frame.dispose();
                            frame.setVisible(false);
                        }else{JOptionPane.showMessageDialog(painelCadastro, "Cliente com o Cpf "+ cpfInput.getText() +" já existe");}
                    }
                }
            }
        });

        cpfErrorMsg.setVisible(false);
        emailErrorMsg.setVisible(false);
        nomeErrorMsg.setVisible(false);
        frame.add(painelCadastro);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }


    private boolean conferirDados(){
        Util util = new Util();
        boolean valido = false;
        boolean cpfValido =  !cpfInput.getText().contains(" ");
        cpfErrorMsg.setVisible(!cpfValido);
        boolean emailValido =  util.conferirEmail(emailInput.getText());
        emailErrorMsg.setVisible(!emailValido);
        boolean nomeValido =  nomeInput.getText().length() >=4;
        nomeErrorMsg.setVisible(!nomeValido);
        valido = cpfValido && emailValido && nomeValido;
        return valido;
    };
    private void setarVisivel(){
        painelCadastro.setVisible(true);
    }
    private void setarInvisivel(){
        painelCadastro.setVisible(false);
    }
}
