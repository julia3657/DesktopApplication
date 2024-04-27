package com.juliahaidarahmad.exchange.rates;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.Parent;
import com.juliahaidarahmad.exchange.api.model.ExchangeRates;
import com.juliahaidarahmad.exchange.api.model.Transaction;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;
import javafx.application.Platform;
import javafx.scene.control.PopupControl;
import java.io.IOException;


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
    public PopupControl advancedFeaturesPopup;
    public DatePicker scheduledDatePicker;
    public TextField scheduledRateTextField;
    @FXML
    private GridPane advancedFeaturesGrid;


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
        if (usdValue <= 0 || lbpValue <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input Detected");
            alert.setContentText("Negative values and Zeros are not allowed. Please enter a positive number.");
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
                    transactionType.selectToggle(null);
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
        try {
            float usdValue = Float.parseFloat(buyUsdRateLabel.getText());
            float lbpValue = Float.parseFloat(sellUsdRateLabel.getText());

            if (usdValue <= 0 || lbpValue <= 0) {
                throw new IllegalArgumentException("Exchange rates must be positive numbers.");
            }

            RadioButton selectedRadioButton = (RadioButton) calculatorType.getSelectedToggle();
            if (selectedRadioButton == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Please select an exchange type.");
                alert.showAndWait();
                return;
            }

            boolean isUsdToLbp = selectedRadioButton.getText().equals("USD to LBP");
            float amount = Float.parseFloat(calculatorTextField.getText());

            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }

            float result = isUsdToLbp ? amount * usdValue : amount / lbpValue;
            String output = "Result: " + (isUsdToLbp ? "LBP " : "USD ") + result;

            Platform.runLater(() -> {
                calculatorTextField.setText("");
                resultCalculator.setText(output);
                calculatorType.selectToggle(null);
            });

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Number Format Error");
            alert.setContentText("Please ensure all inputs are valid numbers.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Input Validation Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void showHelpText(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Here's some helpful information");
        alert.setContentText("In this section, you can add a Transaction. \n If you press Buy Usd that means the transaction is Usd to Lbp. \n If you press Sell Usd that means the transaction is Lbp to Usd.");
        alert.getDialogPane().setPrefWidth(400); // Adjust width as needed
        alert.showAndWait();
    }
}