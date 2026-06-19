package dev.itltcanz.medema.controller;

import static dev.itltcanz.medema.util.DateFormatterUtil.format;

import dev.itltcanz.medema.control.ScanRowFactory;
import dev.itltcanz.medema.model.entity.Scan;
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
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TodayTabController {

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 40;

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
  private int pageNumber;

  @FXML
  public void initialize() {
    // Инициализация таблиц
    initializeTable();
    // Присвоение таблицам списков
    tableView.setItems(observableList);
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

  public void updateTable() {
    pageNumber = DEFAULT_PAGE_NUMBER;
    Page page = Page.page(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER);
    List<Scan> scans = scanService.findScansForToday(page);
    observableList.setAll(scans);
    recordsNumber.setText(Integer.toString(scans.size()));
  }

  @FXML
  public void setNextPage() {
    pageNumber++;
    Page page = Page.page(DEFAULT_PAGE_SIZE, pageNumber);
    ObservableList<Scan> oldData = tableView.getItems();
    List<Scan> newData = scanService.findScansForToday(page);
    oldData.addAll(newData);
    tableView.setItems(oldData);
    recordsNumber.setText(Integer.toString(oldData.size()));
  }

  @FXML
  private void createLazyScroll() {
    tableView.setOnScroll(event -> {
      // Проверяем только прокрутку вниз
      if (event.getDeltaY() < 0) {
        int lastIndex = (pageNumber == 0 ? 1 : pageNumber + 1) * DEFAULT_PAGE_SIZE - 1;
        setNextPage(); // Обновляем данные
        tableView.scrollTo(lastIndex); // Прокручиваем к концу таблицы
      }
    });
  }
}
