package dev.itltcanz.medema.services;

import dev.itltcanz.medema.model.entity.Location;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class UiStateService {

  private final ObservableList<Location> locations = FXCollections.observableArrayList();

}