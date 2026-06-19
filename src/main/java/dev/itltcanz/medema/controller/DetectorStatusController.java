package dev.itltcanz.medema.controller;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class DetectorStatusController {
  private final Map<String, VBox> detectorStatusIndicators = new HashMap<>();
  @FXML
  public VBox detectorStatusContainer;

  public void addDetectorStatus(String id, String location) {
    VBox container = new VBox();
    container.setId(id);
    container.getStyleClass().add("detectorStatus");
    ImageView imageView = new ImageView(new Image("/image/offline.png"));
    imageView.setFitHeight(35);
    imageView.setFitWidth(35);
    Label label = new Label(location);
    Region region = new Region();
    container.getChildren().addAll(imageView, label, region);
    detectorStatusContainer.getChildren().add(container);
    detectorStatusIndicators.put(id, container);
  }

  public void updateDetectorStatus(String id, String location) {
    VBox container = detectorStatusIndicators.get(id);
    Label label = (Label) container.getChildren().get(1);
    label.setText(location);
  }

  public void deleteDetectorStatus(String id) {
    VBox container = detectorStatusIndicators.get(id);
    detectorStatusContainer.getChildren().remove(container);
  }

  @FXML
  public void setOnlineStatus(String detectorId) {
    VBox container = detectorStatusIndicators.get(detectorId);
    ImageView imageView = (ImageView) container.getChildren().getFirst();
    if (imageView != null) {
      imageView.setImage(new Image("/image/online.png"));
    }
  }

  @FXML
  public void setOfflineStatus(String detectorId) {
    VBox container = detectorStatusIndicators.get(detectorId);
    ImageView imageView = (ImageView) container.getChildren().getFirst();
    if (imageView != null) {
      imageView.setImage(new Image("/image/offline.png"));
    }
  }
}
