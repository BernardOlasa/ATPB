package org.example.ui;

import org.example.domain.Aluguel;
import org.example.domain.Bicicleta;
import org.example.data.GerenciadorDeDados;

import org.example.util.PaletaCores;
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
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PainelDashboard extends JPanel {

    private final DefaultCategoryDataset dataset;
    private final ChartPanel chartPanel;

    public PainelDashboard() {
        setLayout(new BorderLayout());
        setBackground(PaletaCores.BACKGROUND);

        this.dataset = new DefaultCategoryDataset();

        JFreeChart chart = createChart(dataset);

        this.chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(PaletaCores.BACKGROUND);
        add(this.chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        JFreeChart stackedBarChart = ChartFactory.createStackedBarChart(
                "Aluguéis Diários nos Últimos 30 Dias",
                "Data",
                "Quantidade de Aluguéis",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        stackedBarChart.setBackgroundPaint(PaletaCores.BACKGROUND);
        stackedBarChart.getTitle().setPaint(PaletaCores.TEXT_DARK);
        stackedBarChart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));

        CategoryPlot plot = stackedBarChart.getCategoryPlot();
        plot.setBackgroundPaint(PaletaCores.PANEL_BACKGROUND);
        plot.setRangeGridlinePaint(PaletaCores.BORDER_COLOR);
        plot.setOutlinePaint(PaletaCores.BORDER_COLOR);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setLabelPaint(PaletaCores.TEXT_DARK);
        domainAxis.setTickLabelPaint(PaletaCores.TEXT_DARK);
        plot.getRangeAxis().setLabelPaint(PaletaCores.TEXT_DARK);
        plot.getRangeAxis().setTickLabelPaint(PaletaCores.TEXT_DARK);

        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, PaletaCores.PRIMARY_ACCENT); // Normais
        renderer.setSeriesPaint(1, PaletaCores.SECONDARY_ACCENT); // Especialistas
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        return stackedBarChart;
    }

    public void refreshData() {
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

        dataset.clear();

        List<LocalDate> datasOrdenadas = new ArrayList<>(contagensDiarias.keySet());
        Collections.sort(datasOrdenadas);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (LocalDate data : datasOrdenadas) {
            int[] contadores = contagensDiarias.get(data);
            String categoriaData = data.format(formatter);

            dataset.addValue(contadores[0], "Normais", categoriaData);
            dataset.addValue(contadores[1], "Especialistas", categoriaData);
        }
    }
}