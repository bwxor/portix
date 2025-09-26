module com.bwxor.iport {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.bwxor.iport to javafx.fxml;
    exports com.bwxor.iport;
}