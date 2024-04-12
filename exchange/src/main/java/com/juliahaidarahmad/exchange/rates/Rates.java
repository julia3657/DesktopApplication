package com.juliahaidarahmad.exchange.rates;

import com.juliahaidarahmad.exchange.Authentication;
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


public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    @FXML
    public TextField lbpTextField;
    @FXML
    public TextField usdTextField;
    public ToggleGroup transactionType;
    public Label resultCalculator;
    public TextField calculatorTextField;
    public ToggleGroup calculatorType;
    public void initialize() {
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
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;

        ExchangeService.exchangeApi().addTransaction(transaction,authHeader).enqueue(new Callback<Object>() {

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

    public void calculateResult(ActionEvent actionEvent) {

            float usdValue = Float.parseFloat(buyUsdRateLabel.getText());
            float lbpValue = Float.parseFloat(sellUsdRateLabel.getText());
            if (usdValue <= 0 || lbpValue <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid Input Detected");
                alert.setContentText("Negative values are not allowed. Please enter a positive number.");
                alert.showAndWait();
                return;
            }
            RadioButton selectedRadioButton = (RadioButton) calculatorType.getSelectedToggle();
            if (selectedRadioButton == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Please select an exchange type.");
                return;
            }
            boolean isUsdToLbp = selectedRadioButton.getText().equals("USD to LBP");
            float amount = Float.parseFloat(calculatorTextField.getText());
            if (amount <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input Detected");
                alert.setContentText("Amount must be greater than zero.");
                alert.showAndWait();
                return;
            }
            float result;
            if (isUsdToLbp) {
                result = amount * usdValue;
            } else {
                result = amount / lbpValue;
            }

            String output = "Result: " + (isUsdToLbp ? "LBP " : "USD ") + result;
            Platform.runLater(() -> {
                calculatorTextField.setText("");
                resultCalculator.setText(output);
            });

    }}