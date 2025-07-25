package com.juliahaidarahmad.exchange;

import com.juliahaidarahmad.exchange.login.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class Parent implements OnPageCompleteListener,Initializable{
    public BorderPane borderPane;
    public Button transactionButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;
    private Scene scene;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNavigation();
    }

    @Override
    public void onPageCompleted() {
        swapContent(Section.RATES);
    }
    public void ratesSelected() {
        swapContent(Section.RATES);
    }

    public void advancedSelected() {
        swapContent(Section.ADVANCED);
    }
    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }
    public void loginSelected() {
        swapContent(Section.LOGIN);
    }
    public void registerSelected() {
        swapContent(Section.REGISTER);
    }
    public void marketplaceSelected(){swapContent(Section.MARKETPLACE);}

    public void logoutSelected() {
        Authentication.getInstance().deleteToken();
        Login.authenticatedUser = null;
        swapContent(Section.RATES);
    }
    private void updateNavigation() {
        boolean authenticated = Authentication.getInstance().getToken() != null;
        transactionButton.setManaged(authenticated);
        transactionButton.setVisible(authenticated);
        loginButton.setManaged(!authenticated);
        loginButton.setVisible(!authenticated);
        registerButton.setManaged(!authenticated);
        registerButton.setVisible(!authenticated);
        logoutButton.setManaged(authenticated);
        logoutButton.setVisible(authenticated);
    }

    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
            if (section.doesComplete()) {
                ((PageCompleter)
                        loader.getController()).setOnPageCompleteListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateNavigation();
    }

    public void statsSelected(ActionEvent actionEvent) {swapContent(Section.STATISTICS);}


    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private enum Section {
        RATES,
        TRANSACTIONS,
        LOGIN,
        REGISTER,
        STATISTICS,

        MARKETPLACE,
        ADVANCED;
        public String getResource() {
            return switch (this) {
                case RATES ->
                        "/com/juliahaidarahmad/exchange/rates/rates.fxml";
                case TRANSACTIONS ->
                        "/com/juliahaidarahmad/exchange/transactions/transactions.fxml";
                case LOGIN ->
                        "/com/juliahaidarahmad/exchange/login/login.fxml";
                case REGISTER ->
                        "/com/juliahaidarahmad/exchange/register/register.fxml";
                case STATISTICS ->
                        "/com/juliahaidarahmad/exchange/Statistics/Statistics.fxml";
                case MARKETPLACE ->
                        "/com/juliahaidarahmad/exchange/MarketPlace/MarketPlace.fxml";
                case ADVANCED ->
                        "/com/juliahaidarahmad/exchange/AdvancedTrans/AdvancedTrans.fxml";
                default -> null;
            };
        }
        public boolean doesComplete() {
            return switch (this) {
                case LOGIN, REGISTER -> true;
                default -> false;
            };
        }

    }
}
