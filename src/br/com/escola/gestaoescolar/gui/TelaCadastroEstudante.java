package br.com.escola.gestaoescolar.gui;

import br.com.escola.gestaoescolar.dominio.Estudante;
import br.com.escola.gestaoescolar.funcionalidades.CadastroDeEstudante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaCadastroEstudante extends JPanel {

    private JTextField campoNome;
    private JTextField campoTelefone;
    private JTextField campoEndereco;
    private JTextField campoCpf;
    private JTextField campoEmail;
    private JTable tabela;
    private String cpfEstudanteEdicao;
    private Font fonte = new Font("Arial", Font.BOLD, 16);

    private CadastroDeEstudante cadastroDeEstudante = new CadastroDeEstudante();

    public TelaCadastroEstudante() {
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

        var labelNome = new JLabel("Nome:");
        labelNome.setFont(this.fonte);
        painel.add(labelNome, gbc);
        gbc.gridy++;

        var labelTelefone = new JLabel("Telefone:");
        labelTelefone.setFont(this.fonte);
        painel.add(labelTelefone, gbc);
        gbc.gridy++;

        var labelEndereco = new JLabel("Endreeço:");
        labelEndereco.setFont(this.fonte);
        painel.add(labelEndereco, gbc);
        gbc.gridy++;

        var labelCpf = new JLabel("CPF:");
        labelCpf.setFont(this.fonte);
        painel.add(labelCpf, gbc);
        gbc.gridy++;

        var labelEmail = new JLabel("Email:");
        labelEmail.setFont(this.fonte);
        painel.add(labelEmail, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.campoNome = new JTextField(20);
        this.campoNome.setFont(this.fonte);
        painel.add(this.campoNome, gbc);
        gbc.gridy++;

        this.campoTelefone = new JTextField(20);
        this.campoTelefone.setFont(this.fonte);
        painel.add(this.campoTelefone, gbc);
        gbc.gridy++;

        this.campoEndereco = new JTextField(20);
        this.campoEndereco.setFont(this.fonte);
        painel.add(this.campoEndereco, gbc);
        gbc.gridy++;

        this.campoCpf = new JTextField(20);
        this.campoCpf.setFont(this.fonte);
        painel.add(this.campoCpf, gbc);
        gbc.gridy++;

        this.campoEmail = new JTextField(20);
        this.campoEmail.setFont(this.fonte);
        painel.add(this.campoEmail, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        var botaoSalvar = new JButton("Salvar");
        botaoSalvar.setFont(this.fonte);
        botaoSalvar.addActionListener(e -> this.cadastrarEstudante());
        painel.add(botaoSalvar, gbc);
    }

    private void cadastrarEstudante() {
        try {
            var nome = this.campoNome.getText();
            if (nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var telefone = this.campoTelefone.getText();
            if (telefone.isBlank()) {
                JOptionPane.showMessageDialog(this, "Telefone é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var endereco = this.campoEndereco.getText();
            if (endereco.isBlank()) {
                JOptionPane.showMessageDialog(this, "Endereço é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var cpf = this.campoCpf.getText();
            if (cpf.isBlank()) {
                JOptionPane.showMessageDialog(this, "CPF é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var email = this.campoEmail.getText();
            if (email.isBlank()) {
                JOptionPane.showMessageDialog(this, "Email é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var estudante = new Estudante(nome, telefone, endereco, cpf, email);

            if (cpfEstudanteEdicao != null) {
                this.cadastroDeEstudante.atualizar(this.cpfEstudanteEdicao, estudante);
                JOptionPane.showMessageDialog(this, "Estudante atualizado com sucesso!");
            } else {
                this.cadastroDeEstudante.cadastrar(estudante);
                JOptionPane.showMessageDialog(this, "Estudante cadastrado com sucesso!");
            }

            this.limparFormulario();
            this.atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro: " +ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        this.campoNome.setText("");
        this.campoTelefone.setText("");
        this.campoEndereco.setText("");
        this.campoCpf.setText("");
        this.campoEmail.setText("");
        this.cpfEstudanteEdicao = null;
    }

    private void desenharTabela(JPanel painel) {
        var estudantes = this.cadastroDeEstudante.listar();
        var dadosEstudantesParaTabela = estudantes.stream()
                .map(estudante -> new Object[]{estudante.getNome(), estudante.getTelefone(), estudante.getEndereco(), estudante.getCpf(), estudante.getEmail()})
                .toArray(Object[][]::new);

        var tableModel = new DefaultTableModel(dadosEstudantesParaTabela, new String[]{"Nome", "Telefone", "Endereço", "CPF", "Email"}) {
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

        var estudantes = new CadastroDeEstudante().listar();
        var dadosEstudantes = estudantes.stream()
                .map(estudante -> new Object[]{estudante.getNome(), estudante.getTelefone(), estudante.getEndereco(), estudante.getCpf(), estudante.getEmail()})
                .toArray(Object[][]::new);

        for (Object[] row : dadosEstudantes) {
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
                this.campoNome.setText(tabela.getValueAt(linhaClicada, 0).toString());
                this.campoTelefone.setText(tabela.getValueAt(linhaClicada, 1).toString());
                this.campoEndereco.setText(tabela.getValueAt(linhaClicada, 2).toString());
                this.campoCpf.setText(tabela.getValueAt(linhaClicada, 3).toString());
                this.campoEmail.setText(tabela.getValueAt(linhaClicada, 4).toString());
                this.cpfEstudanteEdicao = this.campoCpf.getText();
            }
        });
        menu.add(opcaoEditar);

        var opcaoExcluir = new JMenuItem("Excluir");
        opcaoExcluir.setFont(this.fonte);
        opcaoExcluir.addActionListener(e -> {
            int linhaClicada = tabela.getSelectedRow();
            if (linhaClicada != -1) {
                var cpf = (String) tabela.getValueAt(linhaClicada, 3);
                this.cadastroDeEstudante.excluir(cpf);
                JOptionPane.showMessageDialog(this, "Estudante excluído com sucesso!");
                this.atualizarTabela();
            }
        });
        menu.add(opcaoExcluir);
        return menu;
    }

}
