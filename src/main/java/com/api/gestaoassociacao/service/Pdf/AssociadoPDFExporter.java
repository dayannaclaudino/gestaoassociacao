package com.api.gestaoassociacao.service.Pdf;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.api.gestaoassociacao.model.Associado;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
         
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Nome", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Apelido", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Cpf", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Data Nasc", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Rg", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nis", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Celular", font));
        table.addCell(cell);     
        
        cell.setPhrase(new Phrase("Sócio Desde", font));
        table.addCell(cell);
    }
     
    private void writeTableData(PdfPTable table) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Associado associado : listAssociado) {
            table.addCell(String.valueOf(associado.getId()));
            table.addCell(associado.getNome());
            table.addCell(associado.getApelido());
            table.addCell(String.valueOf(associado.getCpf()));
            table.addCell(String.valueOf(associado.getDataNascimento() != null ? associado.getDataNascimento().format(formatter) : "Não Informado"));
            table.addCell(String.valueOf(associado.getRg()));
            table.addCell(String.valueOf(associado.getNis()));
            table.addCell(String.valueOf(associado.getCelular()));
            table.addCell(String.valueOf(associado.getSocioDesde()));
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
         
        document.add(p);
         
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 4.0f, 2.0f, 3.0f, 2.5f, 2.0f, 2.0f, 3.0f, 2.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
         
        document.close();
         
    }    
}
