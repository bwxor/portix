package com.bwxor.iport.controller;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.exception.IPAddressBuildException;
import com.bwxor.iport.util.IntParser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PreferencesController {
    private IPAddress from;
    private IPAddress to;
    private List<Port> filter;
    private Integer timeout;

    public IPAddress getFrom() {
        return from;
    }

    public void setFrom(IPAddress from) {
        this.from = from;
    }

    public IPAddress getTo() {
        return to;
    }

    public void setTo(IPAddress to) {
        this.to = to;
    }

    public List<Port> getFilter() {
        return filter;
    }

    public void setFilter(List<Port> filter) {
        this.filter = filter;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public TextField getFromTextField() {
        return fromTextField;
    }

    public void setFromTextField(TextField fromTextField) {
        this.fromTextField = fromTextField;
    }

    public TextField getToTextField() {
        return toTextField;
    }

    public void setToTextField(TextField toTextField) {
        this.toTextField = toTextField;
    }

    public TextArea getFilterTextArea() {
        return filterTextArea;
    }

    public void setFilterTextArea(TextArea filterTextArea) {
        this.filterTextArea = filterTextArea;
    }

    public Button getApplyButton() {
        return applyButton;
    }

    public void setApplyButton(Button applyButton) {
        this.applyButton = applyButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

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
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private TextArea filterTextArea;
    @FXML
    private TextField timeoutTextField;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    public void initialize() {
        clipRect.widthProperty().bind(root.widthProperty());
        clipRect.heightProperty().bind(root.heightProperty());
    }

    public void manuallyInit() {
        if (from != null) {
            fromTextField.setText(from.toString());
        }

        if (to != null) {
            toTextField.setText(to.toString());
        }

        if (filter != null) {
            filterTextArea.setText(
                    filter.stream().collect(
                                    StringBuilder::new,
                                    (first, second) -> {
                                        StringBuilder interm = !first.isEmpty() ? first.append("\n") : first;
                                        interm.append(second);
                                    },
                                    StringBuilder::append)
                            .toString());
        }

        if (timeout != null) {
            timeoutTextField.setText(timeout.toString());
        }
    }

    @FXML
    public void onApplyButtonClick() {
        try {
            from = new IPAddress(fromTextField.getText());
        } catch (IPAddressBuildException ex) {
            from = new IPAddress("192.168.1.1");
        }

        try {
            to = new IPAddress(toTextField.getText());
        } catch (IPAddressBuildException ex) {
            to = new IPAddress("192.168.1.254");
        }

        String[] delimitedFilters = filterTextArea.getText().trim().replace("\r", "").split("\n");

        filter = Arrays.stream(delimitedFilters)
                .filter(IntParser::tryParse)
                .map(e -> new Port(Integer.parseInt(e)))
                .collect(Collectors.toList());

        try {
            timeout = Integer.parseInt(timeoutTextField.getText());
        } catch (NumberFormatException ex) {
            timeout = 1000;
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffset = stage.getY() - mouseEvent.getScreenY();
    }

    @FXML
    public void handleMovementAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffset);
    }
}
