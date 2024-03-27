package com.juliahaidarahmad.exchange.transactions;

import com.juliahaidarahmad.exchange.Authentication;
import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.model.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
public class Transactions implements Initializable {
    public TableColumn lbpAmount;
    public TableColumn usdAmount;
    public TableColumn transactionDate;
    public TableColumn usdToLbp;
    public TableView tableView;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbpAmount.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("lbpAmount"));
        usdAmount.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("usdAmount"));
        transactionDate.setCellValueFactory(new
                PropertyValueFactory<Transaction, String>("addedDate"));
        usdToLbp.setCellValueFactory(new
                PropertyValueFactory<Transaction,Boolean>("usdToLbp"));
        usdToLbp.setCellFactory(column -> new TableCell<Transaction, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item) {
                        setText("Sell Usd");
                    } else {
                        setText("Buy Usd");
                    }
                }
            }
        });

        ExchangeService.exchangeApi().getTransactions("Bearer " +
                        Authentication.getInstance().getToken())
                .enqueue(new Callback<List<Transaction>>() {
                    @Override
                    public void onResponse(Call<List<Transaction>> call,
                                           Response<List<Transaction>> response) {
                        tableView.getItems().setAll(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<Transaction>> call,
                                          Throwable throwable) {
                    }
                });
    }
}