package com.bwxor.iport.controller;

import com.bwxor.iport.MainApplication;
import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.entity.ScanResult;
import com.bwxor.iport.service.scan.AbstractQueueScanService;
import com.bwxor.iport.service.scan.MockQueueScanService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainController {
    private IPAddress from;
    private IPAddress to;
    private List<Port> filter;

    private AbstractQueueScanService scanService;
    private boolean scanStopped;

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

    @FXML
    private void initialize() {
        from = new IPAddress("192.168.1.1");
        to = new IPAddress("192.168.1.254");

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
        clearButton.setDisable(false);
        stopButton.setDisable(false);

        progressBar.setProgress(0);

        scanStopped = false;
        resultTableView.setItems(FXCollections.observableArrayList());

        scanService = new MockQueueScanService();

        var scanResults = new ArrayList<ScanResult>();

        ObservableList<ScanResult> data = FXCollections.observableArrayList();

        ExecutorService accumulator = Executors.newSingleThreadExecutor();
        Future<?> future = accumulator.submit(
                () -> scanService.scan(new IPAddress(from.toString()), new IPAddress(to.toString()), new ArrayList<>(filter))
        );

        ScheduledExecutorService poller = Executors.newSingleThreadScheduledExecutor();
        poller.scheduleAtFixedRate(
                () -> {
                    var queue = scanService.getScanQueue();
                    if (queue != null && !queue.isEmpty()) {
                        while (!queue.isEmpty() && !scanStopped) {
                            ScanResult sr = queue.poll();
                            scanResults.add(sr);
                            data.add(sr);
                            resultTableView.setItems(data);

                            if (clearButton.isDisabled()) {
                                clearButton.setDisable(false);
                            }
                        }
                    }
                    if (scanStopped || (queue.isEmpty() && future.isDone())) {
                        accumulator.shutdown();
                        poller.shutdown();
                        Platform.runLater(() -> {
                            scanButton.setDisable(false);
                            stopButton.setDisable(true);
                        });
                    }
                }, 0, 100, TimeUnit.MILLISECONDS
        );
    }

    @FXML
    public void onStopButtonClick() {
        scanStopped = true;
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

        Scene scene = new Scene(fxmlLoader.load());
        PreferencesController preferencesController = fxmlLoader.getController();
        preferencesController.setFrom(from);
        preferencesController.setTo(to);
        preferencesController.setFilter(filter);
        preferencesController.manuallyInit();
        stage.setScene(scene);

        stage.showAndWait();

        from = preferencesController.getFrom();
        to = preferencesController.getTo();
        filter = preferencesController.getFilter();
    }
}
