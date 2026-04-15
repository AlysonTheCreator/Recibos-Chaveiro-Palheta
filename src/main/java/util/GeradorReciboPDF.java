package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.Recibo;
import model.Servico;

import java.io.FileOutputStream;

public class GeradorReciboPDF {

    public static void gerar(Recibo recibo) {
        String nomeArquivo = "recibo_" + recibo.getId() + ".pdf";
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font textoFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

            Paragraph titulo = new Paragraph("RECIBO DE SERVIÇOS", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            Paragraph infoLoja = new Paragraph();
            infoLoja.add(new Chunk("Chaveiro Palheta\n", subtituloFont));
            infoLoja.add(new Chunk("CNPJ: 11.914.045/0001-98\n", textoFont));
            infoLoja.add(new Chunk("Telefone: (91) 98141-9599 / 98212-6504\n", textoFont));
            infoLoja.add(new Chunk("Data/Hora: " + recibo.getDataHora() + "\n", textoFont));
            infoLoja.add(new Chunk("Número do recibo: " + recibo.getId() + "\n", textoFont));
            infoLoja.setSpacingAfter(20);
            document.add(infoLoja);

            PdfPTable tabela = new PdfPTable(4);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{4f, 2f, 2f, 2f});

            adicionarCabecalho(tabela, "Serviço");
            adicionarCabecalho(tabela, "Quantidade");
            adicionarCabecalho(tabela, "Preço");
            adicionarCabecalho(tabela, "Total");

            for (Servico servico : recibo.getItens()) {
                tabela.addCell(servico.getNome());
                tabela.addCell(String.valueOf(servico.getQuantidade()));
                tabela.addCell(String.format("R$ %.2f", servico.getPreco()));
                tabela.addCell(String.format("R$ %.2f", servico.getValorTotal()));
            }

            document.add(tabela);

            Paragraph total = new Paragraph(
                    "Total geral: R$ " + String.format("%.2f", recibo.getTotalGeral()),
                    subtituloFont
            );
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(15);
            document.add(total);

            Paragraph assinatura = new Paragraph(
                    "\n\n____________________________________\nAssinatura",
                    textoFont
            );
            assinatura.setAlignment(Element.ALIGN_CENTER);
            assinatura.setSpacingBefore(30);
            document.add(assinatura);

        } catch (Exception e) {
            System.out.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            document.close();
        }
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
}