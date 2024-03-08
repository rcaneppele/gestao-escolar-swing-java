package br.com.escola.gestaoescolar.funcionalidades;

import br.com.escola.gestaoescolar.dominio.Periodo;
import br.com.escola.gestaoescolar.dominio.Turma;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CadastroDeTurma {

    private Path arquivo;

    public CadastroDeTurma() {
        try {
            this.arquivo = Path.of("turmas.csv");
            if (!Files.exists(arquivo)) {
                Files.createFile(arquivo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo de turmas!");
        }
    }

    public void cadastrar(String codigo, LocalDate dataInicio, LocalDate dataFim, Periodo periodo, String codigoCurso) {
        //validacoes
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("Campo código é obrigatório!");
        }

        if (dataInicio == null) {
            throw new IllegalArgumentException("Campo data início é obrigatório!");
        }

        if (dataFim == null) {
            throw new IllegalArgumentException("Campo data fim é obrigatório!");
        }

        if (periodo == null) {
            throw new IllegalArgumentException("Campo período é obrigatório!");
        }

        if (codigoCurso == null) {
            throw new IllegalArgumentException("Campo código do curso é obrigatório!");
        }

        var turmasCadastradas = listar();
        for (var t : turmasCadastradas) {
            if (t.getCodigo().equals(codigo)) {
                throw new IllegalArgumentException("Código já cadastrado!");
            }
        }

        var cadastroDeCursos = new CadastroDeCurso();
        var curso = cadastroDeCursos.carregarCursoPeloCodigo(codigoCurso);
        if (curso == null) {
            throw new IllegalArgumentException("Código do curso inexistente!");
        }

        var mascaraData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            Files.writeString(
                    arquivo,
                    codigo +"," + dataInicio.format(mascaraData) + "," +dataFim.format(mascaraData) + "," +periodo +"," +codigoCurso +"\n",
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar turma no arquivo!");
        }
    }

    public List<Turma> listar() {
        var lista = new ArrayList<Turma>();

        try {
            var linhas = Files.readAllLines(arquivo);

            var mascaraData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            var cadastroDeCursos = new CadastroDeCurso();

            for(var linha : linhas) {
                var campos = linha.split(",");

                var codigoCurso = campos[4];
                var curso = cadastroDeCursos.carregarCursoPeloCodigo(codigoCurso);

                var turma = new Turma(campos[0], curso, LocalDate.parse(campos[1], mascaraData), LocalDate.parse(campos[2], mascaraData), Periodo.valueOf(campos[3]));
                lista.add(turma);
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Erro ao carregar turmas do arquivo!");
            return lista;
        }
    }

    public void excluir(String codigo) {
        try {
            var linhas = Files.readAllLines(arquivo);
            var iterator = linhas.iterator();
            while (iterator.hasNext()) {
                String linha = iterator.next();
                String[] campos = linha.split(",");
                if (campos[0].equals(codigo)) {
                    iterator.remove();
                    break;
                }
            }
            Files.write(arquivo, linhas);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir turma do arquivo!");
        }
    }

    public void atualizar(String codigoTurmaAnterior, String codigo, LocalDate dataInicio, LocalDate dataFim, Periodo periodo, String codigoCurso) {
        this.excluir(codigoTurmaAnterior);
        this.cadastrar(codigo, dataInicio, dataFim, periodo, codigoCurso);
    }

}
