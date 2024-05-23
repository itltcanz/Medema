package dev.xotdoge.medema.javafx;

import dev.xotdoge.medema.controls.LimitedTextFlow;
import dev.xotdoge.medema.entity.Scan;
import dev.xotdoge.medema.logic.Page;
import dev.xotdoge.medema.logic.PdfCreator;
import dev.xotdoge.medema.controls.ScanRowFactory;
import dev.xotdoge.medema.services.ScanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("DuplicatedCode")
public class Controller {
    private final int DEFAULT_PAGE_NUMBER = 0;
    private final int DEFAULT_PAGE_SIZE = 40;
    private int pageNumberAllTime;
    private int pageNumberToday;
    private boolean isFiltered;
    @FXML
    public VBox moduleStatusContainer;
    private final Map<String, ImageView> moduleStatusIndicators = new HashMap<>();
    @FXML
    public Tab tabAllTime;
    @FXML
    public Label recordsNumber;
    @FXML
    public DateTimePicker fromDateTimePicker;
    @FXML
    public DateTimePicker toDateTimePicker;
    @FXML
    public TextField filterTextField;
    @FXML
    private TableView<Scan> tableViewToday;
    @FXML
    private TableColumn<Scan, String> idColumnToday;
    @FXML
    private TableColumn<Scan, String> locationColumnToday;
    @FXML
    private TableColumn<Scan, Byte> metalColumnToday;
    @FXML
    private TableColumn<Scan, String> timeColumnToday;
    @FXML
    public TableView<Scan> tableViewAllTime;
    @FXML
    private TableColumn<Scan, String> idColumnAllTime;
    @FXML
    private TableColumn<Scan, String> locationColumnAllTime;
    @FXML
    private TableColumn<Scan, Byte> metalColumnAllTime;
    @FXML
    private TableColumn<Scan, String> timeColumnAllTime;
    @FXML
    public LimitedTextFlow debugArea;

    @FXML
    public void initialize() {
        // Инициализация таблиц
        initializeTableToday();
        initializeTableAllTime();
        // Заполнение таблиц
        setStartPageToday();
        setStartPageAllTime();
        // Обработчик ленивой загрузки
        tableViewToday.setOnScroll(this::lazyScrollToday);
        tableViewAllTime.setOnScroll(this::lazyScrollAllTime);
    }

    private void initializeTableToday() {
        tableViewToday.setRowFactory(new ScanRowFactory());
        // Определение колонок таблицы "Сегодня"
        idColumnToday.setCellValueFactory(new PropertyValueFactory<>("moduleId"));
        locationColumnToday.setCellValueFactory(new PropertyValueFactory<>("location"));
        metalColumnToday.setCellValueFactory(new PropertyValueFactory<>("metal"));
        timeColumnToday.setCellValueFactory(new PropertyValueFactory<>("timeString"));
    }

    private void initializeTableAllTime() {
        tableViewAllTime.setRowFactory(new ScanRowFactory());
        // Определение колонок таблицы "Все время"
        idColumnAllTime.setCellValueFactory(new PropertyValueFactory<>("moduleId"));
        locationColumnAllTime.setCellValueFactory(new PropertyValueFactory<>("location"));
        metalColumnAllTime.setCellValueFactory(new PropertyValueFactory<>("metal"));
        timeColumnAllTime.setCellValueFactory(new PropertyValueFactory<>("timeString"));
    }

    @FXML
    private void lazyScrollToday(ScrollEvent event) {
        // Проверяем только прокрутку вниз
        if (event.getDeltaY() < 0) {
            int lastIndex = (pageNumberToday == 0 ? 1 : pageNumberToday + 1) * DEFAULT_PAGE_SIZE - 1;
            setNextPageToday(); // Обновляем данные
            tableViewToday.scrollTo(lastIndex); // Прокручиваем к концу таблицы
        }
    }

    @FXML
    private void lazyScrollAllTime(ScrollEvent event) {
        // Проверяем состояние фильтра
        if (!isFiltered) {
            // Проверяем только прокрутку вниз
            if (event.getDeltaY() < 0) {
                int lastIndex = (pageNumberAllTime == 0 ? 1 : pageNumberAllTime + 1) * DEFAULT_PAGE_SIZE - 1;
                setNextPageAllTime();
                tableViewAllTime.scrollTo(lastIndex);
            }
        }
    }

    @FXML
    public void setStartPageToday() {
        pageNumberToday = DEFAULT_PAGE_NUMBER;
        Page page = new Page(pageNumberToday, DEFAULT_PAGE_SIZE);
        List<Scan> newData = ScanService.getScansForToday(page);
        tableViewToday.setItems(FXCollections.observableList(newData));
    }

    @FXML
    public void setNextPageToday() {
        pageNumberToday++;
        Page page = new Page(pageNumberToday, DEFAULT_PAGE_SIZE);
        ObservableList<Scan> oldData = tableViewToday.getItems();
        List<Scan> newData = ScanService.getScansForToday(page);
        oldData.addAll(newData);
        tableViewToday.setItems(oldData);
    }

    @FXML
    public void setStartPageAllTime() {
        pageNumberAllTime = DEFAULT_PAGE_NUMBER;
        Page page = new Page(pageNumberAllTime, DEFAULT_PAGE_SIZE);
        List<Scan> data = ScanService.getScansForAllTime(page);
        tableViewAllTime.setItems(FXCollections.observableList(data));
        recordsNumber.setText(Integer.toString(data.size()));
    }

    @FXML
    public void setNextPageAllTime() {
        pageNumberAllTime++;
        Page page = new Page(pageNumberAllTime, DEFAULT_PAGE_SIZE);
        ObservableList<Scan> oldData = tableViewAllTime.getItems();
        List<Scan> newData = ScanService.getScansForAllTime(page);
        oldData.addAll(newData);
        tableViewAllTime.setItems(oldData);
        recordsNumber.setText(Integer.toString(oldData.size()));
    }

    @FXML
    public ObservableList<Scan> allFilter(LocalDateTime fromDateTime, LocalDateTime toDateTime, String string) {
        List<Scan> data = ScanService.getScansByParameters(fromDateTime, toDateTime, string);
        recordsNumber.setText(Integer.toString(data.size()));
        return FXCollections.observableArrayList(data);
    }

    @FXML
    public ObservableList<Scan> dateFilter(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        List<Scan> data = ScanService.getScansInTime(fromDateTime, toDateTime);
        recordsNumber.setText(Integer.toString(data.size()));
        return FXCollections.observableArrayList(data);
    }

    @FXML
    public ObservableList<Scan> objectFilter(String string) {
        List<Scan> data = ScanService.getScansByString(string);
        recordsNumber.setText(Integer.toString(data.size()));
        return FXCollections.observableArrayList(data);
    }

    @FXML
    public void clearFilter() {
        isFiltered = false;
        tableViewAllTime.scrollTo(DEFAULT_PAGE_NUMBER);
        setStartPageAllTime();
        fromDateTimePicker.setDateTimeValue(null);
        toDateTimePicker.setDateTimeValue(null);
        filterTextField.clear();
    }

    @FXML
    public void filterData() {
        String fromString = fromDateTimePicker.getEditor().getText();
        String toString = toDateTimePicker.getEditor().getText();
        LocalDateTime fromDateTime = fromDateTimePicker.getDateTimeValue();
        LocalDateTime toDateTime = toDateTimePicker.getDateTimeValue();
        String string = filterTextField.getText();
        if (!fromString.equals("") && !toString.equals("") && !string.equals("")) {
            ObservableList<Scan> data = allFilter(fromDateTime, toDateTime, string);
            tableViewAllTime.setItems(data);
            tableViewAllTime.scrollTo(DEFAULT_PAGE_NUMBER);
            isFiltered = true;
        }
        if (!fromString.equals("") && !toString.equals("") && string.equals("")) {
            ObservableList<Scan> data = dateFilter(fromDateTime, toDateTime);
            tableViewAllTime.setItems(data);
            tableViewAllTime.scrollTo(DEFAULT_PAGE_NUMBER);
            isFiltered = true;
        }
        if (fromString.equals("") && toString.equals("") && !string.equals("")) {
            ObservableList<Scan> data = objectFilter(string);
            tableViewAllTime.setItems(data);
            tableViewAllTime.scrollTo(DEFAULT_PAGE_NUMBER);
            isFiltered = true;
        }
        if (fromString.equals("") && toString.equals("") && string.equals("")) {
            setStartPageAllTime();
            tableViewAllTime.scrollTo(DEFAULT_PAGE_NUMBER);
        }
    }
    @FXML
    public void createReport() {
        PdfCreator pdfCreator = new PdfCreator(this);
        pdfCreator.createReport();
    }

    public void addModuleStatus(String id, String location) {
        ImageView imageView = new ImageView(new Image("/image/offline.png"));
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        Label label = new Label(location);
        Region region = new Region();
        moduleStatusContainer.getChildren().addAll(imageView, label, region);
        moduleStatusIndicators.put(id, imageView);
    }

    @FXML
    public void setOnlineStatus(String modelId) {
        ImageView imageView = moduleStatusIndicators.get(modelId);
        if (imageView != null) {
            imageView.setImage(new Image("/image/online.png"));
        }
    }
    @FXML
    public void setOfflineStatus(String modelId) {
        ImageView imageView = moduleStatusIndicators.get(modelId);
        if (imageView != null) {
            imageView.setImage(new Image("/image/offline.png"));
        }
    }

    @FXML
    public void addInfoToDebug(String text) {
        debugArea.appendInfoText(text);
    }

    @FXML
    public void addErrorToDebug(String text) {
        debugArea.appendErrorText(text);
    }

    @FXML
    public void viewError(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/metal-detector.png"));
        alert.setTitle("Ошибка!");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

    @FXML
    public void viewInfo(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/metal-detector.png"));
        alert.setTitle("Информация");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }
}