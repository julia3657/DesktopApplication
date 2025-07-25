package com.juliahaidarahmad.exchange.api;
import com.juliahaidarahmad.exchange.api.model.*;
import com.juliahaidarahmad.exchange.login.ForgotPasswordRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Exchange {
    @POST("/user")
    Call<User> addUser(@Body User user);
    @POST("/authentication")
    Call<Token> authenticate(@Body User user);
    @POST("/authentication")
    Call<Token> authenticateGoogle();
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction,
                                @Header("Authorization") String authorization);
    @POST("/notification")
    Call<Object> addNotification(@Body AdvancedTransaction transaction,
                                @Header("Authorization") String authorization);
    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization")
                                            String authorization);
    @GET("/statistics")
    Call<Statistics> getStats(@Query("days") Integer numberOfDays);
    @GET("/graph")
    Call<GraphsResponse> getGraph(@Query("from") String date1, @Query("to") String date2);

    @POST("/market")
    Call<Object> addMarketPlace(@Body MarketPlace marketplace,
                                @Header("Authorization") String authorization);
    @GET("/market")
    Call<List<MarketPlace>> getMarketPlace();
    @POST("/market/buy")
    Call<Void> buyMarketPlace(@Body MarketPlace marketPlace,
                              @Header("Authorization") String authorization);
    @GET("/user")
    Call<User> getUser(@Header("Authorization") String authorization);

    @DELETE("/market/{market_id}")
    Call<Void> deleteMarketPlace(@Header("Authorization") String authorization, @Path("market_id") String marketId);
    @POST("/forgotPassword")
    Call<Void> forgotPassword(@Body Object request);

    @POST("/resetPassword")
    Call<Void> resetPassword(@Body ResetPasswordRequest request);

}
