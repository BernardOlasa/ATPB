package org.example.Classes;

import java.util.*;

public final class Bicicleta {
    protected int idBicicleta, aroDaRoda;
    protected double valorAluguel, valorSeguro;
    protected boolean specialist, alugada, emManutencao;
    protected List<Promocao> promocoes;
    protected String marca, modelo, cor, materialDoAro, materialDaBicicleta;


    public double pegarMaiorPromocao() {
        double promocaoEstacada = 0;
        double maiorPromocao = 0;
        for (Promocao p : promocoes) {
            if (p.estacavel) {
                promocaoEstacada += p.porcentagem;
            }
            if (p.porcentagem > maiorPromocao) {
                maiorPromocao = p.porcentagem;
            }
            if (promocaoEstacada > maiorPromocao) {
                maiorPromocao = promocaoEstacada;
            }
        }
        return maiorPromocao;
    }

    public double pegarValorRealAluguel() {
        return valorAluguel * ((pegarMaiorPromocao() / 100) + 1);
    }


    @Override
    public String toString() {
        return String.format("%d,%d,%.2f,%.2f,%b,%b,%b,%s,%s,%s,%s,%s,%s",
                idBicicleta, aroDaRoda, valorAluguel, valorSeguro, specialist, alugada, emManutencao, promocoes, marca, modelo, cor, materialDoAro, materialDaBicicleta);
    }

    //--------------------------------------{Construtor}--------------------------------------
    public Bicicleta(int idBicicleta, int aroDaRoda, double valorAluguel, double valorSeguro, boolean specialist, boolean alugada, boolean emManutencao, List<Promocao> promocoes, String marca, String modelo, String cor, String materialDoAro, String materialDaBicicleta) {
        this.idBicicleta = idBicicleta;
        this.aroDaRoda = aroDaRoda;
        this.valorAluguel = valorAluguel;
        this.valorSeguro = valorSeguro;
        this.specialist = specialist;
        this.alugada = alugada;
        this.emManutencao = emManutencao;
        this.promocoes = promocoes;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.materialDoAro = materialDoAro;
        this.materialDaBicicleta = materialDaBicicleta;
    }

    //----------------------------------------{GET SET}---------------------------------------
    public int getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(int idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public int getAroDaRoda() {
        return aroDaRoda;
    }

    public void setAroDaRoda(int aroDaRoda) {
        this.aroDaRoda = aroDaRoda;
    }

    public double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public double getValorSeguro() {
        return valorSeguro;
    }

    public void setValorSeguro(double valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    public boolean isSpecialist() {
        return specialist;
    }

    public void setSpecialist(boolean specialist) {
        this.specialist = specialist;
    }

    public boolean isAlugada() {return alugada;}

    public void setAlugada(boolean alugada) {
        this.alugada = alugada;
    }

    public boolean isEmManutencao() {return emManutencao;}

    public void setEmManutencao(boolean emManutencao) {
        this.emManutencao = emManutencao;
    }

    public List<Promocao> getPromocoes() {
        return promocoes;
    }

    public void setPromocoes(List<Promocao> promocoes) {
        this.promocoes = promocoes;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getMaterialDoAro() {
        return materialDoAro;
    }

    public void setMaterialDoAro(String materialDoAro) {
        this.materialDoAro = materialDoAro;
    }

    public String getMaterialDaBicicleta() {
        return materialDaBicicleta;
    }

    public void setMaterialDaBicicleta(String materialDaBicicleta) {
        this.materialDaBicicleta = materialDaBicicleta;
    }
}
