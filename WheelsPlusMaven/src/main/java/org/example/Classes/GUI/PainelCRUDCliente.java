package org.example.Classes.GUI;

import org.example.Classes.Cliente;
import org.example.Classes.Dados.GerenciadorDeDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelCRUDCliente extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private final JFrame parentFrame;

    public PainelCRUDCliente(JFrame parentFrame) {
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
        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());

        carregarClientes();
    }

    private void criarTabela() {
        String[] colunas = {"CPF", "Nome", "Email", "Score"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void carregarClientes() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Cliente> clientes = GerenciadorDeDados.clientes;
        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{
                    cliente.getCpf(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    String.format("%.2f", cliente.getScore())
            });
        }
    }

    private void adicionarCliente() {
        DialogCliente dialog = new DialogCliente(parentFrame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            GerenciadorDeDados.clientes.add(dialog.getCliente());
            GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
            carregarClientes();
        }
    }

    private void editarCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpf = (String) tableModel.getValueAt(selectedRow, 0);
        Cliente clienteParaEditar = GerenciadorDeDados.clientes.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);

        if (clienteParaEditar != null) {
            DialogCliente dialog = new DialogCliente(parentFrame, clienteParaEditar);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                // Atualiza o cliente na lista
                int index = GerenciadorDeDados.clientes.indexOf(clienteParaEditar);
                GerenciadorDeDados.clientes.set(index, dialog.getCliente());
                GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
                carregarClientes();
            }
        }
    }

    private void excluirCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String cpf = (String) tableModel.getValueAt(selectedRow, 0);
            GerenciadorDeDados.clientes.removeIf(c -> c.getCpf().equals(cpf));
            GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
            carregarClientes();
        }
    }

    public void refreshData() {
        carregarClientes();
    }
}