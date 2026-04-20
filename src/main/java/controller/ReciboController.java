package controller;

import dao.ReciboDAO;
import model.Recibo;
import model.Servico;
import util.GeradorReciboPDF;


public class ReciboController {
    private Recibo reciboAtual;
    private final ReciboDAO reciboDAO;


    public ReciboController() {
        this.reciboAtual = new Recibo();
        this.reciboDAO = new ReciboDAO();

    }

    public void definirDadosCliente(String nomeEmpresa, String cnpj, String endereco) {
        reciboAtual.setNomeEmpresa(nomeEmpresa);
        reciboAtual.setCnpj(cnpj);
        reciboAtual.setEndereco(endereco);
    }

    public void adicionarServico(String nome, int quantidade, double preco) {
        Servico servico = new Servico(nome, quantidade, preco);
        reciboAtual.adicionarServico(servico);
    }

    public void removerServico(int indice) {
        reciboAtual.removerServico(indice);
    }

    public Recibo getReciboAtual() {
        return reciboAtual;
    }

    public boolean finalizarReciboEGerarPDF() {
        if (reciboAtual.estaVazio()) {
            return false;
        }

        int idRecibo = reciboDAO.salvarRecibo(reciboAtual);
        if (idRecibo == -1) {
            return false;
        }

        reciboAtual.setId(idRecibo);
        reciboDAO.salvarItensRecibo(idRecibo, reciboAtual);
        GeradorReciboPDF.gerar(reciboAtual);

        reciboAtual = new Recibo();
        return true;
    }
}