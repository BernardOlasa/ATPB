package org.example.ui;

import org.example.domain.Aluguel;
import org.example.data.GerenciadorDeDados;
import org.example.util.PaletaCores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setBackground(PaletaCores.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Histórico de Todos os Aluguéis", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(PaletaCores.TEXT_DARK);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        criarTabela();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(PaletaCores.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        refreshData();
    }

    private void criarTabela() {
        String[] colunas = {"ID Aluguel", "Cliente", "CPF", "Data", "Dias", "Bicicletas (IDs)", "Valor Total", "Devolvido"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setBackground(PaletaCores.BACKGROUND);
        table.setForeground(PaletaCores.TEXT_DARK);
        table.setGridColor(PaletaCores.BORDER_COLOR);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PaletaCores.SECONDARY_ACCENT);
        header.setForeground(PaletaCores.TEXT_LIGHT);
        header.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void carregarHistorico() {
        tableModel.setRowCount(0);
        List<Aluguel> alugueis = GerenciadorDeDados.alugueis;
        alugueis.sort(Comparator.comparing(Aluguel::getDataAluguel).reversed());

        for (Aluguel aluguel : alugueis) {
            if (aluguel.getCliente() == null) continue;

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