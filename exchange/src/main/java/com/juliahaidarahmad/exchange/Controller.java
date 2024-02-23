package com.juliahaidarahmad.exchange;

import com.juliahaidarahmad.exchange.api.model.ExchangeRates;
import com.juliahaidarahmad.exchange.api.model.Transaction;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;
import javafx.application.Platform;


public class Controller {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    @FXML
    public TextField lbpTextField;
    @FXML
    public TextField usdTextField;
    public ToggleGroup transactionType;
    public void initialize() {
        fetchRates();
    }
    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                ExchangeRates exchangeRates = response.body();
                Platform.runLater(() -> {

                    buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());

                    sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                });
            }
            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable throwable) {
                                                                             }
        });

    }
    public void addTransaction(ActionEvent actionEvent) {
    try{
        if (transactionType.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Transaction Type Selected");
            alert.setContentText("Please select a transaction type (Buy or Sell USD).");
            alert.showAndWait();
            return;
        }

        Transaction transaction = new Transaction(
                Float.parseFloat(usdTextField.getText()),
                Float.parseFloat(lbpTextField.getText()),
                ((RadioButton)transactionType.getSelectedToggle()).getText().equals("Sell USD")
        );
        float usdValue = Float.parseFloat(usdTextField.getText());
        float lbpValue = Float.parseFloat(lbpTextField.getText());
        if (usdValue < 0 || lbpValue < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input Detected");
            alert.setContentText("Negative values are not allowed. Please enter a positive number.");
            alert.showAndWait();
            return; // Exit the method early
        }
        ExchangeService.exchangeApi().addTransaction(transaction).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                fetchRates();

                Platform.runLater(() -> {
                    usdTextField.setText("");
                    lbpTextField.setText("");
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
    }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input Detected");
            alert.setContentText("Please ensure the values entered are correct (integer or float).");
            alert.showAndWait();
            return;
        }

    }

}