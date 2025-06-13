package org.example.Classes.GUI;

import org.example.Classes.Aluguel;
import org.example.Classes.Bicicleta;
import org.example.Classes.Cliente;
import org.example.Classes.Dados.GerenciadorDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class PainelAluguel extends JPanel {

    private final JComboBox<String> diasInput;
    private final JLabel diasErrorMsg;
    private final JComboBox<String> comboBoxClientes;
    private final JPanel innerPanelDisponiveis;
    private final JPanel innerPanelSelecionadas;

    private final ArrayList<Bicicleta> bicicletasDisponiveis = new ArrayList<>();
    private final ArrayList<Bicicleta> bicicletasSelecionadas = new ArrayList<>();

    public PainelAluguel() {
        super(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- PAINEL SUPERIOR: DADOS DO ALUGUEL ---
        JPanel painelDadosAluguel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Dados de Aluguel", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelDadosAluguel.add(titulo, BorderLayout.NORTH);

        JPanel painelInputs = new JPanel(new GridLayout(1, 3, 20, 0));
        painelInputs.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Dias
        JPanel diasPanel = new JPanel(new GridLayout(3, 1)); // Alterado para 3 linhas para o erro
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
        adicionarClienteBtn.addActionListener(e -> {
            // A classe PainelCadastro já existe e foi fornecida na refatoração
            new PainelCadastro();
            // Para uma melhor experiência, um listener poderia ser implementado
            // para atualizar a combobox de clientes automaticamente após o cadastro.
        });
        JPanel painelBotaoAddCliente = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        painelBotaoAddCliente.add(adicionarClienteBtn);
        painelInputs.add(painelBotaoAddCliente);

        painelDadosAluguel.add(painelInputs, BorderLayout.CENTER);
        add(painelDadosAluguel, BorderLayout.NORTH);

        // --- PAINEL CENTRAL: BICICLETAS ---
        JPanel painelBicicletas = new JPanel(new GridLayout(1, 2, 10, 10));
        painelBicicletas.setBorder(BorderFactory.createTitledBorder("Bicicletas Disponíveis / Selecionadas (Clique duplo para mover)"));

        innerPanelDisponiveis = new JPanel();
        innerPanelDisponiveis.setLayout(new BoxLayout(innerPanelDisponiveis, BoxLayout.Y_AXIS));
        painelBicicletas.add(new JScrollPane(innerPanelDisponiveis));

        innerPanelSelecionadas = new JPanel();
        innerPanelSelecionadas.setLayout(new BoxLayout(innerPanelSelecionadas, BoxLayout.Y_AXIS));
        painelBicicletas.add(new JScrollPane(innerPanelSelecionadas));

        add(painelBicicletas, BorderLayout.CENTER);

        // --- INÍCIO DA CORREÇÃO ---
        // Carrega os dados das bicicletas do GerenciadorDeDados para as listas locais.
        atualizarBicicletasDisponiveis();
        // --- FIM DA CORREÇÃO ---

        // Agora, atualiza a visualização com as listas que acabaram de ser populadas.
        atualizarVisualizacaoBicicletas();

        // --- PAINEL INFERIOR: BOTÃO DE EFETUAR ---
        JPanel painelFinal = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton efetuarAluguelBtn = new JButton("Efetuar Aluguel");
        efetuarAluguelBtn.addActionListener(e -> efetuarAluguel());
        painelFinal.add(efetuarAluguelBtn);
        add(painelFinal, BorderLayout.SOUTH);
    }

    /**
     * Move uma bicicleta entre a lista de disponíveis e selecionadas.
     * @param bike A bicicleta a ser movida.
     * @param onDoubleClick Ação a ser executada no clique duplo.
     */
    private void moverBicicleta(Bicicleta bike, ActionListener onDoubleClick) {
        if (bicicletasDisponiveis.contains(bike)) {
            bicicletasDisponiveis.remove(bike);
            bicicletasSelecionadas.add(bike);
        } else {
            bicicletasSelecionadas.remove(bike);
            bicicletasDisponiveis.add(bike);
        }
        // Após mover os dados, a interface gráfica é reconstruída
        atualizarVisualizacaoBicicletas();
    }

    /**
     * Limpa e recria os cards de bicicleta nos painéis de visualização.
     */
    private void atualizarVisualizacaoBicicletas() {
        innerPanelDisponiveis.removeAll();
        bicicletasDisponiveis.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasDisponiveis) {
            // A ação de clique duplo agora chama o método moverBicicleta
            BicicletaCard card = new BicicletaCard(bike, e -> moverBicicleta(bike, null));
            innerPanelDisponiveis.add(card);
        }

        innerPanelSelecionadas.removeAll();
        bicicletasSelecionadas.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));
        for (Bicicleta bike : bicicletasSelecionadas) {
            BicicletaCard card = new BicicletaCard(bike, e -> moverBicicleta(bike, null));
            innerPanelSelecionadas.add(card);
        }

        // Revalida e redesenha os painéis para mostrar as mudanças
        revalidate();
        repaint();
    }

    /**
     * Recarrega a lista de clientes da fonte de dados.
     */
    private void atualizarClientes() {
        comboBoxClientes.removeAllItems();
        for (Cliente c : GerenciadorDeDados.clientes) { //
            comboBoxClientes.addItem(c.getNome() + " : " + c.getCpf()); //
        }
    }

    /**
     * Carrega a lista de bicicletas disponíveis a partir do GerenciadorDeDados.
     * Filtra as que já estão alugadas ou em manutenção.
     */
    private void atualizarBicicletasDisponiveis() {
        bicicletasDisponiveis.clear();
        bicicletasDisponiveis.addAll(
                GerenciadorDeDados.bicicletas.stream()
                        .filter(b -> !b.isAlugada() && !b.isEmManutencao()) //
                        .collect(Collectors.toList())
        );
        bicicletasSelecionadas.clear();
    }

    /**
     * Valida os dados e processa o aluguel.
     */
    private void efetuarAluguel() {
        // Validação de dias
        if (diasInput.getSelectedIndex() <= 0) {
            diasErrorMsg.setVisible(true);
            return;
        }
        diasErrorMsg.setVisible(false);

        // Validação de cliente
        if (comboBoxClientes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validação de bicicletas
        if (bicicletasSelecionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos uma bicicleta!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String clienteCpf = comboBoxClientes.getSelectedItem().toString().split(" : ")[1];
            Cliente cliente = GerenciadorDeDados.clientes.stream() //
                    .filter(c -> c.getCpf().equals(clienteCpf)) //
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            int dias = Integer.parseInt(diasInput.getSelectedItem().toString());
            int novoId = GerenciadorDeDados.alugueis.stream() //
                    .mapToInt(Aluguel::getIdAluguel) //
                    .max().orElse(0) + 1;

            // Cria um novo aluguel
            Aluguel novoAluguel = new Aluguel(novoId, dias, cliente, new ArrayList<>(bicicletasSelecionadas), new Date()); //
            GerenciadorDeDados.alugueis.add(novoAluguel); //

            // Marca as bicicletas como alugadas
            bicicletasSelecionadas.forEach(b -> b.setAlugada(true)); //

            // Salva os dados
            GerenciadorDeDados.salvarDados("Alugueis", GerenciadorDeDados.alugueis); //
            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas); //

            JOptionPane.showMessageDialog(this, "Aluguel realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Reseta o painel para o próximo aluguel
            atualizarBicicletasDisponiveis();
            atualizarVisualizacaoBicicletas();
            diasInput.setSelectedIndex(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao efetuar aluguel: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}