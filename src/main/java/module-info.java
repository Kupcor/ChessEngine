module org.pk {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.pk to javafx.fxml;
    exports org.pk;
}
