
module com.juliahaidarahmad.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    opens com.juliahaidarahmad.exchange to javafx.fxml;
    opens com.juliahaidarahmad.exchange.api.model to gson;
    exports com.juliahaidarahmad.exchange;
}

