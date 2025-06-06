package org.example.Classes;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;

public final class Cliente {
    protected String cpf,nome,email;
    protected double score;


    protected List<Aluguel> alugueis;

    public void enviarEmail(String mensagem) {
        // A completar                                                TO DO
    }


    @Override
    public String toString() {
        return  String.format("%s,%s,%s,%.2f",cpf,nome,email,score);
    }
    //--------------------------------------{Construtor}--------------------------------------
    public Cliente(String cpf, String nome, String email, double score) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.score = score;
    }

    //----------------------------------------{GET SET}---------------------------------------

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public List<Aluguel> getAlugueis() {
        return alugueis;
    }
    public void setAlugueis(List<Aluguel> aluguels) {
        this.alugueis = aluguels;
    }

    public void adicionarAluguel(Aluguel aluguel){
        alugueis.add(aluguel);
    }

    public void enviarEmail(String remetente, String senhaApp) {

        String destinatario = email;
        String corpo = String.format("Caro %s, segue esse e-mail para lembrar ao senhor que seu prazo de aluguel já acabou," +
                " em caso de não devolução do item alugado, reteremos o valor do seguro e será descontado a diaria encima do mesmo \n Atenciosamente: Equipe Wheels+ ", nome);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senhaApp);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Aviso de termino de tempo de aluguel");
            message.setText(corpo);
            Transport.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}










