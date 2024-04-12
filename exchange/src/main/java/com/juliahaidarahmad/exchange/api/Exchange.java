package com.juliahaidarahmad.exchange.api;
import com.juliahaidarahmad.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Exchange {
    @POST("/user")
    Call<User> addUser(@Body User user);
    @POST("/authentication")
    Call<Token> authenticate(@Body User user);
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction,
                                @Header("Authorization") String authorization);
    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization")
                                            String authorization);
    @GET("/statistics")
    Call<Statistics> getStats(@Query("days") Integer numberOfDays);

    @GET("/graph")
    Call<GraphsResponse> getGraph(@Query("from") String date1, @Query("to") String date2);
}
