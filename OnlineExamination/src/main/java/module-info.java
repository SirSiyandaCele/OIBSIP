module com.example.onlineexamination {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.onlineexamination to javafx.fxml;
    exports com.example.onlineexamination;
}