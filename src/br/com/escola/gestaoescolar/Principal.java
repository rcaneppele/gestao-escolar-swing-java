package br.com.escola.gestaoescolar;

import br.com.escola.gestaoescolar.gui.BarraDeMenu;
import br.com.escola.gestaoescolar.gui.TelaInicial;

import javax.swing.*;
import java.awt.*;

public class Principal extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::new);
    }

    public Principal() {
        var painelPrincipal = configurarTelasDaAplicacao();
        var barraDeMenu = new BarraDeMenu(painelPrincipal);
        configurarJanelaDaAplicacao(barraDeMenu);
    }

    private void configurarJanelaDaAplicacao(JMenuBar barraDeMenu) {
        setTitle("Gestão Escolar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(640, 480));
        setVisible(true);
        setJMenuBar(barraDeMenu);
    }

    private JPanel configurarTelasDaAplicacao() {
        var painelPrincipal = new JPanel(new CardLayout());
        painelPrincipal.add(new TelaInicial());
        getContentPane().add(painelPrincipal);
        return painelPrincipal;
    }

}
