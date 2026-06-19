package dev.itltcanz.medema.controller;

import dev.itltcanz.medema.services.UiStateService;
import dev.itltcanz.medema.model.dto.location.CreateLocationDto;
import dev.itltcanz.medema.model.dto.location.DeleteLocationDto;
import dev.itltcanz.medema.model.dto.location.UpdateLocationDto;
import dev.itltcanz.medema.model.entity.Location;
import dev.itltcanz.medema.services.LocationService;
import jakarta.inject.Inject;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class LocationTabController {

  private final LocationService locationService;
  private final UiStateService uiDataConfig;
  @FXML
  public TableView<Location> locationTableView;
  @FXML
  public TableColumn<Location, Long> locationIdColum;
  @FXML
  public TableColumn<Location, String> locationNameColum;
  @FXML
  public TextField locationIdTextField;
  @FXML
  public TextField locationNameTextField;

  @FXML
  public void initialize() {
    // Инициализация таблиц
    initializeLocationTable();
    // Присвоение таблицам списков
    locationTableView.setItems(uiDataConfig.getLocations());
    // Заполнение таблиц
    updateLocationTable();
    // Обработчики
    createLocationTableViewListener();
  }

  private void initializeLocationTable() {
    locationIdColum.setCellValueFactory(new PropertyValueFactory<>("id"));
    locationNameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
  }

  public void updateLocationTable() {
    List<Location> locations = locationService.findAll();
    uiDataConfig.getLocations().setAll(locations);
  }

  private void createLocationTableViewListener() {
    locationTableView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldSelection, newSelection) -> {
          if (newSelection != null) {
            fillLocationFields(newSelection);
          } else {
            clearLocationInputs();
            locationTableView.getSelectionModel().clearSelection();
          }
        }
    );
    locationTableView.setRowFactory(tv -> {
      TableRow<Location> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (row.isEmpty()) {
          locationTableView.getSelectionModel().clearSelection();
        }
      });
      return row;
    });
  }

  private void fillLocationFields(Location location) {
    locationIdTextField.setText(location.getId().toString());
    locationNameTextField.setText(location.getName());
  }

  public void clearLocationInputs() {
    locationIdTextField.clear();
    locationNameTextField.clear();
  }

  public void createLocation() {
    var locationDto = CreateLocationDto.builder()
        .name(locationNameTextField.getText())
        .build();
    locationService.create(locationDto);
    updateLocationTable();
  }

  public void updateLocation() {
    var locationDto = UpdateLocationDto.builder()
        .id(locationIdTextField.getText())
        .name(locationNameTextField.getText())
        .build();
    locationService.update(locationDto);
    updateLocationTable();
  }

  public void deleteLocation() {
    var locationDto = DeleteLocationDto.builder()
        .id(locationIdTextField.getText())
        .build();
    locationService.delete(locationDto);
    clearLocationInputs();
    updateLocationTable();
  }

}
