package com.example.pnwalerts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pnwalerts.ApiCalls.FirebaseApi;
import com.example.pnwalerts.database.DatabaseCreatorHelper;
import com.example.pnwalerts.model.DatabaseInstance;
import com.example.pnwalerts.model.Subscriptions;
import com.example.pnwalerts.model.User;
import com.example.pnwalerts.ui.HomePage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button signUpButton;
    private String userEmail;
    EditText emailString;
    DatabaseInstance db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseInstance.getInstance(getApplicationContext());

        if (!newUser()){
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.user_login);

            signUpButton = findViewById(R.id.emailSubmitButton);
            emailString = findViewById(R.id.userEmailInput);

            userEmail = "";
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userEmail= emailString.getText().toString();
                    // update database here
                    updateDB();
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                }
            });
        }
    }

    public boolean newUser(){
        // check local database to see if the user exists in there
        // if they don't exist return true and false if they already exist
        if (db.getEmail() == null) return true;
        return false;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void updateDB(){
        db.addEmail(getUserEmail());
        createPost();
        return;
    };

    private void createPost() {
        User newUser = new User();
        newUser.setEmail(DatabaseInstance.getInstance(getApplicationContext()).getEmail());

        Subscriptions newSubscription1 = new Subscriptions();
        newSubscription1.setCategory("Weather");
        newSubscription1.setPriority(0);
        newUser.setSubscriptions(newSubscription1);

        Subscriptions newSubscription2 = new Subscriptions();
        newSubscription2.setCategory("Construction");
        newSubscription2.setPriority(0);
        newUser.setSubscriptions(newSubscription2);

        Subscriptions newSubscription3 = new Subscriptions();
        newSubscription3.setCategory("Snohomish");
        newSubscription3.setPriority(0);
        newUser.setSubscriptions(newSubscription3);

        Subscriptions newSubscription4 = new Subscriptions();
        newSubscription4.setCategory("Everett");
        newSubscription4.setPriority(0);
        newUser.setSubscriptions(newSubscription4);

        Subscriptions newSubscription5 = new Subscriptions();
        newSubscription5.setCategory("Lake Stevens");
        newSubscription5.setPriority(0);
        newUser.setSubscriptions(newSubscription5);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/"//"https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/byUser/"
        ).addConverterFactory(GsonConverterFactory.create()).build();

        FirebaseApi jsonPlaceHolderApi = retrofit.create(FirebaseApi.class);
        Call<User> call = jsonPlaceHolderApi.createUser(newUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
