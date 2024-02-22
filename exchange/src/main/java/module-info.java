module com.juliahaidarahmad.exchange {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.juliahaidarahmad.exchange to javafx.fxml;
    exports com.juliahaidarahmad.exchange;
}