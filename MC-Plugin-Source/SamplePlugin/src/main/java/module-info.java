module com.example.sampleplugin {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sampleplugin to javafx.fxml;
    exports com.example.sampleplugin;
}