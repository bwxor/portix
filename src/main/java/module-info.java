module com.bwxor.portix {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;

    opens com.bwxor.portix to javafx.fxml;
    opens com.bwxor.portix.entity to javafx.base;

    exports com.bwxor.portix;
    exports com.bwxor.portix.controller;
    opens com.bwxor.portix.controller to javafx.fxml;
}