package org.example.ui;

import org.example.domain.Cliente;
import org.example.data.GerenciadorDeDados;
import org.example.util.PaletaCores;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PainelCRUDCliente extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private final JFrame parentFrame;
    private JTextField campoBusca; // Campo de busca

    public PainelCRUDCliente(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(PaletaCores.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelSuperior = new JPanel(new BorderLayout(10,10));
        painelSuperior.setOpaque(false);

        JLabel titulo = new JLabel("Gerenciamento de Clientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(PaletaCores.TEXT_DARK);
        painelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setOpaque(false);
        painelBusca.add(new JLabel("Buscar por Nome ou CPF:"));
        campoBusca = new JTextField(25);
        painelBusca.add(campoBusca);
        painelSuperior.add(painelBusca, BorderLayout.CENTER);

        add(painelSuperior, BorderLayout.NORTH);

        criarTabela();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(PaletaCores.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(PaletaCores.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setOpaque(false);
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        styleButton(btnAdicionar, PaletaCores.PRIMARY_ACCENT);
        styleButton(btnEditar, PaletaCores.SECONDARY_ACCENT);
        styleButton(btnExcluir, PaletaCores.DANGER);

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());

        campoBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarClientes(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarClientes(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarClientes(); }
        });

        carregarTodosClientes();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(PaletaCores.TEXT_LIGHT);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void criarTabela() {
        String[] colunas = {"CPF", "Nome", "Email", "Score"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(PaletaCores.PANEL_BACKGROUND);
        table.setForeground(PaletaCores.TEXT_DARK);
        table.setGridColor(PaletaCores.BORDER_COLOR);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(PaletaCores.PRIMARY_ACCENT);
        table.setSelectionForeground(PaletaCores.TEXT_LIGHT);

        JTableHeader header = table.getTableHeader();
        header.setBackground(PaletaCores.SECONDARY_ACCENT);
        header.setForeground(PaletaCores.TEXT_LIGHT);
        header.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void popularTabela(List<Cliente> clientes) {
        tableModel.setRowCount(0);
        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{
                    cliente.getCpf(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    String.format("%.2f", cliente.getScore())
            });
        }
    }

    private void carregarTodosClientes() {
        popularTabela(GerenciadorDeDados.clientes);
    }

    private void filtrarClientes() {
        String textoBusca = campoBusca.getText().toLowerCase().trim();
        if (textoBusca.isEmpty()) {
            carregarTodosClientes();
        } else {
            List<Cliente> clientesFiltrados = GerenciadorDeDados.clientes.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(textoBusca) ||
                            c.getCpf().contains(textoBusca))
                    .collect(Collectors.toList());
            popularTabela(clientesFiltrados);
        }
    }

    private void adicionarCliente() {
        DialogCliente dialog = new DialogCliente(parentFrame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            GerenciadorDeDados.clientes.add(dialog.getCliente());
            GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
            filtrarClientes();
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
                int index = GerenciadorDeDados.clientes.indexOf(clienteParaEditar);
                GerenciadorDeDados.clientes.set(index, dialog.getCliente());
                GerenciadorDeDados.salvarDados("Clientes", GerenciadorDeDados.clientes);
                filtrarClientes();
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
            filtrarClientes();
        }
    }

    public void refreshData() {
        filtrarClientes();
    }
}