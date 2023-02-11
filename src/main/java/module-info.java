module com.example.blackbook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.blackbook to javafx.fxml;
    exports com.example.blackbook;
}