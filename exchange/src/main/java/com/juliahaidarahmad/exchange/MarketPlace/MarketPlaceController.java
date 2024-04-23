package com.juliahaidarahmad.exchange.MarketPlace;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.model.MarketPlace;
import com.juliahaidarahmad.exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarketPlaceController implements Initializable {

    @FXML
    private TextField usdTextField;

    @FXML
    private TextField lbpTextField;

    @FXML
    private RadioButton buyUsdRadioButton;

    @FXML
    private ToggleGroup transactionType;
    private String userToken;

    public TableView<MarketPlace> tableView;
    public TableColumn idColumn;
    public TableColumn usdAmountColumn;
    public TableColumn lbpAmountColumn;
    public TableColumn dateColumn;
    public TableColumn directionColumn;

    @FXML
    public Label usdAmountLabel;
    @FXML
    public Label lbpAmountLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usdAmountColumn.setCellValueFactory(new PropertyValueFactory<>("usdAmount"));
        lbpAmountColumn.setCellValueFactory(new PropertyValueFactory<>("lbpAmount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("usdToLbp"));
        directionColumn.setCellFactory(column -> new TableCell<MarketPlace, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Lbp to Usd" : "Usd to Lbp");
                }
            }
        });


        TableColumn<MarketPlace, Void> actionColumn = new TableColumn<>("Action");

        actionColumn.setCellFactory(col -> new TableCell<MarketPlace, Void>() {
            private final Button buyButton = new Button("Buy");

            {
                buyButton.setOnAction(event -> {
                    MarketPlace data = getTableView().getItems().get(getIndex());
                    handleBuy(data); // Method to handle the buying process
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buyButton);
                }
            }
        });

        tableView.getColumns().add(actionColumn);
        loadMarketData();
        getUser();
    }
    private void handleBuy(MarketPlace marketPlace) {
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;

        ExchangeService.exchangeApi().buyMarketPlace(marketPlace, authHeader).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction successfully processed for " + marketPlace.getUsdAmount() + " USD");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failed", "Transaction failed to process for " + marketPlace.getUsdAmount() + " USD");
                        String errorMessage = null;
                        try {
                            errorMessage = " Error: " + response.errorBody().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.err.println(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Platform.runLater(() -> {
                    showAlert(Alert.AlertType.ERROR, "Error", "Network error or server is down. Please try again later.");
                    throwable.printStackTrace(); // Consider logging this properly or handling the error more gracefully
                });
            }
        });
    }

    private void getUser() {
            String token = "Bearer " + Authentication.getInstance().getToken();
            ExchangeService.exchangeApi().getUser(token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && (response.body() != null)) {
                        User user;
                        user = response.body();
                        Platform.runLater(() -> {
                            Platform.runLater(() -> {
                                usdAmountLabel.setText(String.format("%.1f USD", user.getUsdBalance()));
                                lbpAmountLabel.setText(String.format("%.1f LBP", user.getLbpBalance()));
                            });
                        });
                    } else {
                        Platform.runLater(() -> {
                            System.err.println("Failed to fetch user info: " + response.code());
                            Platform.runLater(() -> {
                                usdAmountLabel.setText("Null");
                                lbpAmountLabel.setText("Null");
                            });
                        });
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        System.err.println("Error fetching user info: " + throwable.getMessage());
                        // Optionally update your UI with this error
                    });
                }
            });
        }




    private void loadMarketData() {

        ExchangeService.exchangeApi().getMarketPlace().enqueue(new Callback<List<MarketPlace>>() {
            @Override
            public void onResponse(Call<List<MarketPlace>> call, Response<List<MarketPlace>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> tableView.getItems().setAll(response.body()));
                } else {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch market data."));
                }
            }

            @Override
            public void onFailure(Call<List<MarketPlace>> call, Throwable t) {
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Unable to connect to the server."));
                t.printStackTrace();  // Consider logging this properly or handling the error more gracefully
            }
        });
    }

    @FXML
    private void addRequest(ActionEvent event) {
        try {
            Float usdAmount = Float.parseFloat(usdTextField.getText());
            Float lbpAmount = Float.parseFloat(lbpTextField.getText());
            boolean usdToLbp = buyUsdRadioButton.isSelected(); // Simplified boolean logic based on selected RadioButton

            MarketPlace marketplace = new MarketPlace(null, usdAmount, lbpAmount, usdToLbp);


            String userToken = Authentication.getInstance().getToken();
            String authHeader = userToken != null ? "Bearer " + userToken : null;

            ExchangeService.exchangeApi().addMarketPlace(marketplace, authHeader).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            usdTextField.clear();
                            lbpTextField.clear();
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction added successfully!");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to complete transaction, make sure you are logged in.");
                        }
                    });
                }

                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR, "Error", "The operation could not be completed. Please try again later.");
                    });
                }
            });
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid Input Detected. Please ensure the values entered are correct (integer or float).");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
