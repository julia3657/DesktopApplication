
module com.juliahaidarahmad.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires okhttp3;
    requires retrofit2.converter.gson;
    requires java.prefs;
    opens com.juliahaidarahmad.exchange to javafx.fxml;
    opens com.juliahaidarahmad.exchange.api.model to gson,javafx.base;
    exports com.juliahaidarahmad.exchange;
    exports com.juliahaidarahmad.exchange.rates;
    exports com.juliahaidarahmad.exchange.Statistics to javafx.fxml;
    exports com.juliahaidarahmad.exchange.MarketPlace;
    opens com.juliahaidarahmad.exchange.Statistics to javafx.fxml;
    opens com.juliahaidarahmad.exchange.rates to javafx.fxml;
    opens com.juliahaidarahmad.exchange.login to javafx.fxml;
    opens com.juliahaidarahmad.exchange.register to javafx.fxml;
    opens com.juliahaidarahmad.exchange.transactions to javafx.fxml;
    opens com.juliahaidarahmad.exchange.MarketPlace to javafx.fxml;

}

