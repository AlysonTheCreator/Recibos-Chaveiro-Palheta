package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Recibo {
    private int id;
    private String dataHora;
    private String nomeEmpresa;
    private String cnpj;
    private String endereco;
    private List<Servico> itens;

    public Recibo() {
        this.itens = new ArrayList<>();
        this.dataHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.nomeEmpresa = "";
        this.cnpj = "";
        this.endereco = "";
    }

    public void adicionarServico(Servico servico) {
        itens.add(servico);
    }

    public void removerServico(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
        }
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public double getTotalGeral() {
        double total = 0;
        for (Servico servico : itens) {
            total += servico.getValorTotal();
        }
        return total;
    }

    public List<Servico> getItens() {
        return itens;
    }

    public String getDataHora() {
        return dataHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa != null ? nomeEmpresa.trim() : "";
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj != null ? cnpj.trim() : "";
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco != null ? endereco.trim() : "";
    }

    public boolean temDadosCliente() {
        return !nomeEmpresa.isBlank() || !cnpj.isBlank() || !endereco.isBlank();
    }
}