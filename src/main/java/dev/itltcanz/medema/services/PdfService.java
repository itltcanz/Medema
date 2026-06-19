package dev.itltcanz.medema.services;

import static dev.itltcanz.medema.util.DateFormatterUtil.format;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.itltcanz.medema.model.entity.Scan;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@SuppressWarnings({"FieldCanBeLocal", "SameParameterValue"})
public class PdfService {

  private final NotificationService notificationService;
  private Document document;
  private Font font;
  private Font boldFont;

  public void createReport(TableView<Scan> tableView) {
    try {
      renderDocument(tableView);
      notificationService.showInfo("Документ создан", "Отчет успешно сформирован");
    } catch (DocumentException | IOException e) {
      notificationService.showInfo("Ошибка создания документа", "Невозможно открыть файл");
    } catch (NullPointerException e) {
      notificationService.showInfo("Ошибка создания документа", "Файл отчета указан неверно");
    }
  }

  private void renderDocument(TableView<Scan> tableView) throws DocumentException, IOException {
    String dir = getFileDirectory();
    document = getDocument(dir);
    font = getFont("font/Heuristica-Regular_0.otf");
    boldFont = getFont("font/Heuristica-Bold_0.otf");

    Paragraph header = createHeader("ОАО Ейский портовый элеватор\n\n");
    Paragraph title = createTitle("Отчет");
    Paragraph signature = createSignature("по сканам металлодетекторов\n\n\n");
    PdfPTable table = createTable(tableView);
    document.add(header);
    document.add(title);
    document.add(signature);
    document.add(table);
    document.close();
  }

  private String getFileDirectory() throws NullPointerException {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*pdf"));
    File dir = fileChooser.showSaveDialog(new Stage());
    String dest = dir.getAbsolutePath();
    return dest.contains(".pdf") ? dest : dest + ".pdf";
  }

  private Document getDocument(String dir) throws FileNotFoundException, DocumentException {
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
      addCustomCell(table, scan.getDetector().getId(), font);
      addCustomCell(table, scan.getDetector().getLocationName(), font);
      addCustomCell(table, String.valueOf(scan.getMetal()), font);
      addCustomCell(table, format(scan.getTime()), font);
    }
    return table;
  }

  private void addCustomCell(PdfPTable table, String property, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(property, font));
    cell.setPadding(5);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
  }
}
