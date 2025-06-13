package org.example.Classes.GUI;

import org.example.Classes.Aluguel;
import org.example.Classes.Dados.GerenciadorDeDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PainelHistoricoAlugueis extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PainelHistoricoAlugueis() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Histórico de Todos os Aluguéis", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        criarTabela();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        refreshData(); // Carga inicial dos dados
    }

    private void criarTabela() {
        String[] colunas = {"ID Aluguel", "Cliente", "CPF", "Data", "Dias", "Bicicletas (IDs)", "Valor Total", "Devolvido"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna a tabela não editável
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void carregarHistorico() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Aluguel> alugueis = GerenciadorDeDados.alugueis;
        // Ordena para mostrar os aluguéis mais recentes primeiro
        alugueis.sort(Comparator.comparing(Aluguel::getDataAluguel).reversed());

        for (Aluguel aluguel : alugueis) {
            if (aluguel.getCliente() == null) continue; // Pula aluguéis sem cliente (dados corrompidos)

            String idsBicicletas = aluguel.getBicicletas().stream()
                    .map(bicicleta -> String.valueOf(bicicleta.getIdBicicleta()))
                    .collect(Collectors.joining(", "));

            tableModel.addRow(new Object[]{
                    aluguel.getIdAluguel(),
                    aluguel.getCliente().getNome(),
                    aluguel.getCliente().getCpf(),
                    dateFormat.format(aluguel.getDataAluguel()),
                    aluguel.getDiasAlugados(),
                    idsBicicletas,
                    String.format("R$ %.2f", aluguel.getValorTotal()),
                    aluguel.isBicicletasDevolvidas() ? "Sim" : "Não"
            });
        }
    }

    public void refreshData() {
        carregarHistorico();
    }
}