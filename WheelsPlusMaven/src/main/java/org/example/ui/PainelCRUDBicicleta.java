package org.example.ui;

import org.example.domain.Bicicleta;
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

public class PainelCRUDBicicleta extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private final JFrame parentFrame;
    private JTextField campoBusca; // Campo de busca

    public PainelCRUDBicicleta(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(PaletaCores.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelSuperior = new JPanel(new BorderLayout(10,10));
        painelSuperior.setOpaque(false);

        JLabel titulo = new JLabel("Gerenciamento de Bicicletas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(PaletaCores.TEXT_DARK);
        painelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setOpaque(false);
        painelBusca.add(new JLabel("Buscar por ID, Marca ou Modelo:"));
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

        btnAdicionar.addActionListener(e -> adicionarBicicleta());
        btnEditar.addActionListener(e -> editarBicicleta());
        btnExcluir.addActionListener(e -> excluirBicicleta());

        campoBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarBicicletas(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarBicicletas(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarBicicletas(); }
        });

        carregarTodasBicicletas();
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
        String[] colunas = {"ID", "Marca", "Modelo", "Cor", "Aro", "Valor Diária", "Especialista", "Maior Promoção"};
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

    private void popularTabela(List<Bicicleta> bicicletas) {
        tableModel.setRowCount(0);
        for (Bicicleta b : bicicletas) {
            if (b.isExcluida()) continue; // Garante que bicicletas excluídas não apareçam

            tableModel.addRow(new Object[]{
                    b.getIdBicicleta(),
                    b.getMarca(),
                    b.getModelo(),
                    b.getCor(),
                    b.getAroDaRoda(),
                    String.format("R$ %.2f", b.getValorAluguel()),
                    b.isSpecialist() ? "Sim" : "Não",
                    String.format("%.1f%%", b.pegarMaiorPromocao())
            });
        }
    }

    private void carregarTodasBicicletas() {
        List<Bicicleta> bicicletasAtivas = GerenciadorDeDados.bicicletas.stream()
                .filter(b -> !b.isExcluida()).collect(Collectors.toList());
        popularTabela(bicicletasAtivas);
    }

    private void filtrarBicicletas() {
        String textoBusca = campoBusca.getText().toLowerCase().trim();
        if (textoBusca.isEmpty()) {
            carregarTodasBicicletas();
        } else {
            List<Bicicleta> bicicletasFiltradas = GerenciadorDeDados.bicicletas.stream()
                    .filter(b -> !b.isExcluida())
                    .filter(b -> String.valueOf(b.getIdBicicleta()).contains(textoBusca) ||
                            b.getMarca().toLowerCase().contains(textoBusca) ||
                            b.getModelo().toLowerCase().contains(textoBusca))
                    .collect(Collectors.toList());
            popularTabela(bicicletasFiltradas);
        }
    }

    private void adicionarBicicleta() {
        DialogBicicleta dialog = new DialogBicicleta(parentFrame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            Bicicleta novaBicicleta = dialog.getBicicleta();
            int novoId = GerenciadorDeDados.bicicletas.stream().mapToInt(Bicicleta::getIdBicicleta).max().orElse(0) + 1;
            novaBicicleta.setIdBicicleta(novoId);

            GerenciadorDeDados.bicicletas.add(novaBicicleta);
            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
            filtrarBicicletas();
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
                filtrarBicicletas();
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

            GerenciadorDeDados.bicicletas.stream()
                    .filter(b -> b.getIdBicicleta() == id)
                    .findFirst()
                    .ifPresent(bicicleta -> {
                        bicicleta.setExcluida(true);
                        GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);
                        filtrarBicicletas();
                        JOptionPane.showMessageDialog(this, "Bicicleta excluída com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    });
        }
    }

    public void refreshData() {
        filtrarBicicletas();
    }
}