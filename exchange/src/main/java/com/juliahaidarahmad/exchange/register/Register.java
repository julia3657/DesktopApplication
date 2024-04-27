package com.juliahaidarahmad.exchange.register;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.OnPageCompleteListener;
import com.juliahaidarahmad.exchange.PageCompleter;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.model.Token;
import com.juliahaidarahmad.exchange.api.model.User;
import com.juliahaidarahmad.exchange.login.Login;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class Register implements PageCompleter {
    public TextField usernameTextField;
    public static User authenticatedUser;
    public TextField passwordTextField;
    private OnPageCompleteListener onPageCompleteListener;

    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }


    public void register(ActionEvent actionEvent) {
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        // Check if any input field is empty
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Incomplete Form");
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        // Regex pattern to validate email
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (!emailPattern.matcher(username).matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Email");
            alert.setHeaderText("Invalid Username");
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
            return;
        }

        User user = new User(username, password);
        ExchangeService.exchangeApi().addUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    authenticateUser(user); // Authenticate the user after successful registration
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect Format");
                        alert.setContentText("Please ensure that your inputs are valid");
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Network Error");
                    alert.setHeaderText("Error Connecting to Server");
                    alert.setContentText("Unable to connect to the server: " + throwable.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }

    private void authenticateUser(User user) {
        ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Authentication.getInstance().saveToken(response.body().getToken());
                    authenticatedUser = user;
                    authenticatedUser.setId(user.getId());
                    Platform.runLater(() -> onPageCompleteListener.onPageCompleted());
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setHeaderText("Authentication Failure");
                        alert.setContentText("Failed to authenticate newly registered user.");
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Network Error");
                    alert.setHeaderText("Error Connecting to Server");
                    alert.setContentText("Unable to authenticate the user: " + throwable.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }

    public void registerWithGoogle(ActionEvent actionEvent) {
            if (Desktop.isDesktopSupported()) {
                User user = new User(usernameTextField.getText(), passwordTextField.getText());
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI("http://localhost:5000/login"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            else {}

    }
}

