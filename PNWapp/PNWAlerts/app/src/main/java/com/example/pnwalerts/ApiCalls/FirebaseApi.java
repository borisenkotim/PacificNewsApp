package com.example.pnwalerts.ApiCalls;

import com.example.pnwalerts.model.Alert;
import com.example.pnwalerts.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FirebaseApi {

@GET("byCategory/{category}")
Call<List<Alert>> getCategoryPost(@Path("category") String categoryFilter);

@GET("byUser/{email}")
Call<List<Alert>> getEmailPost(@Path("email") String userEmail);

@GET("{email}")
Call<List<User>> getUser(@Path("email") String userEmail);

@POST("user/")
Call<User> createUser(@Body User newUser);

@PUT("{email}")
Call<User> putPost(@Path("email") String userEmail, @Body User newUser);

}
