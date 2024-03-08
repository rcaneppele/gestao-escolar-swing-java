package br.com.escola.gestaoescolar.gui;

import javax.swing.*;
import java.awt.*;

public class BarraDeMenu extends JMenuBar {

    private JPanel painelPrincipal;

    public BarraDeMenu(JPanel painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        setPreferredSize(new Dimension(this.getWidth(), 30));

        montarMenuEstudantes();
        montarMenuCursos();
        montarMenuTurmas();
    }

    private void montarMenuEstudantes() {
        var menu = new JMenu("Estudantes");
        var opcaoCadastro = new JMenuItem("Cadastro");
        opcaoCadastro.addActionListener(e -> exibirTela(new TelaCadastroEstudante()));
        menu.add(opcaoCadastro);
        add(menu);
    }

    private void montarMenuCursos() {
        var menu = new JMenu("Cursos");
        var opcaoCadastro = new JMenuItem("Cadastro");
        opcaoCadastro.addActionListener(e -> exibirTela(new TelaCadastroCurso()));
        menu.add(opcaoCadastro);
        add(menu);
    }

    private void montarMenuTurmas() {
        var menu = new JMenu("Turmas");
        var opcaoCadastro = new JMenuItem("Cadastro");
        opcaoCadastro.addActionListener(e -> exibirTela(new TelaCadastroTurma()));
        menu.add(opcaoCadastro);
        add(menu);
    }

    private void exibirTela(Component tela) {
        this.painelPrincipal.removeAll();
        this.painelPrincipal.add(tela);
        this.painelPrincipal.revalidate();
        this.painelPrincipal.repaint();
    }

}
