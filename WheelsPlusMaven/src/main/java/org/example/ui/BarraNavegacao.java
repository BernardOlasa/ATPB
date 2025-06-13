package org.example.ui;

import org.example.util.PaletaCores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BarraNavegacao {

    private final JMenuBar barraDeNavegacao;

    public BarraNavegacao(ActionListener aluguelAction, ActionListener entregaAction,
                          ActionListener historicoAction, ActionListener dashboardAction,
                          ActionListener promocaoAction,
                          ActionListener crudClienteAction, ActionListener crudBicicletaAction, int nivel) {
        barraDeNavegacao = new JMenuBar();
        barraDeNavegacao.setBackground(PaletaCores.SECONDARY_ACCENT);
        barraDeNavegacao.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JMenu menuAluguel = createStyledMenu("Aluguel");
        addClickListenerToMenu(menuAluguel, aluguelAction);
        barraDeNavegacao.add(menuAluguel);

        JMenu menuEntrega = createStyledMenu("Entregas");
        addClickListenerToMenu(menuEntrega, entregaAction);
        barraDeNavegacao.add(menuEntrega);

        if (nivel == 1) {
            JMenu menuHistorico = createStyledMenu("Histórico");
            addClickListenerToMenu(menuHistorico, historicoAction);
            barraDeNavegacao.add(menuHistorico);

            JMenu menuDashboard = createStyledMenu("Dashboard");
            addClickListenerToMenu(menuDashboard, dashboardAction);
            barraDeNavegacao.add(menuDashboard);

            JMenu menuPromocoes = createStyledMenu("Promoções");
            addClickListenerToMenu(menuPromocoes, promocaoAction);
            barraDeNavegacao.add(menuPromocoes);

            JMenu menuCRUDCliente = createStyledMenu("Clientes");
            addClickListenerToMenu(menuCRUDCliente, crudClienteAction);
            barraDeNavegacao.add(menuCRUDCliente);

            JMenu menuCRUDBicicleta = createStyledMenu("Bicicletas");
            addClickListenerToMenu(menuCRUDBicicleta, crudBicicletaAction);
            barraDeNavegacao.add(menuCRUDBicicleta);
        }
    }

    private JMenu createStyledMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(PaletaCores.TEXT_LIGHT);
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return menu;
    }
    private void addClickListenerToMenu(JMenu menu, ActionListener listener) {
        if (listener == null) return;
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(null);
            }
        });
    }

    public JMenuBar getBarraNavegacao() {
        return barraDeNavegacao;
    }
}