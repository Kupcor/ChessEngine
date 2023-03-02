module org.pk {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.base;

    opens org.pk to javafx.fxml;
    exports org.pk;
    exports org.pk.chessgame;
}
