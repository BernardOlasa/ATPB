package org.example.ui;

import org.example.domain.Bicicleta;
import javax.swing.JPanel;

@FunctionalInterface
public interface CardFactory {

    JPanel createCard(Bicicleta bicicleta);
}