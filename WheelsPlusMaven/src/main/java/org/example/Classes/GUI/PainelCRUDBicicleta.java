package org.example.Classes.GUI;

import org.example.Classes.Bicicleta;
import org.example.Classes.Dados.GerenciadorDeDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PainelCRUDBicicleta extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private final JFrame parentFrame;

    public PainelCRUDBicicleta(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        criarTabela();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos Botões
        btnAdicionar.addActionListener(e -> adicionarBicicleta());
        btnEditar.addActionListener(e -> editarBicicleta());
        btnExcluir.addActionListener(e -> excluirBicicleta());

        carregarBicicletas();
    }

    private void criarTabela() {
        String[] colunas = {"ID", "Marca", "Modelo", "Cor", "Aro", "Valor Diária", "Alugada", "Manutenção"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void carregarBicicletas() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Bicicleta> bicicletas = GerenciadorDeDados.bicicletas.stream()
                .filter(b -> !b.isExcluida()) // Filtra para não mostrar bicicletas excluídas
                .collect(Collectors.toList());
        for (Bicicleta b : bicicletas) {
            tableModel.addRow(new Object[]{
                    b.getIdBicicleta(),
                    b.getMarca(),
                    b.getModelo(),
                    b.getCor(),
                    b.getAroDaRoda(),
                    String.format("R$ %.2f", b.getValorAluguel()),
                    b.isAlugada() ? "Sim" : "Não",
                    b.isEmManutencao() ? "Sim" : "Não"
            });
        }
    }

    private void adicionarBicicleta() {
        DialogBicicleta dialog = new DialogBicicleta(parentFrame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            Bicicleta novaBicicleta = dialog.getBicicleta();
            // O ID é gerado com base no maior ID existente para garantir que seja único.
            int novoId = GerenciadorDeDados.bicicletas.stream().mapToInt(Bicicleta::getIdBicicleta).max().orElse(0) + 1;
            novaBicicleta.setIdBicicleta(novoId);

            GerenciadorDeDados.bicicletas.add(novaBicicleta);
            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
            carregarBicicletas();
        }
    }

    private void editarBicicleta() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma bicicleta para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Bicicleta bicicletaParaEditar = GerenciadorDeDados.bicicletas.stream()
                .filter(b -> b.getIdBicicleta() == id)
                .findFirst()
                .orElse(null);

        if (bicicletaParaEditar != null) {
            DialogBicicleta dialog = new DialogBicicleta(parentFrame, bicicletaParaEditar);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                int index = GerenciadorDeDados.bicicletas.indexOf(bicicletaParaEditar);
                GerenciadorDeDados.bicicletas.set(index, dialog.getBicicleta());
                GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
                carregarBicicletas();
            }
        }
    }

    private void excluirBicicleta() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma bicicleta para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a bicicleta selecionada?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            // Lógica de soft-delete
            GerenciadorDeDados.bicicletas.stream()
                    .filter(b -> b.getIdBicicleta() == id)
                    .findFirst()
                    .ifPresent(bicicleta -> {
                        bicicleta.setExcluida(true);
                        GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
                        carregarBicicletas();
                        JOptionPane.showMessageDialog(this, "Bicicleta excluída com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    });
        }
    }

    public void refreshData() {
        carregarBicicletas();
    }
}