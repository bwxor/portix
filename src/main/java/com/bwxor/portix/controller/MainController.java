package com.bwxor.portix.controller;

import com.bwxor.portix.MainApplication;
import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;
import com.bwxor.portix.service.scan.QueueScanService;
import com.bwxor.portix.service.scan.QueueScanner;
import com.bwxor.portix.service.scan.impl.InetSocketQueueScanner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainController {
    private IPAddress from;
    private IPAddress to;
    private List<Port> filter;
    private int timeout;

    private QueueScanService scanService;
    private QueueScanner queueScanner;
    private long noEntriesToScan;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane root;
    @FXML
    private Rectangle clipRect;

    @FXML
    private Pane topPane;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private TextField searchTextField;

    @FXML
    private Button scanButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableView<ScanResult> resultTableView;

    @FXML
    private TableColumn<IPAddress, String> ipAddressColumn;
    @FXML
    private TableColumn<Port, String> portColumn;
    @FXML
    private TableColumn<Port, String> statusColumn;
    @FXML
    private ProgressBar progressBar;
    private String searchExpression;
    private List<ScanResult> scanResults;
    private ObservableList<ScanResult> observableList;

    @FXML
    private void initialize() {
        clipRect.widthProperty().bind(root.widthProperty());
        clipRect.heightProperty().bind(root.heightProperty());

        from = new IPAddress("192.168.1.1");
        to = new IPAddress("192.168.1.254");

        scanResults = new ArrayList<>();
        searchExpression = "";
        observableList = FXCollections.observableArrayList();
        resultTableView.setItems(observableList);

        // ToDo: Keep in a separate resources file
        filter = List.of(
                new Port(20),
                new Port(21),
                new Port(22),
                new Port(23),
                new Port(25),
                new Port(53),
                new Port(69),
                new Port(80),
                new Port(110),
                new Port(123),
                new Port(137),
                new Port(138),
                new Port(139),
                new Port(143),
                new Port(161),
                new Port(162),
                new Port(389),
                new Port(443),
                new Port(445),
                new Port(587),
                new Port(636),
                new Port(993),
                new Port(995),
                new Port(1433),
                new Port(3306),
                new Port(3389),
                new Port(5432),
                new Port(5900),
                new Port(8080)
        );

        timeout = 1000;

        stopButton.setDisable(true);
        clearButton.setDisable(true);

        resultTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        var effectiveWidth = resultTableView.widthProperty().subtract(2);

        ipAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        ipAddressColumn.setStyle("-fx-alignment: CENTER;");
        ipAddressColumn.prefWidthProperty().bind(effectiveWidth.multiply(0.4));

        portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        portColumn.prefWidthProperty().bind(effectiveWidth.multiply(0.2));
        portColumn.setStyle("-fx-alignment: CENTER;");

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.prefWidthProperty().bind(effectiveWidth.multiply(0.4));
        statusColumn.setStyle("-fx-alignment: CENTER;");
    }

    @FXML
    public void onScanButtonClick() {
        scanButton.setDisable(true);

        progressBar.setProgress(0);
        noEntriesToScan = (to.diff(from) + 1) * filter.size();

        scanResults.clear();

        queueScanner = new InetSocketQueueScanner();
        scanService = new QueueScanService();
        scanService.setQueueScanner(queueScanner);

        ExecutorService accumulator = Executors.newSingleThreadExecutor();
        Future<?> future = accumulator.submit(
                () -> scanService.scan(new IPAddress(from.toString()), new IPAddress(to.toString()), new ArrayList<>(filter), timeout)
        );

        ScheduledExecutorService poller = Executors.newSingleThreadScheduledExecutor();
        poller.scheduleAtFixedRate(
                () -> {
                    int pollerWaitCounter = 0;

                    var queue = scanService.getScanQueue();

                    // Solve race condition where queue is not initialized by the accumulator thread on time
                    while (queue == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        // Additional safety measure: If the queue doesn't get initialized within 3 seconds, stop the polling
                        if (pollerWaitCounter++ == 10) {
                            scanService.stop();
                            break;
                        }
                    }

                    while (queue != null && !queue.isEmpty() && !scanService.isStopped()) {
                        ScanResult sr = queue.poll();
                        scanResults.add(sr);

                        if (matchesSearchFilter(sr.getIpAddress())) {
                            observableList.add(sr);
                        }

                        progressBar.setProgress((double) scanResults.size() / noEntriesToScan);

                        if (clearButton.isDisabled()) {
                            clearButton.setDisable(false);
                        }

                        if (stopButton.isDisabled()) {
                            stopButton.setDisable(false);
                        }
                    }

                    if (scanService.isStopped() || queue == null || (queue.isEmpty() && future.isDone())) {
                        accumulator.shutdown();
                        poller.shutdown();
                        Platform.runLater(() -> {
                            scanButton.setDisable(false);
                            stopButton.setDisable(true);
                        });
                    }
                }, 1500, 100, TimeUnit.MILLISECONDS
        );
    }

    @FXML
    public void onStopButtonClick() {
        scanService.stop();
        stopButton.setDisable(true);
    }

    @FXML
    public void onClearButtonClick() {
        resultTableView.getItems().clear();
        clearButton.setDisable(true);
    }

    @FXML
    public void onPreferencesButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("preferences-view.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Preferences");

        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/icon.png")));

        stage.setResizable(false);

        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(MainApplication.class.getResource("css/onyx.css").toExternalForm());

        PreferencesController preferencesController = fxmlLoader.getController();
        preferencesController.setFrom(from);
        preferencesController.setTo(to);
        preferencesController.setFilter(filter);
        preferencesController.setTimeout(timeout);
        preferencesController.manuallyInit();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        stage.showAndWait();

        from = preferencesController.getFrom();
        to = preferencesController.getTo();
        filter = preferencesController.getFilter();
        timeout = preferencesController.getTimeout();
    }

    @FXML
    public void onCloseButtonClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onMinimizeButtonClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void handleClickAction(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @FXML
    public void handleMovementAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @FXML
    public void handleSearchTextFieldOnKeyTyped(KeyEvent keyEvent) {
        searchExpression = searchTextField.getText().trim();

        observableList.setAll(scanResults
                .stream()
                .filter(e -> matchesSearchFilter(e.getIpAddress()))
                .toList());
    }

    private boolean matchesSearchFilter(IPAddress ipAddress) {
        String ip = ipAddress.toString();

        if (searchExpression.isEmpty() || searchExpression.equals("*")) {
            return true;
        }

        if (searchExpression.startsWith("*") && searchExpression.endsWith("*")) {
            return ip.contains(searchExpression.substring(1, searchExpression.length() - 1));
        } else if (searchExpression.endsWith("*")) {
            return ip.startsWith(searchExpression.substring(0, searchExpression.length() - 1));
        } else if (searchExpression.startsWith("*")) {
            return ip.endsWith(searchExpression.substring(1));
        } else {
            return ip.equals(searchExpression);
        }
    }
}
