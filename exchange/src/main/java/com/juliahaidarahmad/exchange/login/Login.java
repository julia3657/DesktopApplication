package com.juliahaidarahmad.exchange.login;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.OnPageCompleteListener;
import com.juliahaidarahmad.exchange.PageCompleter;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.model.Token;
import com.juliahaidarahmad.exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login implements PageCompleter {
    public static User authenticatedUser;
    public TextField usernameTextField;
    public TextField passwordTextField;
    private OnPageCompleteListener onPageCompleteListener;
    public void setOnPageCompleteListener(OnPageCompleteListener
                                                  onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }

    public void login(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(), passwordTextField.getText());
        ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                Authentication.getInstance().saveToken(response.body().getToken());
                authenticatedUser = user;
                authenticatedUser.id= user.getId();
                Platform.runLater(() -> {
                    onPageCompleteListener.onPageCompleted();
                });
            }
            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {}
        });
    }

    public void openGoogleLogin(ActionEvent actionEvent) {


    }
}
