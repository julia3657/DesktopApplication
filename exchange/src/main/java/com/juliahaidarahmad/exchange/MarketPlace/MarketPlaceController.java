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
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.juliahaidarahmad.exchange.login.Login;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    private Label helpTextLabel;

    @FXML
    public Label usdAmountLabel;
    @FXML
    public Label lbpAmountLabel;

    public static void showAlert(AlertType type, String title, String headerText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

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
            Button buyButton = new Button("\u2713"); // Unicode for check mark
            Button deleteButton = new Button("\u2716");

            {
                buyButton.setOnAction(event -> {
                    MarketPlace data = getTableView().getItems().get(getIndex());
                    handleBuy(data); // Method to handle the buying process
                });
                deleteButton.setOnAction(event -> {
                    MarketPlace dataToDelete = getTableView().getItems().get(getIndex());
                    handleDelete(dataToDelete, Login.authenticatedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(5);  // Use HBox to hold both buttons
                    hBox.getChildren().addAll(buyButton, deleteButton);
                    setGraphic(hBox);
                }
            }

        });


        tableView.getColumns().add(actionColumn);
        loadMarketData();
        getUser();
    }

    private void handleDelete(MarketPlace marketPlaceToDelete, User authenticatedUser) {
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;


        if (authHeader != null) {
            String marketId = String.valueOf(marketPlaceToDelete.getId());
            ExchangeService.exchangeApi().deleteMarketPlace(authHeader, marketId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Market transaction deleted successfully!");
                            // Refresh the table view to reflect the deletion
                            loadMarketData();
                        } else {
                            String errorMessage = "Failed to delete transaction.";
                            if (response.errorBody() != null) {
                                try {
                                    // Extract error message from error body
                                    errorMessage = response.errorBody().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR, "Error", "Network error or server is down. Please try again later.");
                        throwable.printStackTrace();
                    });
                }
            });
        } else {
            // User is not authorized to delete the marketplace entry
            showAlert(Alert.AlertType.ERROR, "Unauthorized", "You are not authorized to delete this transaction.");
        }
    }


    private void handleBuy(MarketPlace marketPlace) {
        // Validation checks
        if (marketPlace == null || marketPlace.getUsdAmount() <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid amount greater than 0.");
            return;
        }

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;

        ExchangeService.exchangeApi().buyMarketPlace(marketPlace, authHeader).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction successfully processed for " + marketPlace.getUsdAmount() + " USD");
                    } else {
                        String errorMessage = "Make sure you have enough credit and aren't buying from yourself" ;
                        showAlert(Alert.AlertType.ERROR, "Transaction Failed", errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Platform.runLater(() -> {
                    showAlert(Alert.AlertType.ERROR, "Network Error", "Network error or server is down. Please try again later.");
                });
                throwable.printStackTrace();
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

            if (usdAmount <= 0 || lbpAmount <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Amount", "Amounts must be greater than zero.");
                return;
            }

            // Check if a radio button is selected
            RadioButton selectedRadioButton = (RadioButton) transactionType.getSelectedToggle();
            if (selectedRadioButton == null) {
                showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select the transaction direction.");
                return;
            }
            boolean usdToLbp = selectedRadioButton.getText().equals("USD to LBP");

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
                            showAlert(Alert.AlertType.ERROR, "Error", "You are not logged in or don't have enough credit");
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

    public void showHelpText(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Here's some helpful information");
        alert.setContentText("In this section, you can sell Usd or Lbp and set your expected price. \nPlease select Usd to Lbp if you want to sell Usd \n and select Lbp to Usd if you want to sell Lbp. ");
        alert.getDialogPane().setPrefWidth(400); // Adjust width as needed
        alert.showAndWait();
    }

}