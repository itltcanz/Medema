package dev.itltcanz.medema.controller;

import dev.itltcanz.medema.services.UiStateService;
import dev.itltcanz.medema.model.dto.detector.CreateDetectorDto;
import dev.itltcanz.medema.model.dto.detector.DeleteDetectorDto;
import dev.itltcanz.medema.model.dto.detector.UpdateDetectorDto;
import dev.itltcanz.medema.model.entity.Detector;
import dev.itltcanz.medema.model.entity.Location;
import dev.itltcanz.medema.services.DetectorService;
import jakarta.inject.Inject;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class DetectorTabController {

  private final ObservableList<Detector> detectorObservableList = FXCollections.observableArrayList();
  private final DetectorService detectorService;
  private final UiStateService uiDataConfig;
  @FXML
  public TableView<Detector> detectorTableView;
  @FXML
  public TableColumn<Detector, String> detectorIdColum;
  @FXML
  public TableColumn<Detector, String> detectorIpColum;
  @FXML
  public TableColumn<Detector, String> detectorPortColum;
  @FXML
  public TableColumn<Detector, String> detectorLocationColum;
  @FXML
  public TextField detectorIdTextField;
  @FXML
  public TextField detectorIpTextField;
  @FXML
  public TextField detectorPortTextField;
  @FXML
  public ComboBox<Location> detectorLocationComboBox;

  @FXML
  public void initialize() {
    // Инициализация таблиц
    initializeDetectorTable();
    // Присвоение таблицам списков
    detectorTableView.setItems(detectorObservableList);
    detectorLocationComboBox.setItems(uiDataConfig.getLocations());
    // Заполнение таблиц
    updateDetectorTable();
    // Создание обработчиков
    createDetectorTableViewListener();
    createDetectorLocationComboBoxListener();
  }

  private void initializeDetectorTable() {
    detectorIdColum.setCellValueFactory(new PropertyValueFactory<>("id"));
    detectorIpColum.setCellValueFactory(new PropertyValueFactory<>("ip"));
    detectorPortColum.setCellValueFactory(new PropertyValueFactory<>("port"));
    detectorLocationColum.setCellValueFactory(new PropertyValueFactory<>("locationName"));
  }

  private void createDetectorTableViewListener() {
    detectorTableView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldSelection, newSelection) -> {
          if (newSelection != null) {
            fillDetectorFields(newSelection);
          } else {
            clearDetectorInputs();
            detectorTableView.getSelectionModel().clearSelection();
          }
        }
    );
    detectorTableView.setRowFactory(tv -> {
      TableRow<Detector> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (row.isEmpty()) {
          detectorTableView.getSelectionModel().clearSelection();
        }
      });
      return row;
    });
  }

  private void createDetectorLocationComboBoxListener() {
    detectorLocationComboBox.setConverter(new javafx.util.StringConverter<>() {
      @Override
      public String toString(Location location) {
        return location == null ? null : location.getName();
      }

      @Override
      public Location fromString(String string) {
        // Метод не используется, так как ComboBox только для чтения
        return null;
      }
    });
  }


  private void fillDetectorFields(Detector detector) {
    detectorIdTextField.setText(detector.getId());
    detectorIpTextField.setText(detector.getIp());
    detectorPortTextField.setText(detector.getPort());
    detectorLocationComboBox.getSelectionModel().select(detector.getLocation());
  }

  public void createDetector() {
    CreateDetectorDto detectorDto = CreateDetectorDto.builder()
        .id(detectorIdTextField.getText())
        .ip(detectorIpTextField.getText())
        .port(detectorPortTextField.getText())
        .location(detectorLocationComboBox.getSelectionModel().getSelectedItem())
        .build();
    detectorService.create(detectorDto);
    updateDetectorTable();
  }

  public void updateDetector() {
    var detectorDto = UpdateDetectorDto.builder()
        .id(detectorIdTextField.getText())
        .ip(detectorIpTextField.getText())
        .port(detectorPortTextField.getText())
        .location(detectorLocationComboBox.getSelectionModel().getSelectedItem())
        .build();
    detectorService.update(detectorDto);
    updateDetectorTable();
  }

  public void deleteDetector() {
    var detectorDto = DeleteDetectorDto.builder()
        .id(detectorIdTextField.getText())
        .build();
    detectorService.delete(detectorDto);
    clearDetectorInputs();
    updateDetectorTable();
  }

  public void updateDetectorTable() {
    List<Detector> detectors = detectorService.findAll();
    detectorObservableList.setAll(detectors);
  }

  public void clearDetectorInputs() {
    detectorIdTextField.clear();
    detectorIpTextField.clear();
    detectorPortTextField.clear();
    detectorLocationComboBox.getSelectionModel().clearSelection();
  }
} 