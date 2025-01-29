package com.api.gestaoassociacao.service.Pdf;

import com.api.gestaoassociacao.model.Mensalidade;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MensalidadePDFExporter {

    public byte[] gerarRelatorioMensalidades(List<Mensalidade> mensalidades, BigDecimal totalParcelas) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Título do PDF
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Relatório de Mensalidades", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Título do PDF
            DateTimeFormatter formatterdata = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataAtual = "Data: " + LocalDate.now().format(formatterdata);
            Font tituloFontData = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Paragraph tituloData = new Paragraph(dataAtual, tituloFontData);
            titulo.setAlignment(Element.ALIGN_RIGHT);
            titulo.setSpacingAfter(5);
            document.add(tituloData);

            // Título Associacao
            Font tituloFontAssociacao = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph tituloAssociacao = new Paragraph("Associação Quilombola do Castainho", tituloFontAssociacao);
            titulo.setAlignment(Element.ALIGN_RIGHT);
            titulo.setSpacingAfter(5);
            document.add(tituloAssociacao);

            // Tabela
            PdfPTable table = new PdfPTable(9); // Número de colunas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setPadding(5);
            
            Font font = FontFactory.getFont(FontFactory.HELVETICA);
            font.setColor(BaseColor.WHITE);
                  
            // Cabeçalho da tabela
            cell.setPhrase(new Phrase("ID", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Associado", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Emissão", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Parcela", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Vencimento", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Dias Atraso", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Pagamento", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Valor", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Situação", font));
            table.addCell(cell);

            // Conteúdo da tabela
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Mensalidade mensalidade : mensalidades) {
                table.addCell(mensalidade.getId().toString());
                table.addCell(mensalidade.getAssociado().getNome());
                table.addCell(mensalidade.getDataEmissao() != null ? mensalidade.getDataEmissao().format(formatter) : "Não informado");
                table.addCell(String.valueOf(mensalidade.getParcela()));
                table.addCell(mensalidade.getDataVencimento() != null ? mensalidade.getDataVencimento().format(formatter) : "Não informado");
                table.addCell(String.valueOf(mensalidade.getDiasAtraso()));
                table.addCell(mensalidade.getDataPagamento() != null ? mensalidade.getDataPagamento().format(formatter) : "Não informado"
);
                table.addCell("R$ " + mensalidade.getValor().toString());
                table.addCell(mensalidade.getSituacao().toString());
            }

            // Adiciona a tabela ao documento
            document.add(table);

            // Total de parcelas
            Paragraph total = new Paragraph("Total das Parcelas: R$ " + totalParcelas.toString(), tituloFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(20);
            document.add(total);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
    
}
