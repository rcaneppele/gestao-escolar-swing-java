package br.com.escola.gestaoescolar.funcionalidades;

import br.com.escola.gestaoescolar.dominio.Curso;
import br.com.escola.gestaoescolar.dominio.Nivel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CadastroDeCurso {

    private Path arquivo;

    public CadastroDeCurso() {
        try {
            this.arquivo = Path.of("cursos.csv");
            if (!Files.exists(arquivo)) {
                Files.createFile(arquivo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar arquivo de cursos!");
        }
    }

    public void cadastrar(Curso curso) {
        //validacoes
        if (curso.getNome().isBlank()) {
            throw new IllegalArgumentException("Campo nome é obrigatório!");
        }

        if (curso.getCodigo().isBlank()) {
            throw new IllegalArgumentException("Campo código é obrigatório!");
        }

        if (curso.getCargaHoraria() <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser maior do que zero!");
        }

        if (curso.getNivel() == null) {
            throw new IllegalArgumentException("Campo nível é obrigatório!");
        }

        var cursosCadastrados = listar();
        if (cursosCadastrados.contains(curso)) {
            throw new IllegalArgumentException("Código já cadastrado!");
        }

        try {
            Files.writeString(
                    arquivo,
                    curso.getCodigo() + "," + curso.getNome() + "," + curso.getCargaHoraria() + "," + curso.getNivel() + "\n",
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar curso no arquivo!");
        }
    }

    public List<Curso> listar() {
        var lista = new ArrayList<Curso>();

        try {
            var linhas = Files.readAllLines(arquivo);

            for(var linha : linhas) {
                var campos = linha.split(",");
                var curso = new Curso(campos[0], campos[1], Integer.parseInt(campos[2]), Nivel.valueOf(campos[3]));
                lista.add(curso);
            }

            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar cursos do arquivo!");
        }
    }

    public Curso carregarCursoPeloCodigo(String codigoCurso) {
        var cursosCadastrados = listar();
        for (var curso : cursosCadastrados) {
            if (curso.getCodigo().equals(codigoCurso)) {
                return curso;
            }
        }

        return null;
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
            throw new RuntimeException("Erro ao excluir curso do arquivo!");
        }
    }

    public void atualizar(String codigoCurso, Curso curso) {
        this.excluir(codigoCurso);
        this.cadastrar(curso);
    }

}
