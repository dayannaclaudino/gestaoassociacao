package com.api.gestaoassociacao.service.Pdf;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Dependente;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
 
 
public class AssociadoPDFExporter {
    private List<Associado> listAssociado;
     
   public AssociadoPDFExporter(List<Associado> listAssociado) {
        this.listAssociado = listAssociado;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);

        String[] headers = {"ID", "Nome", "Apelido", "Cpf", "Data Nasc", "Rg", "Nis", "Celular", "Sócio Desde"};

        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Associado associado : listAssociado) {
            // Dados do Associado
            table.addCell(String.valueOf(associado.getId()));
            table.addCell(associado.getNome());
            table.addCell(associado.getApelido());
            table.addCell(String.valueOf(associado.getCpf()));
            table.addCell(associado.getDataNascimento() != null ? associado.getDataNascimento().format(formatter) : "Não Informado");
            table.addCell(String.valueOf(associado.getRg()));
            table.addCell(String.valueOf(associado.getNis()));
            table.addCell(String.valueOf(associado.getCelular()));
            table.addCell(String.valueOf(associado.getSocioDesde()));

            // Se houver dependentes, adicioná-los abaixo do associado
            List<Dependente> dependentes = associado.getDependentes();
            if (dependentes != null && !dependentes.isEmpty()) {
                PdfPCell dependentesHeader = new PdfPCell(new Phrase("Dependentes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
                dependentesHeader.setBackgroundColor(BaseColor.GRAY);
                dependentesHeader.setColspan(9); // Mescla todas as colunas
                table.addCell(dependentesHeader);

                for (Dependente dependente : dependentes) {
                    PdfPCell emptyCell = new PdfPCell();
                    emptyCell.setBackgroundColor(BaseColor.WHITE); // Cor de fundo para células vazias
                    emptyCell.setBorder(Rectangle.NO_BORDER); // Remove a borda para deixar mais visualmente agradável
                    
                    table.addCell(emptyCell); // ID vazio

                    PdfPCell dependenteCell = new PdfPCell(new Phrase(" → " + dependente.getNome()));
                    dependenteCell.setBackgroundColor(new BaseColor(230, 230, 250)); // Cor de fundo (Lavender)
                    table.addCell(dependenteCell); 

                    PdfPCell parenteCell = new PdfPCell(new Phrase(" → " + dependente.getParentesco()));
                    parenteCell.setBackgroundColor(new BaseColor(230, 230, 250)); // Cor de fundo (Lavender)
                    table.addCell(parenteCell);

                    table.addCell(emptyCell); // Apelido vazio
                    table.addCell(emptyCell); // CPF vazio
                    table.addCell(emptyCell); // RG vazio
                    table.addCell(emptyCell); // NIS vazio
                    table.addCell(emptyCell); // Celular vazio
                    table.addCell(emptyCell); // Sócio Desde vazio
                }
            }
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(BaseColor.BLACK);

        Paragraph p = new Paragraph("Lista de Associados", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        p.setSpacingAfter(10);
        document.add(p);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.0f, 4.0f, 2.0f, 3.0f, 2.5f, 2.0f, 2.0f, 3.0f, 2.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }
}
