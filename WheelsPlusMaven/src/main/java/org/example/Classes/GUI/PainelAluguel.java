package org.example.Classes.GUI;

import jakarta.mail.MessagingException;
import org.example.Classes.Aluguel;
import org.example.Classes.Bicicleta;
import org.example.Classes.Cliente;
import org.example.Classes.Dados.GerenciadorDeDados;
import org.example.Classes.Dados.GeradorDeReciboPDF;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class PainelAluguel extends JPanel {

    // Helper interno para centralizar a lógica de cálculo
    protected class CalculoHelper {
        public double calcularSeguroProporcional(Bicicleta bike, Cliente cliente) {
            if (cliente == null) return 0.0;
            double score = cliente.getScore();
            double multiplicador;

            if (score >= 1000) {
                multiplicador = 0.0; // Score 1000 ou mais: 0% do seguro
            } else if (score >= 500) {
                // Interpolação linear entre score 500 (100%) e 1000 (0%)
                multiplicador = 1.0 - ((score - 500.0) / 500.0);
            } else if (score > 0) {
                // Interpolação linear entre score 0 (300%) e 500 (100%)
                multiplicador = 3.0 - ((score / 500.0) * 2.0);
            } else {
                multiplicador = 3.0; // Score 0 ou menos: 300% do seguro
            }

            return bike.getValorSeguro() * multiplicador;
        }
    }

    private final JComboBox<String> diasInput;
    private final JLabel diasErrorMsg;
    private final JComboBox<String> comboBoxClientes;
    private final JPanel innerPanelDisponiveis;
    private final JPanel innerPanelSelecionadas;

    private final ArrayList<Bicicleta> bicicletasDisponiveis = new ArrayList<>();
    private final ArrayList<Bicicleta> bicicletasSelecionadas = new ArrayList<>();
    private final CalculoHelper calculoHelper = new CalculoHelper();

    public PainelAluguel() {
        super(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- INÍCIO DA RESTAURAÇÃO DO PAINEL SUPERIOR ---
        JPanel painelDadosAluguel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Dados de Aluguel", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelDadosAluguel.add(titulo, BorderLayout.NORTH);

        JPanel painelInputs = new JPanel(new GridLayout(1, 3, 20, 0));
        painelInputs.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Dias
        JPanel diasPanel = new JPanel(new GridLayout(3, 1));
        diasPanel.add(new JLabel("Dias:"));
        String[] diasOptions = new String[31];
        diasOptions[0] = "Selecione...";
        for (int i = 1; i <= 30; i++) diasOptions[i] = String.valueOf(i);
        diasInput = new JComboBox<>(diasOptions);
        diasPanel.add(diasInput);
        diasErrorMsg = new JLabel("Campo obrigatório");
        diasErrorMsg.setForeground(Color.RED);
        diasErrorMsg.setVisible(false);
        diasPanel.add(diasErrorMsg);
        painelInputs.add(diasPanel);

        // Clientes
        JPanel painelSelecionarClientes = new JPanel(new GridLayout(2, 1));
        painelSelecionarClientes.add(new JLabel("Selecione um cliente:"));
        comboBoxClientes = new JComboBox<>();
        atualizarClientes();
        painelSelecionarClientes.add(comboBoxClientes);
        painelInputs.add(painelSelecionarClientes);

        // Botão Adicionar Cliente
        JButton adicionarClienteBtn = new JButton("Adicionar Cliente");
        adicionarClienteBtn.addActionListener(e -> new PainelCadastro());
        JPanel painelBotaoAddCliente = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        painelBotaoAddCliente.add(adicionarClienteBtn);
        painelInputs.add(painelBotaoAddCliente);

        painelDadosAluguel.add(painelInputs, BorderLayout.CENTER);
        add(painelDadosAluguel, BorderLayout.NORTH);
        // --- FIM DA RESTAURAÇÃO DO PAINEL SUPERIOR ---

        // --- PAINEL CENTRAL ---
        JPanel painelBicicletas = new JPanel(new GridLayout(1, 2, 10, 10));
        painelBicicletas.setBorder(BorderFactory.createTitledBorder("Bicicletas Disponíveis / Selecionadas (Clique duplo para mover)"));
        innerPanelDisponiveis = new JPanel();
        innerPanelDisponiveis.setLayout(new BoxLayout(innerPanelDisponiveis, BoxLayout.Y_AXIS));
        painelBicicletas.add(new JScrollPane(innerPanelDisponiveis));
        innerPanelSelecionadas = new JPanel();
        innerPanelSelecionadas.setLayout(new BoxLayout(innerPanelSelecionadas, BoxLayout.Y_AXIS));
        painelBicicletas.add(new JScrollPane(innerPanelSelecionadas));
        add(painelBicicletas, BorderLayout.CENTER);

        // --- PAINEL INFERIOR ---
        JPanel painelFinal = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton efetuarAluguelBtn = new JButton("Efetuar Aluguel");
        efetuarAluguelBtn.addActionListener(e -> efetuarAluguel());
        painelFinal.add(efetuarAluguelBtn);
        add(painelFinal, BorderLayout.SOUTH);

        // --- LISTENERS PARA ATUALIZAÇÃO DINÂMICA ---
        diasInput.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateAllCardValues();
            }
        });
        comboBoxClientes.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateAllCardValues();
            }
        });

        // CARGA INICIAL DOS DADOS
        refreshData();
    }

    public void refreshData() {
        this.atualizarBicicletasDisponiveis();
        this.atualizarVisualizacaoBicicletas();
    }

    private void updateAllCardValues() {
        int dias = 0;
        if (diasInput.getSelectedIndex() > 0) {
            dias = Integer.parseInt(diasInput.getSelectedItem().toString());
        }

        Cliente cliente = null;
        if (comboBoxClientes.getSelectedIndex() >= 0 && comboBoxClientes.getSelectedItem() != null) {
            String clienteCpf = comboBoxClientes.getSelectedItem().toString().split(" : ")[1];
            cliente = GerenciadorDeDados.clientes.stream().filter(c -> c.getCpf().equals(clienteCpf)).findFirst().orElse(null);
        }

        for (Component comp : innerPanelDisponiveis.getComponents()) {
            if (comp instanceof BicicletaCardDinamico) {
                ((BicicletaCardDinamico) comp).updateValores(dias, cliente, calculoHelper);
            }
        }
        for (Component comp : innerPanelSelecionadas.getComponents()) {
            if (comp instanceof BicicletaCardDinamico) {
                ((BicicletaCardDinamico) comp).updateValores(dias, cliente, calculoHelper);
            }
        }
    }

    private void moverBicicleta(BicicletaCardDinamico card) {
        Bicicleta bike = card.getBicicleta();
        if (bicicletasDisponiveis.contains(bike)) {
            bicicletasDisponiveis.remove(bike);
            bicicletasSelecionadas.add(bike);
        } else {
            bicicletasSelecionadas.remove(bike);
            bicicletasDisponiveis.add(bike);
        }
        atualizarVisualizacaoBicicletas();
    }

    private void atualizarVisualizacaoBicicletas() {
        innerPanelDisponiveis.removeAll();
        bicicletasDisponiveis.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasDisponiveis) {
            BicicletaCardDinamico card = new BicicletaCardDinamico(bike);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) moverBicicleta(card);
                }
            });
            innerPanelDisponiveis.add(card);
        }

        innerPanelSelecionadas.removeAll();
        bicicletasSelecionadas.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasSelecionadas) {
            BicicletaCardDinamico card = new BicicletaCardDinamico(bike);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) moverBicicleta(card);
                }
            });
            innerPanelSelecionadas.add(card);
        }

        updateAllCardValues();
        revalidate();
        repaint();
    }

    private void atualizarClientes() {
        Object selectedItem = comboBoxClientes.getSelectedItem();
        comboBoxClientes.removeAllItems();
        for (Cliente c : GerenciadorDeDados.clientes) {
            comboBoxClientes.addItem(c.getNome() + " : " + c.getCpf());
        }
        if (selectedItem != null) {
            comboBoxClientes.setSelectedItem(selectedItem);
        }
    }

    private void atualizarBicicletasDisponiveis() {
        bicicletasDisponiveis.clear();
        bicicletasDisponiveis.addAll(GerenciadorDeDados.bicicletas.stream().filter(b -> !b.isAlugada() && !b.isEmManutencao() && !b.isExcluida()) // Filtro adicionado
                .collect(Collectors.toList()));
        bicicletasSelecionadas.clear();
    }


    private void efetuarAluguel() {
        if (diasInput.getSelectedIndex() <= 0) {
            diasErrorMsg.setVisible(true);
            return;
        }
        diasErrorMsg.setVisible(false);

        if (comboBoxClientes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (bicicletasSelecionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos uma bicicleta!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String clienteCpf = comboBoxClientes.getSelectedItem().toString().split(" : ")[1];
            Cliente cliente = GerenciadorDeDados.clientes.stream()
                    .filter(c -> c.getCpf().equals(clienteCpf))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            int dias = Integer.parseInt(diasInput.getSelectedItem().toString());
            int novoId = GerenciadorDeDados.alugueis.stream()
                    .mapToInt(Aluguel::getIdAluguel)
                    .max().orElse(0) + 1;

            Aluguel novoAluguel = new Aluguel(novoId, dias, cliente, new ArrayList<>(bicicletasSelecionadas), new Date());
            GerenciadorDeDados.alugueis.add(novoAluguel);

            bicicletasSelecionadas.forEach(b -> b.setAlugada(true));

            GerenciadorDeDados.salvarDados("Alugueis", GerenciadorDeDados.alugueis);
            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);

            // --- LÓGICA DE GERAÇÃO E ENVIO DO RECIBO ---
            try {
                String caminhoDoRecibo = GeradorDeReciboPDF.gerar(novoAluguel);

                try {
                    GeradorDeReciboPDF.enviarPorEmail(novoAluguel, caminhoDoRecibo);
                    JOptionPane.showMessageDialog(this,
                            "Aluguel realizado com sucesso!\nRecibo salvo e enviado para o email do cliente.",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (MessagingException emailEx) {
                    // Se o email falhar, informa que o PDF foi salvo localmente
                    JOptionPane.showMessageDialog(this,
                            "Aluguel realizado e recibo salvo, mas houve um erro ao enviar o email.\nCaminho do recibo: " + caminhoDoRecibo,
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    emailEx.printStackTrace();
                }

            } catch (IOException pdfEx) {
                // Se a geração do PDF falhar
                JOptionPane.showMessageDialog(this,
                        "Aluguel realizado, mas houve um erro ao gerar o recibo em PDF.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                pdfEx.printStackTrace();
            }
            // --- FIM DA LÓGICA ---

            refreshData();
            diasInput.setSelectedIndex(0);
            comboBoxClientes.setSelectedIndex(-1);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao efetuar aluguel: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
