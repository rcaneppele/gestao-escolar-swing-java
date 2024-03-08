package br.com.escola.gestaoescolar.gui;

import br.com.escola.gestaoescolar.dominio.Curso;
import br.com.escola.gestaoescolar.dominio.Nivel;
import br.com.escola.gestaoescolar.funcionalidades.CadastroDeCurso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaCadastroCurso extends JPanel {

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoCargaHoraria;
    private JComboBox<Nivel> campoNivel;
    private JTable tabela;
    private String codigoCursoEdicao;
    private Font fonte = new Font("Arial", Font.BOLD, 16);

    private CadastroDeCurso cadastroDeCurso = new CadastroDeCurso();

    public TelaCadastroCurso() {
        setVisible(false);
        setLayout(new BorderLayout());

        var painelFormulario = new JPanel(new GridBagLayout());
        desenharFormulario(painelFormulario);
        add(painelFormulario, BorderLayout.NORTH);

        var painelTabela = new JPanel(new BorderLayout());
        desenharTabela(painelTabela);
        add(painelTabela, BorderLayout.CENTER);
    }

    private void desenharFormulario(JPanel painel) {
        var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        var labelCodigo = new JLabel("Código:");
        labelCodigo.setFont(this.fonte);
        painel.add(labelCodigo, gbc);
        gbc.gridy++;

        var labelNome = new JLabel("Nome:");
        labelNome.setFont(this.fonte);
        painel.add(labelNome, gbc);
        gbc.gridy++;

        var labelCargaHoraria = new JLabel("Carga horária (em horas):");
        labelCargaHoraria.setFont(this.fonte);
        painel.add(labelCargaHoraria, gbc);
        gbc.gridy++;

        var labelNivel = new JLabel("Nível:");
        labelNivel.setFont(this.fonte);
        painel.add(labelNivel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.campoCodigo = new JTextField(20);
        this.campoCodigo.setFont(this.fonte);
        painel.add(this.campoCodigo, gbc);
        gbc.gridy++;

        this.campoNome = new JTextField(20);
        this.campoNome.setFont(this.fonte);
        painel.add(this.campoNome, gbc);
        gbc.gridy++;

        this.campoCargaHoraria = new JTextField(20);
        this.campoCargaHoraria.setFont(this.fonte);
        painel.add(this.campoCargaHoraria, gbc);
        gbc.gridy++;

        this.campoNivel = new JComboBox<>(Nivel.values());
        this.campoNivel.setFont(this.fonte);
        painel.add(this.campoNivel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        var botaoSalvar = new JButton("Salvar");
        botaoSalvar.setFont(this.fonte);
        botaoSalvar.addActionListener(e -> this.cadastrarCurso());
        painel.add(botaoSalvar, gbc);
    }

    private void cadastrarCurso() {
        try {
            var codigo = this.campoCodigo.getText();
            if (codigo.isBlank()) {
                JOptionPane.showMessageDialog(this, "Código é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var nome = this.campoNome.getText();
            if (nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var cargaHoraria = Integer.parseInt(this.campoCargaHoraria.getText());
            if (cargaHoraria <= 0) {
                JOptionPane.showMessageDialog(this, "Carga horária mínima é 1!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var nivel = (Nivel) this.campoNivel.getSelectedItem();
            var curso = new Curso(codigo, nome, cargaHoraria, nivel);

            if (codigoCursoEdicao != null) {
                this.cadastroDeCurso.atualizar(this.codigoCursoEdicao, curso);
                JOptionPane.showMessageDialog(this, "Curso atualizado com sucesso!");
            } else {
                this.cadastroDeCurso.cadastrar(curso);
                JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!");
            }

            this.limparFormulario();
            this.atualizarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro: " +ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        this.campoCodigo.setText("");
        this.campoNome.setText("");
        this.campoCargaHoraria.setText("");
        this.campoNivel.setSelectedIndex(0);
        this.codigoCursoEdicao = null;
    }

    private void desenharTabela(JPanel painel) {
        var cursos = this.cadastroDeCurso.listar();
        var dadosCursosParaTabela = cursos.stream()
                .map(curso -> new Object[]{curso.getCodigo(), curso.getNome(), curso.getCargaHoraria(), curso.getNivel()})
                .toArray(Object[][]::new);

        var tableModel = new DefaultTableModel(dadosCursosParaTabela, new String[]{"Código", "Nome", "Carga Horária", "Nível"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.tabela = new JTable(tableModel);
        this.tabela.setFont(this.fonte);
        this.tabela.getTableHeader().setFont(this.fonte);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                int row = tabela.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tabela.getRowCount()) {
                    tabela.setRowSelectionInterval(row, row);
                    var menu = desenharMenuEditarExcluir();
                    menu.show(tabela, e.getX(), e.getY());
                }
            }
            }
        });

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        var model = (DefaultTableModel) this.tabela.getModel();
        model.setRowCount(0);

        var cursos = new CadastroDeCurso().listar();
        var dadosCursos = cursos.stream()
                .map(curso -> new Object[]{curso.getCodigo(), curso.getNome(), curso.getCargaHoraria(), curso.getNivel()})
                .toArray(Object[][]::new);

        for (Object[] row : dadosCursos) {
            model.addRow(row);
        }
    }

    private JPopupMenu desenharMenuEditarExcluir() {
        var menu = new JPopupMenu();
        var opcaoEditar = new JMenuItem("Editar");
        opcaoEditar.setFont(this.fonte);
        opcaoEditar.addActionListener(e -> {
            var linhaClicada = tabela.getSelectedRow();
            if (linhaClicada != -1) {
                this.campoCodigo.setText(tabela.getValueAt(linhaClicada, 0).toString());
                this.campoNome.setText(tabela.getValueAt(linhaClicada, 1).toString());
                this.campoCargaHoraria.setText(tabela.getValueAt(linhaClicada, 2).toString());
                this.campoNivel.setSelectedItem(Nivel.valueOf(tabela.getValueAt(linhaClicada, 3).toString()));
                this.codigoCursoEdicao = this.campoCodigo.getText();
            }
        });
        menu.add(opcaoEditar);

        var opcaoExcluir = new JMenuItem("Excluir");
        opcaoExcluir.setFont(this.fonte);
        opcaoExcluir.addActionListener(e -> {
            int linhaClicada = tabela.getSelectedRow();
            if (linhaClicada != -1) {
                var codigo = (String) tabela.getValueAt(linhaClicada, 0);
                this.cadastroDeCurso.excluir(codigo);
                JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!");
                this.atualizarTabela();
            }
        });
        menu.add(opcaoExcluir);
        return menu;
    }

}
