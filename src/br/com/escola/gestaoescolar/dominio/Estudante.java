package br.com.escola.gestaoescolar.dominio;

import java.util.Objects;

public class Estudante {

    private String nome;
    private String telefone;
    private String endereco;
    private String cpf;
    private String email;

    public Estudante(String nome, String telefone, String endereco, String cpf, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cpf = cpf;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudante estudante = (Estudante) o;
        return Objects.equals(cpf, estudante.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

}
