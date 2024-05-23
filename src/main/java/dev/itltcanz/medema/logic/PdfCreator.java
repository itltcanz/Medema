package dev.itltcanz.medema.logic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.itltcanz.medema.entity.Scan;
import dev.itltcanz.medema.javafx.Controller;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings({"SameParameterValue", "FieldCanBeLocal"})
public class PdfCreator {

    private final Controller controller;
    private Document document;
    private Font font;
    private Font boldFont;

    public PdfCreator(Controller controller) {
        this.controller = controller;
    }

    public void createReport() {
        try {
            String dir = getFileDirectory();
            document = getDocument(dir);
            font = getFont("font/Heuristica-Regular_0.otf");
            boldFont = getFont("font/Heuristica-Bold_0.otf");
            Paragraph header = createHeader("ОАО Ейский портовый элеватор\n\n");
            Paragraph title = createTitle("Отчет");
            Paragraph signature = createSignature("по сканам металлодетекторов\n\n\n");
            PdfPTable table = createTable(controller.tableViewAllTime);
            document.add(header);
            document.add(title);
            document.add(signature);
            document.add(table);
            document.close();
            sendDoneInfo();
        } catch (DocumentException | IOException e) {
            String headerText = "Ошибка создания документа!";
            String contentText = "Невозможно открыть файл.";
            Platform.runLater(() -> controller.viewError(headerText, contentText));
        } catch (NullPointerException e) {
            String headerText = "Ошибка создания документа!";
            String contentText = "Файл отчета указан неверно.";
            Platform.runLater(() -> controller.viewError(headerText, contentText));
        }
    }

    private String getFileDirectory() throws NullPointerException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*pdf"));
        File dir = fileChooser.showSaveDialog(new Stage());
        String dest = dir.getAbsolutePath();
        return dest.contains(".pdf") ? dest : dest + ".pdf";
    }

    private Document getDocument(String dir) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dir));
        document.open();
        return document;
    }

    private Font getFont(String fontDir) throws DocumentException, IOException {
        BaseFont baseFont;
        baseFont = BaseFont.createFont(fontDir, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return new Font(baseFont);
    }

    private Paragraph createHeader(String text) {
        Paragraph header = new Paragraph(text, font);
        header.setAlignment(Element.ALIGN_RIGHT);
        return header;
    }

    private Paragraph createTitle(String text) {
        Paragraph title = new Paragraph(text, boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        return title;
    }

    private Paragraph createSignature(String text) {
        Paragraph title = new Paragraph(text, font);
        title.setAlignment(Element.ALIGN_CENTER);
        return title;
    }

    private PdfPTable createTable(TableView<Scan> tableView) {
        PdfPTable table = new PdfPTable(tableView.getColumns().size());
        for (TableColumn<Scan, ?> column : tableView.getColumns()) {
            addCustomCell(table, column.getText(), font);
        }
        for (Scan scan : tableView.getItems()) {
            addCustomCell(table, scan.getModule().getId(), font);
            addCustomCell(table, scan.getModule().getLocation(), font);
            addCustomCell(table, String.valueOf(scan.getMetal()), font);
            addCustomCell(table, scan.getTimeString(), font);
        }
        return table;
    }

    private void addCustomCell(PdfPTable table, String property, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(property, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void sendDoneInfo() {
        Platform.runLater(() -> controller.viewInfo("Документ создан!",
                "Отчет успешно сформирован!"));
    }
}
