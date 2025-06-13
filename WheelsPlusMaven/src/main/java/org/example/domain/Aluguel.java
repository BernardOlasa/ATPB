package org.example.domain;

import java.util.*;

public final class Aluguel {
    protected int idAluguel, diasAlugados;
    protected List<Bicicleta> bicicletas;
    protected Cliente cliente;
    protected Date dataAluguel;
    protected double valorAluguel, valorTotal,valorSeguro;
    protected boolean bicicletasDevolvidas;

    public boolean isBicicletasDevolvidas() {
        return bicicletasDevolvidas;
    }

    public void setBicicletasDevolvidas(boolean bicicletasDevolvidas) {
        this.bicicletasDevolvidas = bicicletasDevolvidas;
    }



    public double calcularValorAluguel(){
        double totAluguel = 0;
        for(Bicicleta bc:bicicletas){
            totAluguel +=bc.pegarValorRealAluguel();
        }
        return totAluguel * diasAlugados;
    }
    public double calcularValorSeguro(){
        double totSeguro = 0;
        for(Bicicleta bc:bicicletas){
            totSeguro += bc.valorSeguro;
        }
        return totSeguro;
    }

    public double calcularValorTotal() {
        return valorAluguel + valorSeguro;
    }

    @Override
    public String toString(){
        return String.format("%d,%d,%s,%s,%th",idAluguel,diasAlugados,cliente,bicicletas,dataAluguel);
    }

    public Aluguel(int idAluguel, int diasAlugados, Cliente cliente, List<Bicicleta> bicicletas, Date dataAluguel) {
        this.idAluguel = idAluguel;
        this.diasAlugados = diasAlugados;
        this.cliente = cliente;
        this.bicicletas = bicicletas;
        this.dataAluguel = dataAluguel;
        valorAluguel = calcularValorAluguel();
        valorSeguro = calcularValorSeguro();
        valorTotal = calcularValorTotal();
    }


    public int getIdAluguel() {
        return idAluguel;
    }
    public void setIdAluguel(int idAluguel) {
        this.idAluguel = idAluguel;
    }
    public int getDiasAlugados() {
        return diasAlugados;
    }
    public void setDiasAlugados(int diasAlugados) {
        this.diasAlugados = diasAlugados;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public List<Bicicleta> getBicicletas() {
        return bicicletas;
    }
    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
    }
    public Date getDataAluguel() {
        return dataAluguel;
    }
    public void setDataAluguel(Date dataAluguel) {
        this.dataAluguel = dataAluguel;
    }
    public double getValorAluguel() {
        return valorAluguel;
    }
    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }
    public double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public double getValorSeguro() {
        return valorSeguro;
    }
    public void setValorSeguro(double valorSeguro) {
        this.valorSeguro = valorSeguro;
    }
}
