package br.com.escola.gestaoescolar.gui;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JPanel {

    public TelaInicial() {
        var textoBoasVindas = new JLabel();
        textoBoasVindas.setText("Boas vindas ao Sistema de Gestão Escolar!");
        textoBoasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        textoBoasVindas.setVerticalAlignment(SwingConstants.CENTER);
        textoBoasVindas.setFont(new Font("Arial", Font.BOLD, 24));

        this.add(textoBoasVindas);
        this.setLayout(new CardLayout());

        setVisible(true);
    }

}
