package com.juliahaidarahmad.exchange.login;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.OnPageCompleteListener;
import com.juliahaidarahmad.exchange.PageCompleter;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.ResetPasswordRequest;
import com.juliahaidarahmad.exchange.api.model.Token;
import com.juliahaidarahmad.exchange.api.model.User;
import com.sun.javafx.menu.MenuItemBase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Pattern;


public class Login implements PageCompleter {
    public static User authenticatedUser;
    public TextField usernameTextField;
    public TextField passwordTextField;
    private OnPageCompleteListener onPageCompleteListener;
    @FXML
    TextField newPasswordTextField = new TextField();
    @FXML
    TextField ticketTextField = new TextField();
    @FXML
    public GridPane forgotPasswordsField ;


    public void setOnPageCompleteListener(OnPageCompleteListener
                                                  onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }


    public void login(ActionEvent actionEvent) {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            if (usernameTextField.getText().trim().isEmpty() || passwordTextField.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Incomplete Form");
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;}

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

            ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        // Save token and update authenticated user details
                        Authentication.getInstance().saveToken(response.body().getToken());
                        authenticatedUser = user;
                        authenticatedUser.setId(user.getId());

                        // Navigate to another page on successful login
                        Platform.runLater(() -> {
                            onPageCompleteListener.onPageCompleted();
                        });
                    } else {
                        // Handle backend error message display on UI thread
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Login Failed");
                            alert.setHeaderText("Failed to Log In");
                            String errorMessage = response.errorBody() != null ? "Invalid username or password." : "Unknown error occurred";
                            alert.setContentText(errorMessage);
                            alert.showAndWait();
                        });
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable throwable) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Network Error");
                    alert.setHeaderText("Error Connecting to Server");
                    alert.setContentText("Unable to connect to the server: " + throwable.getMessage());
                    alert.showAndWait();
                }
            });
        }


    public void openGoogleLogin() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://localhost:5000/login"));
                String redirectedUrl = promptUserForRedirectedUrl();
                String token = extractTokenFromUrl(redirectedUrl);
                System.out.println("Received token: " + token);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Error: Desktop is not supported");
        }
    }

    private String promptUserForRedirectedUrl() {

        return "http://localhost:5000/authorize?token=abc123";
    }

    // This method extracts the token from the redirected URL
    private String extractTokenFromUrl(String redirectedUrl) {
        try {
            URI uri = new URI(redirectedUrl);
            String query = uri.getQuery();
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("token")) {
                    return keyValue[1];
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void handleForgotPassword() {
        String user_name = usernameTextField.getText().trim();
        MenuItemBase sendButton = null;

        if (user_name.isEmpty()) {
            System.out.println("Username field is empty");
            return;
        }

        System.out.println(user_name);
        ForgotPasswordRequest request = new ForgotPasswordRequest(user_name);
        System.out.println(request.user_name);
        ExchangeService.exchangeApi().forgotPassword(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        System.out.println("Password reset email sent successfully.");

                    } else {
                        System.out.println("Failed to send password reset email: " + response.message());

                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Platform.runLater(() -> {
                    System.out.println("Network error or server is down.");
                    if (throwable != null) {
                        throwable.printStackTrace(); // This will print detailed error message
                    }
                });
            }
        });
}

    public void resetPassword(ActionEvent actionEvent) {
            String ticket = ticketTextField.getText().trim();
            String newPassword = newPasswordTextField.getText().trim();

            if (ticket.isEmpty() || newPassword.isEmpty()) {
                System.out.println("Please fill in both Ticket and New Password fields.");
                return;
            }


            ResetPasswordRequest request = new ResetPasswordRequest(ticket, newPassword);

            ExchangeService.exchangeApi().resetPassword(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            System.out.println("Password reset successful.");

                        } else {
                            System.out.println("Failed to reset password: " + response.message());

                        }
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        System.out.println("Network error or server is down.");

                    });
                }
            });
        }


}