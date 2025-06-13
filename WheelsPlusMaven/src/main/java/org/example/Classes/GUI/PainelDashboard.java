package org.example.Classes.GUI;

import org.example.Classes.Aluguel;
import org.example.Classes.Bicicleta;
import org.example.Classes.Dados.GerenciadorDeDados;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PainelDashboard extends JPanel {

    private final DefaultCategoryDataset dataset;
    private final ChartPanel chartPanel;

    public PainelDashboard() {
        setLayout(new BorderLayout());

        // 1. Inicializa o dataset que alimentará o gráfico
        this.dataset = new DefaultCategoryDataset();

        // 2. Cria o gráfico usando o ChartFactory do JFreeChart
        JFreeChart chart = createChart(dataset);

        // 3. Coloca o gráfico em um ChartPanel, que é um componente Swing
        this.chartPanel = new ChartPanel(chart);
        add(this.chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        JFreeChart stackedBarChart = ChartFactory.createStackedBarChart(
                "Aluguéis Diários nos Últimos 30 Dias", // Título do Gráfico
                "Data",                                     // Rótulo do eixo X (domínio)
                "Quantidade de Aluguéis",                   // Rótulo do eixo Y (faixa)
                dataset,                                    // Os dados
                PlotOrientation.VERTICAL,                   // Orientação do gráfico
                true,                                       // Incluir legenda?
                true,                                       // Gerar tooltips (dicas ao passar o mouse)?
                false                                       // Gerar URLs?
        );

        // Personalização da aparência do gráfico
        CategoryPlot plot = stackedBarChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE); // Cor de fundo da área do gráfico

        // Gira os rótulos do eixo X para melhor visualização
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Define as cores para cada série (Normais e Especialistas)
        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(54, 162, 235)); // Azul para "Normais"
        renderer.setSeriesPaint(1, new Color(255, 99, 132)); // Vermelho para "Especialistas"
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // Remove o efeito de gradiente

        return stackedBarChart;
    }

    public void refreshData() {
        // Lógica de agregação dos dados (a mesma de antes)
        Map<LocalDate, int[]> contagensDiarias = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();

        for (int i = 0; i < 30; i++) {
            contagensDiarias.put(hoje.minusDays(i), new int[]{0, 0});
        }

        LocalDate trintaDiasAtras = hoje.minusDays(30);

        List<Aluguel> todosOsAlugueis = GerenciadorDeDados.alugueis;
        for (Aluguel aluguel : todosOsAlugueis) {
            LocalDate dataAluguel = aluguel.getDataAluguel().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            if (!dataAluguel.isBefore(trintaDiasAtras) && dataAluguel.isBefore(hoje.plusDays(1))) {
                int[] contadores = contagensDiarias.get(dataAluguel);
                if (contadores != null) {
                    for (Bicicleta bike : aluguel.getBicicletas()) {
                        if (bike.isSpecialist()) {
                            contadores[1]++;
                        } else {
                            contadores[0]++;
                        }
                    }
                }
            }
        }

        // Atualiza o dataset do JFreeChart com os novos dados
        dataset.clear();

        List<LocalDate> datasOrdenadas = new ArrayList<>(contagensDiarias.keySet());
        Collections.sort(datasOrdenadas); // Ordena para exibir do mais antigo ao mais novo

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (LocalDate data : datasOrdenadas) {
            int[] contadores = contagensDiarias.get(data);
            String categoriaData = data.format(formatter);

            // Adiciona os valores ao dataset no formato: (valor, nome_da_série, nome_da_categoria)
            dataset.addValue(contadores[0], "Normais", categoriaData);
            dataset.addValue(contadores[1], "Especialistas", categoriaData);
        }
        // O ChartPanel se atualiza automaticamente quando o dataset é modificado.
    }
}