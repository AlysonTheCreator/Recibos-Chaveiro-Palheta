package model;

public class Servico {
    private String nome;
    private int quantidade;
    private double preco;
    private double valorTotal;

    public Servico(String nome, int quantidade, double preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.valorTotal = quantidade * preco;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}