package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.Recibo;
import model.Servico;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeradorReciboPDF {

    public static void gerar(Recibo recibo) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            Path desktop = Paths.get(System.getProperty("user.home"), "Desktop");

            if (!Files.exists(desktop)) {
                desktop = Paths.get(System.getProperty("user.home"), "OneDrive", "Desktop");
            }

            String nomeArquivo = desktop.resolve("recibo_" + recibo.getId() + ".pdf").toString();

            System.out.println("Arquivo final: " + nomeArquivo);

            PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font textoFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            Font destaqueFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);

            adicionarTitulo(document, tituloFont);
            adicionarInformacoesLoja(document, recibo, subtituloFont, textoFont);
            adicionarDadosClienteSeExistirem(document, recibo, subtituloFont, textoFont);
            adicionarTabelaServicos(document, recibo, textoFont);
            adicionarTotal(document, recibo, destaqueFont);
            adicionarAssinatura(document, textoFont);

        } catch (Exception e) {
            System.out.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static void adicionarTitulo(Document document, Font tituloFont) throws DocumentException {
        Paragraph titulo = new Paragraph("RECIBO DE SERVIÇOS", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(15);
        document.add(titulo);
    }

    private static void adicionarInformacoesLoja(Document document, Recibo recibo, Font subtituloFont, Font textoFont)
            throws DocumentException {

        Paragraph infoLoja = new Paragraph();
        infoLoja.add(new Chunk("Chaveiro Palheta\n", subtituloFont));
        infoLoja.add(new Chunk("CNPJ: 11.914.045/0001-98\n", textoFont));
        infoLoja.add(new Chunk("Telefone: (91) 98141-9599 / 98212-6504\n", textoFont));
        infoLoja.add(new Chunk("Data/Hora: " + recibo.getDataHora() + "\n", textoFont));
        infoLoja.add(new Chunk("Número do recibo: " + recibo.getId() + "\n", textoFont));
        infoLoja.setSpacingAfter(15);

        document.add(infoLoja);
    }

    private static void adicionarDadosClienteSeExistirem(Document document, Recibo recibo, Font subtituloFont, Font textoFont)
            throws DocumentException {

        boolean temEmpresa = recibo.getNomeEmpresa() != null && !recibo.getNomeEmpresa().isBlank();
        boolean temCnpj = recibo.getCnpj() != null && !recibo.getCnpj().isBlank();
        boolean temEndereco = recibo.getEndereco() != null && !recibo.getEndereco().isBlank();

        if (!temEmpresa && !temCnpj && !temEndereco) {
            return;
        }

        Paragraph dadosCliente = new Paragraph();
        dadosCliente.add(new Chunk("Dados do cliente\n", subtituloFont));

        if (temEmpresa) {
            dadosCliente.add(new Chunk("Empresa: " + recibo.getNomeEmpresa() + "\n", textoFont));
        }

        if (temCnpj) {
            dadosCliente.add(new Chunk("CNPJ: " + recibo.getCnpj() + "\n", textoFont));
        }

        if (temEndereco) {
            dadosCliente.add(new Chunk("Endereço: " + recibo.getEndereco() + "\n", textoFont));
        }

        dadosCliente.setSpacingAfter(15);
        document.add(dadosCliente);
    }

    private static void adicionarTabelaServicos(Document document, Recibo recibo, Font textoFont)
            throws DocumentException {

        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setSpacingBefore(5);
        tabela.setSpacingAfter(10);
        tabela.setWidths(new float[]{4f, 2f, 2f, 2f});

        adicionarCabecalho(tabela, "Serviço");
        adicionarCabecalho(tabela, "Quantidade");
        adicionarCabecalho(tabela, "Preço");
        adicionarCabecalho(tabela, "Total");

        for (Servico servico : recibo.getItens()) {
            adicionarCelulaCorpo(tabela, servico.getNome(), textoFont, Element.ALIGN_LEFT);
            adicionarCelulaCorpo(tabela, String.valueOf(servico.getQuantidade()), textoFont, Element.ALIGN_CENTER);
            adicionarCelulaCorpo(tabela, String.format("R$ %.2f", servico.getPreco()), textoFont, Element.ALIGN_CENTER);
            adicionarCelulaCorpo(tabela, String.format("R$ %.2f", servico.getValorTotal()), textoFont, Element.ALIGN_CENTER);
        }

        document.add(tabela);
    }

    private static void adicionarTotal(Document document, Recibo recibo, Font destaqueFont)
            throws DocumentException {

        Paragraph total = new Paragraph(
                "Total geral: R$ " + String.format("%.2f", recibo.getTotalGeral()),
                destaqueFont
        );
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(10);
        document.add(total);
    }

    private static void adicionarAssinatura(Document document, Font textoFont)
            throws DocumentException {

        Paragraph assinatura = new Paragraph(
                "\n\n____________________________________\nAssinatura",
                textoFont
        );
        assinatura.setAlignment(Element.ALIGN_CENTER);
        assinatura.setSpacingBefore(30);
        document.add(assinatura);
    }

    private static void adicionarCabecalho(PdfPTable tabela, String texto) {
        Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(8);
        tabela.addCell(cell);
    }

    private static void adicionarCelulaCorpo(PdfPTable tabela, String texto, Font font, int alinhamento) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(alinhamento);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        tabela.addCell(cell);
    }
}