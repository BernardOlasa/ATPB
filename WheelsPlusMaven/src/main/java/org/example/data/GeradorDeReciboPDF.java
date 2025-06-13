package org.example.data;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.domain.Aluguel;
import org.example.domain.Bicicleta;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class GeradorDeReciboPDF {

    public static String gerar(Aluguel aluguel) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");

            escreverTexto(contentStream, "Recibo de Aluguel - Wheels+", 50, 780, PDType1Font.HELVETICA_BOLD, 18);
            escreverTexto(contentStream, "----------------------------------------------------------------------------------------------------", 50, 760, PDType1Font.COURIER, 12);
            escreverTexto(contentStream, "ID do Aluguel: " + aluguel.getIdAluguel(), 50, 740, PDType1Font.HELVETICA_BOLD, 12);
            escreverTexto(contentStream, "Data: " + dateFormat.format(aluguel.getDataAluguel()), 350, 740, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, "Cliente: " + aluguel.getCliente().getNome(), 50, 710, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, "CPF: " + aluguel.getCliente().getCpf(), 50, 695, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, "Email: " + aluguel.getCliente().getEmail(), 50, 680, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, "Itens Alugados:", 50, 640, PDType1Font.HELVETICA_BOLD, 14);
            int yPosition = 620;
            for (Bicicleta bike : aluguel.getBicicletas()) {
                String descricaoBike = String.format("ID %d: %s %s (Aro %d) - Diária: R$ %.2f",
                        bike.getIdBicicleta(), bike.getMarca(), bike.getModelo(), bike.getAroDaRoda(), bike.getValorAluguel());
                escreverTexto(contentStream, descricaoBike, 60, yPosition, PDType1Font.HELVETICA, 11);
                yPosition -= 20;
            }
            yPosition -= 20;
            escreverTexto(contentStream, "----------------------------------------------------------------------------------------------------", 50, yPosition, PDType1Font.COURIER, 12);
            yPosition -= 20;
            escreverTexto(contentStream, "Período: " + aluguel.getDiasAlugados() + " dias", 400, yPosition, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, String.format("Valor do Aluguel: R$ %.2f", aluguel.getValorAluguel()), 400, yPosition - 20, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, String.format("Valor do Seguro: R$ %.2f", aluguel.getValorSeguro()), 400, yPosition - 40, PDType1Font.HELVETICA, 12);
            escreverTexto(contentStream, String.format("Valor TOTAL: R$ %.2f", aluguel.getValorTotal()), 400, yPosition - 60, PDType1Font.HELVETICA_BOLD, 14);

            contentStream.close();

            File recibosDir = new File("recibos");
            if (!recibosDir.exists()) {
                recibosDir.mkdirs();
            }
            String nomeArquivo = "recibos/recibo-aluguel-" + aluguel.getIdAluguel() + ".pdf";
            document.save(nomeArquivo);
            return new File(nomeArquivo).getAbsolutePath();
        }
    }

    public static void enviarPorEmail(Aluguel aluguel, String caminhoPdf) throws MessagingException {
        String remetente = "bernardo.pignatari@al.infnet.edu.br";
        String senhaApp = "ruef enec ejeg ixmb";
        String destinatario = aluguel.getCliente().getEmail();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        props.put("mail.smtp.connectiontimeout", "10000"); // Timeout para conectar (10 segundos)
        props.put("mail.smtp.timeout", "10000");           // Timeout para ler dados (10 segundos)
        props.put("mail.smtp.writetimeout", "10000");      // Timeout para escrever dados (10 segundos)

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senhaApp);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remetente));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Seu Recibo de Aluguel - Wheels+");

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText("Olá, " + aluguel.getCliente().getNome() + "!\n\nObrigado por escolher a Wheels+.\nSegue em anexo o recibo do seu aluguel.");

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        FileDataSource source = new FileDataSource(caminhoPdf);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(new File(caminhoPdf).getName());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textBodyPart);
        multipart.addBodyPart(attachmentBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    private static void escreverTexto(PDPageContentStream stream, String texto, float x, float y, PDType1Font fonte, int tamanhoFonte) throws IOException {
        stream.beginText();
        stream.setFont(fonte, tamanhoFonte);
        stream.newLineAtOffset(x, y);
        stream.showText(texto);
        stream.endText();
    }
}