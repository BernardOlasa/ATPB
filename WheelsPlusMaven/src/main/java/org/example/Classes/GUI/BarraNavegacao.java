package org.example.Classes.GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class BarraNavegacao {

    private final JMenuBar barraDeNavegacao;

    public BarraNavegacao(ActionListener aluguelAction, ActionListener cadastroBikeAction, ActionListener promocaoAction, int nivel) {
        barraDeNavegacao = new JMenuBar();

        JMenu menuAluguel = new JMenu("Aluguel");
        menuAluguel.addMenuListener(new MenuListenerAdapter(aluguelAction));
        barraDeNavegacao.add(menuAluguel);

        if (nivel == 1) {
            JMenu menuCadastroBike = new JMenu("Cadastro Bicicletas");
            menuCadastroBike.addMenuListener(new MenuListenerAdapter(cadastroBikeAction));
            barraDeNavegacao.add(menuCadastroBike);

            JMenu menuPromocoes = new JMenu("Promoções");
            menuPromocoes.addMenuListener(new MenuListenerAdapter(promocaoAction));
            barraDeNavegacao.add(menuPromocoes);
        }
    }

    public JMenuBar getBarraNavegacao() {
        return barraDeNavegacao;
    }

    // Adaptador para usar ActionListener com JMenu
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