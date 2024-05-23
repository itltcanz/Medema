package dev.itltcanz.medema.controls;

import dev.itltcanz.medema.entity.Scan;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ScanRowFactory implements Callback<TableView<Scan>, TableRow<Scan>> {
    @Override
    public TableRow<Scan> call(TableView<Scan> param) {
        return new TableRow<>() {
            @Override
            protected void updateItem(Scan item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getMetal() == 1) {
                        setStyle("-fx-background-color: #f2564d");
                    } else {
                        setStyle("");
                    }
                }
            }
        };
    }
}
