package org.example.Classes.GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class BarraNavegacao {

    private final JMenuBar barraDeNavegacao;

    public BarraNavegacao(ActionListener aluguelAction, ActionListener entregaAction,
                          ActionListener historicoAction, ActionListener dashboardAction, // PARÂMETRO ADICIONADO
                          ActionListener cadastroBikeAction, ActionListener promocaoAction,
                          ActionListener crudClienteAction, ActionListener crudBicicletaAction, int nivel) {
        barraDeNavegacao = new JMenuBar();

        JMenu menuAluguel = new JMenu("Aluguel");
        menuAluguel.addMenuListener(new MenuListenerAdapter(aluguelAction));
        barraDeNavegacao.add(menuAluguel);

        JMenu menuEntrega = new JMenu("Entregas");
        menuEntrega.addMenuListener(new MenuListenerAdapter(entregaAction));
        barraDeNavegacao.add(menuEntrega);

        if (nivel == 1) {
            JMenu menuHistorico = new JMenu("Histórico");
            menuHistorico.addMenuListener(new MenuListenerAdapter(historicoAction));
            barraDeNavegacao.add(menuHistorico);

            JMenu menuDashboard = new JMenu("Dashboard"); // NOVO MENU ADICIONADO
            menuDashboard.addMenuListener(new MenuListenerAdapter(dashboardAction));
            barraDeNavegacao.add(menuDashboard);

            JMenu menuCadastroBike = new JMenu("Cadastro Bicicletas");
            menuCadastroBike.addMenuListener(new MenuListenerAdapter(cadastroBikeAction));
            barraDeNavegacao.add(menuCadastroBike);

            JMenu menuPromocoes = new JMenu("Promoções");
            menuPromocoes.addMenuListener(new MenuListenerAdapter(promocaoAction));
            barraDeNavegacao.add(menuPromocoes);

            JMenu menuCRUDCliente = new JMenu("CRUD Clientes");
            menuCRUDCliente.addMenuListener(new MenuListenerAdapter(crudClienteAction));
            barraDeNavegacao.add(menuCRUDCliente);

            JMenu menuCRUDBicicleta = new JMenu("CRUD Bicicletas");
            menuCRUDBicicleta.addMenuListener(new MenuListenerAdapter(crudBicicletaAction));
            barraDeNavegacao.add(menuCRUDBicicleta);
        }
    }

    public JMenuBar getBarraNavegacao() {
        return barraDeNavegacao;
    }

    private static class MenuListenerAdapter implements javax.swing.event.MenuListener {
        private final ActionListener actionListener;

        public MenuListenerAdapter(ActionListener listener) {
            this.actionListener = listener;
        }

        @Override
        public void menuSelected(javax.swing.event.MenuEvent e) {
            if (actionListener != null) {
                actionListener.actionPerformed(null);
            }
        }

        @Override
        public void menuDeselected(javax.swing.event.MenuEvent e) {}

        @Override
        public void menuCanceled(javax.swing.event.MenuEvent e) {}
    }
}