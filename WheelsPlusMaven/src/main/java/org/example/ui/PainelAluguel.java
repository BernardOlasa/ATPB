package org.example.ui;

import jakarta.mail.MessagingException;
import org.example.data.GerenciadorDeDados;
import org.example.data.GeradorDeReciboPDF;
import org.example.domain.Aluguel;
import org.example.domain.Bicicleta;
import org.example.domain.Cliente;
import org.example.util.PaletaCores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PainelAluguel extends JPanel {

    protected class CalculoHelper {
        public double calcularSeguroProporcional(Bicicleta bike, Cliente cliente) {
            if (cliente == null) return 0.0;
            double score = cliente.getScore();
            double multiplicador;

            if (score >= 1000) multiplicador = 0.0;
            else if (score >= 500) multiplicador = 1.0 - ((score - 500.0) / 500.0);
            else if (score > 0) multiplicador = 3.0 - ((score / 500.0) * 2.0);
            else multiplicador = 3.0;

            return bike.getValorSeguro() * multiplicador;
        }
    }

    private final JComboBox<String> diasInput;
    private final JLabel diasErrorMsg;
    private final JComboBox<String> comboBoxClientes;
    private final CalculoHelper calculoHelper = new CalculoHelper();
    private final PainelSeletorDeBicicletas painelSeletor;

    public PainelAluguel() {
        super(new BorderLayout(20, 20));
        setBackground(PaletaCores.BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel painelDadosAluguel = new JPanel(new BorderLayout());
        painelDadosAluguel.setOpaque(false);
        JLabel titulo = new JLabel("Dados de Aluguel", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(PaletaCores.TEXT_DARK);
        titulo.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        painelDadosAluguel.add(titulo, BorderLayout.NORTH);

        JPanel painelInputs = new JPanel(new GridLayout(1, 3, 20, 0));
        painelInputs.setOpaque(false);
        painelInputs.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel diasPanel = new JPanel(new BorderLayout(5,5));
        diasPanel.setOpaque(false);
        diasPanel.add(createStyledLabel("Dias:"), BorderLayout.NORTH);
        String[] diasOptions = new String[31];
        diasOptions[0] = "Selecione...";
        for (int i = 1; i <= 30; i++) diasOptions[i] = String.valueOf(i);
        diasInput = new JComboBox<>(diasOptions);
        diasPanel.add(diasInput, BorderLayout.CENTER);
        diasErrorMsg = new JLabel("Campo obrigatório");
        diasErrorMsg.setForeground(PaletaCores.DANGER);
        diasErrorMsg.setVisible(false);
        diasPanel.add(diasErrorMsg, BorderLayout.SOUTH);
        painelInputs.add(diasPanel);

        JPanel painelSelecionarClientes = new JPanel(new BorderLayout(5,5));
        painelSelecionarClientes.setOpaque(false);
        painelSelecionarClientes.add(createStyledLabel("Selecione um cliente:"), BorderLayout.NORTH);
        comboBoxClientes = new JComboBox<>();
        atualizarClientes();
        painelSelecionarClientes.add(comboBoxClientes, BorderLayout.CENTER);
        painelInputs.add(painelSelecionarClientes);

        JButton adicionarClienteBtn = new JButton("Adicionar Cliente");
        styleButton(adicionarClienteBtn);
        adicionarClienteBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogCliente dialog = new DialogCliente(parentFrame, null);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                Cliente novoCliente = dialog.getCliente();
                if (!GerenciadorDeDados.checarClienteExistente(novoCliente)) {
                    GerenciadorDeDados.clientes.add(novoCliente);
                    GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
                    atualizarClientes();
                    comboBoxClientes.setSelectedItem(novoCliente.getNome() + " : " + novoCliente.getCpf());
                } else {
                    JOptionPane.showMessageDialog(this, "Um cliente com este CPF já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel painelBotaoAddCliente = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        painelBotaoAddCliente.setOpaque(false);
        painelBotaoAddCliente.add(adicionarClienteBtn);
        painelInputs.add(painelBotaoAddCliente);

        painelDadosAluguel.add(painelInputs, BorderLayout.CENTER);
        add(painelDadosAluguel, BorderLayout.NORTH);

        CardFactory fabricaDeCardsDinamicos = BicicletaCardDinamico::new;
        painelSeletor = new PainelSeletorDeBicicletas(
                "Bicicletas Disponíveis / Selecionadas (Clique duplo para mover)",
                fabricaDeCardsDinamicos
        );
        add(painelSeletor, BorderLayout.CENTER);

        JPanel painelFinal = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        painelFinal.setOpaque(false);
        JButton efetuarAluguelBtn = new JButton("Efetuar Aluguel");
        styleButton(efetuarAluguelBtn);
        efetuarAluguelBtn.setFont(new Font("Arial", Font.BOLD, 18));
        efetuarAluguelBtn.addActionListener(e -> efetuarAluguel());
        painelFinal.add(efetuarAluguelBtn);
        add(painelFinal, BorderLayout.SOUTH);

        diasInput.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) updateAllCardValues();
        });
        comboBoxClientes.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) updateAllCardValues();
        });

        refreshData();
    }

    private void styleButton(JButton button) {
        button.setBackground(PaletaCores.PRIMARY_ACCENT);
        button.setForeground(PaletaCores.TEXT_LIGHT);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(PaletaCores.TEXT_DARK);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    public void refreshData() {
        atualizarBicicletasDisponiveis();
    }

    private void atualizarBicicletasDisponiveis() {
        List<Bicicleta> disponiveis = GerenciadorDeDados.bicicletas.stream()
                .filter(b -> !b.isAlugada() && !b.isEmManutencao() && !b.isExcluida())
                .collect(Collectors.toList());
        painelSeletor.setBicicletasDisponiveis(disponiveis);
        updateAllCardValues();
    }

    private void updateAllCardValues() {
        int dias = diasInput.getSelectedIndex() > 0 ? Integer.parseInt(diasInput.getSelectedItem().toString()) : 0;
        Cliente cliente = getSelectedCliente();

        for (Component comp : painelSeletor.getCardsDisponiveis()) {
            if (comp instanceof BicicletaCardDinamico) {
                ((BicicletaCardDinamico) comp).updateValores(dias, cliente, calculoHelper);
            }
        }
        for (Component comp : painelSeletor.getCardsSelecionados()) {
            if (comp instanceof BicicletaCardDinamico) {
                ((BicicletaCardDinamico) comp).updateValores(dias, cliente, calculoHelper);
            }
        }
    }

    private Cliente getSelectedCliente() {
        if (comboBoxClientes.getSelectedIndex() >= 0 && comboBoxClientes.getSelectedItem() != null) {
            String selected = comboBoxClientes.getSelectedItem().toString();
            if (selected.contains(" : ")) {
                String clienteCpf = selected.split(" : ")[1];
                return GerenciadorDeDados.clientes.stream()
                        .filter(c -> c.getCpf().equals(clienteCpf))
                        .findFirst().orElse(null);
            }
        }
        return null;
    }

    private void atualizarClientes() {
        Object selectedItem = comboBoxClientes.getSelectedItem();
        comboBoxClientes.removeAllItems();
        for (Cliente c : GerenciadorDeDados.clientes) {
            comboBoxClientes.addItem(c.getNome() + " : " + c.getCpf());
        }
        if (selectedItem != null) {
            comboBoxClientes.setSelectedItem(selectedItem);
        } else {
            comboBoxClientes.setSelectedIndex(-1);
        }
    }

    private void efetuarAluguel() {
        if (diasInput.getSelectedIndex() <= 0) {
            diasErrorMsg.setVisible(true);
            return;
        }
        diasErrorMsg.setVisible(false);

        if (comboBoxClientes.getSelectedItem() == null || comboBoxClientes.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (painelSeletor.getBicicletasSelecionadas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos uma bicicleta!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        final Cliente cliente = getSelectedCliente();
        final int dias = Integer.parseInt(diasInput.getSelectedItem().toString());
        final ArrayList<Bicicleta> bicicletasParaAlugar = new ArrayList<>(painelSeletor.getBicicletasSelecionadas());

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                int novoId = GerenciadorDeDados.alugueis.stream()
                        .mapToInt(Aluguel::getIdAluguel).max().orElse(0) + 1;

                Aluguel novoAluguel = new Aluguel(novoId, dias, cliente, bicicletasParaAlugar, new Date());

                bicicletasParaAlugar.forEach(b -> b.setAlugada(true));

                GerenciadorDeDados.alugueis.add(novoAluguel);
                GerenciadorDeDados.salvarDados("Alugueis", GerenciadorDeDados.alugueis);
                GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);

                try {
                    String caminhoDoRecibo = GeradorDeReciboPDF.gerar(novoAluguel);
                    GeradorDeReciboPDF.enviarPorEmail(novoAluguel, caminhoDoRecibo);
                    return "Aluguel realizado com sucesso!\nRecibo salvo e enviado para o email do cliente.";
                } catch (MessagingException emailEx) {
                    emailEx.printStackTrace();
                    return "Aluguel realizado e recibo salvo, mas houve um erro ao enviar o email.";
                } catch (IOException pdfEx) {
                    pdfEx.printStackTrace();
                    return "Aluguel realizado, mas houve um erro ao gerar o recibo em PDF.";
                }
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();
                    JOptionPane.showMessageDialog(PainelAluguel.this, resultado, "Status do Aluguel", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PainelAluguel.this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    refreshData();
                    diasInput.setSelectedIndex(0);
                    comboBoxClientes.setSelectedIndex(-1);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }
}