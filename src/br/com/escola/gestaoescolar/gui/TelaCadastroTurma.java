package br.com.escola.gestaoescolar.gui;

import br.com.escola.gestaoescolar.dominio.Periodo;
import br.com.escola.gestaoescolar.funcionalidades.CadastroDeCurso;
import br.com.escola.gestaoescolar.funcionalidades.CadastroDeTurma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaCadastroTurma extends JPanel {

    private JTextField campoCodigo;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JComboBox<Periodo> campoPeriodo;
    private JComboBox<String> campoCurso;
    private JTable tabela;
    private String codigoTurmaEdicao;
    private Font fonte = new Font("Arial", Font.BOLD, 16);
    private DateTimeFormatter mascara = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private CadastroDeTurma cadastroDeTurma = new CadastroDeTurma();

    public TelaCadastroTurma() {
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

        var labelDataInicio = new JLabel("Data início:");
        labelDataInicio.setFont(this.fonte);
        painel.add(labelDataInicio, gbc);
        gbc.gridy++;

        var labelDataFim = new JLabel("Data fim:");
        labelDataFim.setFont(this.fonte);
        painel.add(labelDataFim, gbc);
        gbc.gridy++;

        var labelPeriodo = new JLabel("Período:");
        labelPeriodo.setFont(this.fonte);
        painel.add(labelPeriodo, gbc);
        gbc.gridy++;

        var labelCurso = new JLabel("Curso:");
        labelCurso.setFont(this.fonte);
        painel.add(labelCurso, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.campoCodigo = new JTextField(20);
        this.campoCodigo.setFont(this.fonte);
        painel.add(this.campoCodigo, gbc);
        gbc.gridy++;

        this.campoDataInicio = new JTextField(20);
        this.campoDataInicio.setFont(this.fonte);
        painel.add(this.campoDataInicio, gbc);
        gbc.gridy++;

        this.campoDataFim = new JTextField(20);
        this.campoDataFim.setFont(this.fonte);
        painel.add(this.campoDataFim, gbc);
        gbc.gridy++;

        this.campoPeriodo = new JComboBox<>(Periodo.values());
        this.campoPeriodo.setFont(this.fonte);
        painel.add(this.campoPeriodo, gbc);
        gbc.gridy++;

        this.campoCurso = new JComboBox<>((String[]) new CadastroDeCurso().listar().stream().map(c -> c.getCodigo() +" - " +c.getNome()).toArray(String[]::new));
        this.campoCurso.setFont(this.fonte);
        painel.add(this.campoCurso, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        var botaoSalvar = new JButton("Salvar");
        botaoSalvar.setFont(this.fonte);
        botaoSalvar.addActionListener(e -> this.cadastrarTurma());
        painel.add(botaoSalvar, gbc);
    }

    private void cadastrarTurma() {
        try {
            var codigo = this.campoCodigo.getText();
            if (codigo.isBlank()) {
                JOptionPane.showMessageDialog(this, "Código é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (this.campoDataInicio.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Data início é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (this.campoDataFim.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Data fim é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (this.campoCurso.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Escolha um curso!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var dataInicio = LocalDate.parse(this.campoDataInicio.getText(), mascara);
            var dataFim = LocalDate.parse(this.campoDataFim.getText(), mascara);
            var periodo = (Periodo) this.campoPeriodo.getSelectedItem();
            var codigoCurso = this.campoCurso.getSelectedItem().toString().split(" - ")[0];

            if (codigoTurmaEdicao != null) {
                this.cadastroDeTurma.atualizar(this.codigoTurmaEdicao, codigo, dataInicio, dataFim, periodo, codigoCurso);
                JOptionPane.showMessageDialog(this, "Turma atualizada com sucesso!");
            } else {
                this.cadastroDeTurma.cadastrar(codigo, dataInicio, dataFim, periodo, codigoCurso);
                JOptionPane.showMessageDialog(this, "Turma cadastrada com sucesso!");
            }

            this.limparFormulario();
            this.atualizarTabela();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data em formato inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro: " +ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        this.campoCodigo.setText("");
        this.campoDataInicio.setText("");
        this.campoDataFim.setText("");
        this.campoPeriodo.setSelectedIndex(0);
        this.campoCurso.setSelectedIndex(0);
        this.codigoTurmaEdicao = null;
    }

    private void desenharTabela(JPanel painel) {
        var turmas = this.cadastroDeTurma.listar();
        var dadosTurmasParaTabela = turmas.stream()
                .map(turma -> new Object[]{turma.getCodigo(), turma.getDataInicio().format(mascara), turma.getDataFim().format(mascara), turma.getPeriodo(), turma.getCurso().getCodigo() +" - " +turma.getCurso().getNome()})
                .toArray(Object[][]::new);

        var tableModel = new DefaultTableModel(dadosTurmasParaTabela, new String[]{"Código", "Início", "Fim", "Período", "Curso"}) {
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

        var turmas = new CadastroDeTurma().listar();
        var dadosTurmas = turmas.stream()
                .map(turma -> new Object[]{turma.getCodigo(), turma.getDataInicio().format(mascara), turma.getDataFim().format(mascara), turma.getPeriodo(), turma.getCurso().getCodigo() +" - " +turma.getCurso().getNome()})
                .toArray(Object[][]::new);

        for (Object[] row : dadosTurmas) {
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
                this.campoDataInicio.setText(tabela.getValueAt(linhaClicada, 1).toString());
                this.campoDataFim.setText(tabela.getValueAt(linhaClicada, 2).toString());
                this.campoPeriodo.setSelectedItem(Periodo.valueOf(tabela.getValueAt(linhaClicada, 3).toString()));
                this.campoCurso.setSelectedItem(tabela.getValueAt(linhaClicada, 4).toString());
                this.codigoTurmaEdicao = this.campoCodigo.getText();
            }
        });
        menu.add(opcaoEditar);

        var opcaoExcluir = new JMenuItem("Excluir");
        opcaoExcluir.setFont(this.fonte);
        opcaoExcluir.addActionListener(e -> {
            int linhaClicada = tabela.getSelectedRow();
            if (linhaClicada != -1) {
                var codigo = (String) tabela.getValueAt(linhaClicada, 0);
                this.cadastroDeTurma.excluir(codigo);
                JOptionPane.showMessageDialog(this, "Turma excluído com sucesso!");
                this.atualizarTabela();
            }
        });
        menu.add(opcaoExcluir);
        return menu;
    }

}
