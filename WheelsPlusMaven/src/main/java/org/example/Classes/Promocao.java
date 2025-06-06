package org.example.Classes;

import java.util.*;

public final class Promocao {
    protected Date dataInicio;
    protected Date dataFim;
    protected double porcentagem;
    protected boolean estacavel;




    //--------------------------------------{Construtor}--------------------------------------

    public Promocao(Date dataInicio, Date dataFim, double porcentagem, boolean estacavel) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.porcentagem = porcentagem;
        this.estacavel = estacavel;
    }

    //----------------------------------------{GET SET}---------------------------------------
    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    public Date getDataFim() {
        return dataFim;
    }
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
    public double getPorcentagem() {
        return porcentagem;
    }
    public void setPorcentagem(double porcentagem) {
        this.porcentagem = porcentagem;
    }
    public boolean isEstacavel() {
        return estacavel;
    }
    public void setEstacavel(boolean estacavel) {
        this.estacavel = estacavel;
    }
}

