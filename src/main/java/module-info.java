module com.bwxor.iport {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.bwxor.iport to javafx.fxml;
    opens com.bwxor.iport.entity to javafx.base;

    exports com.bwxor.iport;
    exports com.bwxor.iport.controller;
    opens com.bwxor.iport.controller to javafx.fxml;
}