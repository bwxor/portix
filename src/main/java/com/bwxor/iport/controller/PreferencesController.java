package com.bwxor.iport.controller;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.util.IntParser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PreferencesController {
    private IPAddress from;
    private IPAddress to;
    private List<Port> filter;

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

    @FXML
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private TextArea filterTextArea;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

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
    }

    @FXML
    public void onApplyButtonClick() {
        from = new IPAddress(fromTextField.getText());
        to = new IPAddress(toTextField.getText());

        String[] delimitedFilters = filterTextArea.getText().trim().replace("\r", "").split("\n");

        filter = Arrays.stream(delimitedFilters)
                .filter(IntParser::tryParse)
                .map(e -> new Port(Integer.parseInt(e)))
                .collect(Collectors.toList());

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
