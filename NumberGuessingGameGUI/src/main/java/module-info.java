module com.example.brofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.brofx to javafx.fxml;
    exports com.example.brofx;
}