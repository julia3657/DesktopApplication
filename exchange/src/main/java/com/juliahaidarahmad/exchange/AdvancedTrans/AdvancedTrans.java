package com.juliahaidarahmad.exchange.AdvancedTrans;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.model.AdvancedTransaction;
import com.juliahaidarahmad.exchange.api.model.ExchangeRates;
import com.juliahaidarahmad.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class AdvancedTrans {

    @FXML
    private Button addTransactionButton;
    @FXML
    private Label helpTextLabel;

    @FXML
    private TextField usdTextField;

    @FXML
    private ToggleGroup transactionType;

    @FXML
    private ToggleGroup transactionAction;
    public javafx.scene.control.Label buyUsdRateLabel;
    public javafx.scene.control.Label sellUsdRateLabel;
    @FXML
    private TextField FirstTextField;

    @FXML
    private TextField SecondTextField;
    @FXML
    private CheckBox belowTransaction;

    @FXML
    private CheckBox UsdToLbp;
    @FXML
    private CheckBox Buy;




    public void initialize() throws IOException {
        fetchRates();
    }

    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                ExchangeRates exchangeRates = response.body();
                Platform.runLater(() -> {
                    if (exchangeRates.lbpToUsd == null) {
                        sellUsdRateLabel.setText("null");
                    }
                    else if(exchangeRates.usdToLbp == null) {
                        buyUsdRateLabel.setText("null");
                    }
                    else {
                        buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                        sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                    }
                });
            }

            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable throwable) {
                Platform.runLater(() -> {
                    // Handle the error scenario, you can also set the labels to "null" or any error message
                    buyUsdRateLabel.setText("Error fetching data");
                    sellUsdRateLabel.setText("Error fetching data");
                });
            }
        });
    }

    @FXML
    public void addTransaction(ActionEvent actionEvent) {
        try {
            float Amount = Float.parseFloat(FirstTextField.getText());
            float amountToBuy = Float.parseFloat(SecondTextField.getText());

            // Check if the values are greater than zero
            if (Amount <= 0 || amountToBuy <= 0) {
                throw new IllegalArgumentException("Input should be a positive integer or float.");
            }

            boolean lessThan = belowTransaction.isSelected();
            boolean usdToLbp = UsdToLbp.isSelected();
            boolean buy = Buy.isSelected();

            AdvancedTransaction transaction = new AdvancedTransaction(Amount, lessThan, usdToLbp, buy, amountToBuy);

            String userToken = Authentication.getInstance().getToken();
            String authHeader = userToken != null ? "Bearer " + userToken : null;

            ExchangeService.exchangeApi().addNotification(transaction, authHeader).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    fetchRates();

                    Platform.runLater(() -> {
                        FirstTextField.setText("");
                        SecondTextField.setText("");
                        belowTransaction.setSelected(false);
                        UsdToLbp.setSelected(false);
                        Buy.setSelected(false);
                    });
                }

                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Operation Failed");
                        alert.setContentText("The operation could not be completed. Please try again later.");
                        alert.showAndWait();
                    });
                }
            });
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input Detected");
            alert.setContentText("Please ensure the values entered are correct (integer or float).");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input Detected");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void showHelpText(ActionEvent event) {
        helpTextLabel.setVisible(!helpTextLabel.isVisible());
    }
}
