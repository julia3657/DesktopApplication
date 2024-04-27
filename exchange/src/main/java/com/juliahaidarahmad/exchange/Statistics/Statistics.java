package com.juliahaidarahmad.exchange.Statistics;

import com.juliahaidarahmad.exchange.api.ExchangeService;
import com.juliahaidarahmad.exchange.api.model.GraphsResponse;
import com.juliahaidarahmad.exchange.api.model.CurrencyExchangeData;
import com.juliahaidarahmad.exchange.api.model.Table;
import com.juliahaidarahmad.exchange.login.ForgotPasswordRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Statistics implements Initializable {
    public LineChart<Number, Number> graph;
    public NumberAxis xAxis;
    public NumberAxis yAxis;


    public ChoiceBox numberOfDaysStats;
    public ChoiceBox numberOfDaysGraph;
    public ChoiceBox choiceBoxCurrency;

    public TableColumn<Table, String> stats;
    public TableColumn<Table, Float> lbpToUsd;
    public TableColumn<Table, Float> usdToLbp;
    public TableView tableView;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private DatePicker fromStatsDate;
    @FXML
    private DatePicker toStatsDate;


    public void fetchGraphs(ActionEvent actionEvent) {
        LocalDate fromDateValue = fromDate.getValue();
        LocalDate toDateValue = toDate.getValue();

        if (fromDateValue == null || toDateValue == null || fromDateValue.isAfter(toDateValue)) {
            System.out.println("Please select valid dates.");
            return;
        }

        String selected = (String) choiceBoxCurrency.getValue();

        ExchangeService.exchangeApi().getGraph(fromDateValue.toString(), toDateValue.toString()).enqueue(new Callback<GraphsResponse>() {
            @Override
            public void onResponse(Call<GraphsResponse> call, Response<GraphsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GraphsResponse graphsResponse = response.body();
                    Platform.runLater(() -> {
                        graph.getData().clear();

                        if (selected.equals("LBP to USD") || selected.equals("Both")) {
                            addDataSeries(graphsResponse.getLbpToUsd(), "LBP to USD");
                        }
                        if (selected.equals("USD to LBP") || selected.equals("Both")) {
                            addDataSeries(graphsResponse.getUsdToLbp(), "USD to LBP");
                        }
                    });
                } else {
                    System.out.println("No data available or an error occurred.");
                }
            }

            @Override
            public void onFailure(Call<GraphsResponse> call, Throwable throwable) {
                System.out.println("Failed to fetch graph data: " + throwable.getMessage());
            }
        });
    }

    private void addDataSeries(CurrencyExchangeData currencyData, String seriesName) {
        if (currencyData != null) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(seriesName);
            List<Double> values = currencyData.getValues();

            for (int i = 0; i < values.size(); i++) {
                Double rate = values.get(i);
                series.getData().add(new XYChart.Data<>(i + 1, rate));
            }

            graph.getData().add(series);
        }
    }

    public void fetchStats(ActionEvent actionEvent) {
        String numDaysStr = (String) numberOfDaysStats.getValue();
        Integer numDays;

        try {
            numDays = Integer.parseInt(numDaysStr.substring(5, 7).trim());
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse the number of days from: " + numDaysStr);
            return;
        }

        ExchangeService.exchangeApi().getStats(numDays).enqueue(new Callback<com.juliahaidarahmad.exchange.api.model.Statistics>() {
            @Override
            public void onResponse(Call<com.juliahaidarahmad.exchange.api.model.Statistics> call, Response<com.juliahaidarahmad.exchange.api.model.Statistics> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Ensure the response body is not null and convert it to table objects
                    List<Table> statTableObjList = response.body().toStatTableObj();
                    tableView.getItems().setAll(statTableObjList);
                } else {
                    tableView.getItems().clear();
                    System.out.println("No data available or an error occurred.");
                }
            }

            @Override
            public void onFailure(Call<com.juliahaidarahmad.exchange.api.model.Statistics> call, Throwable throwable) {
                System.out.println("An error occurred during the network request.");
                throwable.printStackTrace();
                tableView.getItems().clear(); // Optionally clear or update the UI appropriately
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stats.setCellValueFactory(new PropertyValueFactory<Table, String>("statName"));
        lbpToUsd.setCellValueFactory(new PropertyValueFactory<Table, Float>("lbpToUsd"));
        usdToLbp.setCellValueFactory(new PropertyValueFactory<Table, Float>("usdToLbp"));

        xAxis.setLabel("Date");
        yAxis.setLabel("Rate");

        fetchStats(null);
        fetchGraphs(null);
    }
}