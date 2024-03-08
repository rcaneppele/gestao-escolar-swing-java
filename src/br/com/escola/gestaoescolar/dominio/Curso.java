package br.com.escola.gestaoescolar.dominio;

import java.util.Objects;

public class Curso {

    private String codigo;
    private String nome;
    private int cargaHoraria;
    private Nivel nivel;

    public Curso(String codigo, String nome, int cargaHoraria, Nivel nivel) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(codigo.toLowerCase(), curso.codigo.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public Nivel getNivel() {
        return nivel;
    }

}
