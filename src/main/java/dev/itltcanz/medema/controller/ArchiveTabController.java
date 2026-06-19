package dev.itltcanz.medema.controller;

import static dev.itltcanz.medema.constant.UiPage.DEFAULT_PAGE_NUMBER;
import static dev.itltcanz.medema.constant.UiPage.DEFAULT_PAGE_SIZE;
import static dev.itltcanz.medema.util.DateFormatterUtil.format;

import dev.itltcanz.medema.control.ScanRowFactory;
import dev.itltcanz.medema.model.entity.Scan;
import dev.itltcanz.medema.services.PdfService;
import dev.itltcanz.medema.services.ScanService;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import tornadofx.control.DateTimePicker;

@SuppressWarnings("DuplicatedCode")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ArchiveTabController {

  private final PdfService pdfService;
  private final ScanService scanService;
  private final ObservableList<Scan> observableList = FXCollections.observableArrayList();
  @FXML
  public TableView<Scan> tableView;
  @FXML
  public TableColumn<Scan, String> idColumn;
  @FXML
  public TableColumn<Scan, String> locationColumn;
  @FXML
  public TableColumn<Scan, Byte> metalColumn;
  @FXML
  public TableColumn<Scan, String> timeColumn;
  @FXML
  public Label recordsNumber;
  @FXML
  public DateTimePicker startDateTimePicker;
  @FXML
  public DateTimePicker endDateTimePicker;
  @FXML
  public TextField filterTextField;
  private int pageNumber;

  @FXML
  public void initialize() {
    // Инициализация таблиц
    initializeTable();
    // Присвоение таблицам списков
    tableView.setItems(observableList);
    // Очистка полей
    startDateTimePicker.setDateTimeValue(null);
    endDateTimePicker.setDateTimeValue(null);
    // Заполнение таблиц
    updateTable();
    // Обработчики
    createLazyScroll();
  }

  private void initializeTable() {
    tableView.setRowFactory(new ScanRowFactory());
    idColumn.setCellValueFactory(new PropertyValueFactory<>("detectorId"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("locationName"));
    metalColumn.setCellValueFactory(new PropertyValueFactory<>("metal"));
    timeColumn.setCellValueFactory(this::createCellValueFactory);
  }

  private SimpleStringProperty createCellValueFactory(CellDataFeatures<Scan, String> cellData) {
    LocalDateTime time = cellData.getValue().getTime();
    String formattedTime = format(time);
    return new SimpleStringProperty(formattedTime);
  }

  @FXML
  private void createLazyScroll() {
    tableView.setOnScroll(event -> {
      // Проверяем только прокрутку вниз
      if (event.getDeltaY() < 0) {
        int lastIndex = (pageNumber == 0 ? 1 : pageNumber + 1) * DEFAULT_PAGE_SIZE - 1;
        setNextPage();
        tableView.scrollTo(lastIndex);
      }
    });
  }

  @FXML
  public void clearFilter() {
    tableView.scrollTo(DEFAULT_PAGE_NUMBER);
    updateTable();
    startDateTimePicker.setDateTimeValue(null);
    endDateTimePicker.setDateTimeValue(null);
    filterTextField.clear();
  }

  @FXML
  public void setNextPage() {
    pageNumber++;
    LocalDateTime start = startDateTimePicker.getDateTimeValue();
    LocalDateTime end = endDateTimePicker.getDateTimeValue();
    String param = filterTextField.getText();
    Page page = Page.page(DEFAULT_PAGE_SIZE, pageNumber);
    List<Scan> newData = scanService.findScansWithFilters(start, end, param, page);
    ObservableList<Scan> oldData = tableView.getItems();
    oldData.addAll(newData);
    tableView.setItems(oldData);
    recordsNumber.setText(Integer.toString(oldData.size()));
  }

  @FXML
  public void updateTable() {
    pageNumber = DEFAULT_PAGE_NUMBER;
    LocalDateTime start = startDateTimePicker.getDateTimeValue();
    LocalDateTime end = endDateTimePicker.getDateTimeValue();
    String param = filterTextField.getText();
    Page page = Page.page(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER);
    List<Scan> scans = scanService.findScansWithFilters(start, end, param, page);
    observableList.setAll(scans);
    tableView.scrollTo(DEFAULT_PAGE_NUMBER);
    recordsNumber.setText(Integer.toString(scans.size()));
  }

  @FXML
  public void createReport() {
    pdfService.createReport(tableView);
  }
}
