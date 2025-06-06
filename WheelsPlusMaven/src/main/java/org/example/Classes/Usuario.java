package org.example.Classes;

public final class Usuario {

    protected int nivel;
    protected int nome;
    protected int senha;

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getNome() {
        return nome;
    }

    public void setNome(int nome) {
        this.nome = nome;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }



    public Usuario(int nivel, String nome, String senha) {
        this.nivel = nivel;
        this.nome = nome.hashCode();
        this.senha = senha.hashCode();
    }




}
