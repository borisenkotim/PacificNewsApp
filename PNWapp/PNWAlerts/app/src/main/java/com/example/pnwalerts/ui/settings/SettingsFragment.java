package com.example.pnwalerts.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pnwalerts.ApiCalls.FirebaseApi;
import com.example.pnwalerts.R;
import com.example.pnwalerts.model.Alert;
import com.example.pnwalerts.model.DatabaseInstance;
import com.example.pnwalerts.model.Subscriptions;
import com.example.pnwalerts.model.User;
import com.example.pnwalerts.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsFragment extends Fragment {

    CheckBox weatherCheck1;
    CheckBox weatherCheck2;
    CheckBox construction1;
    CheckBox construction2;
    CheckBox location1check1;
    CheckBox location1check2;
    CheckBox location2check1;
    CheckBox location2check2;
    CheckBox location3check1;
    CheckBox location3check2;
    Button confirm;
    HashMap<String, Integer> subscription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.settings, container, false);
        weatherCheck1 = root.findViewById(R.id.weatherCheck1);
        weatherCheck2 = root.findViewById(R.id.weatherCheck2);
        construction1 = root.findViewById(R.id.constructionCheck1);
        construction2 = root.findViewById(R.id.constructionCheck2);
        location1check1 = root.findViewById(R.id.everettCheck1);
        location1check2 = root.findViewById(R.id.everettCheck2);
        location2check1 = root.findViewById(R.id.snohomishCheck1);
        location2check2 = root.findViewById(R.id.snohomishCheck2);
        location3check1 = root.findViewById(R.id.stevensCheck1);
        location3check2 = root.findViewById(R.id.stevensCheck2);
        confirm = root.findViewById(R.id.changesButton);

        doHttpRequest();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logChanges();
            }
        });

        getActivity().setTitle("Manage Subscriptions");
        return root;
    }

    public void logChanges(){
        // set passed in map to the updated map here
        doHttpPost();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new HomeFragment()).commit();
        Toast.makeText(getContext(), "Changes are saved", Toast.LENGTH_SHORT).show();
        return;
    }

    private void doHttpRequest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/user/subscriptions/"//"https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/byUser/"
        ).addConverterFactory(GsonConverterFactory.create()).build();

        FirebaseApi jsonPlaceHolderApi = retrofit.create(FirebaseApi.class);
        Call<List<User>> call = jsonPlaceHolderApi.getUser(DatabaseInstance.getInstance(getContext()).getEmail());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                }

                List<User> alertList = response.body();

                getList(alertList);

                //Toast.makeText(getContext(), String.valueOf(alertList.get(0).getSubscriptions().size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getList(List<User> newUser) {
        subscription = new HashMap<>();
//        subscription.put("Weather", 0);
//        subscription.put("Construction", 0);
//        subscription.put("Everett", 0);
//        subscription.put("Lake Stevens", 0);
//        subscription.put("Snohomish", 0);

        for(int i = 0; i < newUser.get(0).getSubscriptions().size(); i++) {
            subscription.put(newUser.get(0).getSubscriptions().get(i).getCategory(),
                    newUser.get(0).getSubscriptions().get(i).getPriority());
        }

        updateSettings();

        weatherCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!weatherCheck1.isChecked()){
                    subscription.put("Weather", 0);
                    weatherCheck2.setChecked(false);
                }
                else subscription.put("Weather", 1);
            }
        });

        weatherCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weatherCheck2.isChecked()){
                    subscription.put("Weather", 2);
                    weatherCheck1.setChecked(true);
                }
                else subscription.put("Weather", 1);
            }
        });

        construction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!construction1.isChecked()){
                    subscription.put("Construction", 0);
                    construction2.setChecked(false);
                }
                else subscription.put("Construction", 1);
            }
        });

        construction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (construction2.isChecked()){
                    subscription.put("Construction", 2);
                    construction1.setChecked(true);
                }
                else subscription.put("Construction", 1);
            }
        });

        location1check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!location1check1.isChecked()){
                    subscription.put("Everett", 0);
                    location1check2.setChecked(false);
                }
                else subscription.put("Everett", 1);
            }
        });

        location1check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location1check2.isChecked()){
                    subscription.put("Everett", 2);
                    location1check1.setChecked(true);
                }
                else subscription.put("Everett", 1);
            }
        });

        location2check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!location2check1.isChecked()){
                    subscription.put("Lake Stevens", 0);
                    location2check2.setChecked(false);
                }
                else subscription.put("Lake Stevens", 1);
            }
        });

        location2check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location2check2.isChecked()){
                    subscription.put("Lake Stevens", 2);
                    location2check1.setChecked(true);
                }
                else subscription.put("Lake Stevens", 1);
            }
        });

        location3check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!location3check1.isChecked()){
                    subscription.put("Snohomish", 0);
                    location3check2.setChecked(false);
                }
                else subscription.put("Snohomish", 1);
            }
        });

        location3check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location3check2.isChecked()){
                    subscription.put("Snohomish", 2);
                    location3check1.setChecked(true);
                }
                else subscription.put("Snohomish", 1);
            }
        });
    }

    public void updateSettings(){
        // for (int i = 0; i < subscription.size(); i++){
        for(String key : subscription.keySet()){
            switch(key) {
                case "Weather":
                    switch(subscription.get(key)) {
                        case 0:
                            weatherCheck1.setChecked(false);
                            break;
                        case 1:
                            weatherCheck1.setChecked(true);
                            break;
                        case  2:
                            weatherCheck1.setChecked(true);
                            weatherCheck2.setChecked(true);
                            break;
                        default:
                    }
                    break;
                case "Construction":
                    switch(subscription.get(key)) {
                        case 0:
                            construction1.setChecked(false);
                            break;
                        case 1:
                            construction1.setChecked(true);
                            break;
                        case  2:
                            construction1.setChecked(true);
                            construction2.setChecked(true);
                            break;
                        default:
                    }
                    break;
                case "Everett":
                    switch(subscription.get(key)) {
                        case 0:
                            location1check1.setChecked(false);
                            break;
                        case 1:
                            location1check1.setChecked(true);
                            break;
                        case  2:
                            location1check1.setChecked(true);
                            location1check2.setChecked(true);
                            break;
                        default:
                    }
                    break;
                case "Lake Stevens":
                    switch(subscription.get(key)) {
                        case 0:
                            location2check1.setChecked(false);
                            break;
                        case 1:
                            location2check1.setChecked(true);
                            break;
                        case  2:
                            location2check1.setChecked(true);
                            location2check2.setChecked(true);
                            break;
                        default:
                    }
                    break;
                case "Snohomish":
                    switch(subscription.get(key)) {
                        case 0:
                            location3check1.setChecked(false);
                            break;
                        case 1:
                            location3check1.setChecked(true);
                            break;
                        case  2:
                            location3check1.setChecked(true);
                            location3check2.setChecked(true);
                            break;
                        default:
                    }
                    break;
                default:
            }
        }
        return;
    }

    private void doHttpPost() {
        updatePost();
    }

    private void updatePost() {
        User newUser = new User();
        newUser.setEmail(DatabaseInstance.getInstance(getContext()).getEmail());
        Subscriptions newSubscription;
        for(String item : subscription.keySet()) {
            newSubscription = new Subscriptions();
            newSubscription.setCategory(item);
            newSubscription.setPriority(subscription.get(item));
            newUser.setSubscriptions(newSubscription);
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/user/"//"https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/byUser/"
        ).addConverterFactory(GsonConverterFactory.create()).build();

        FirebaseApi jsonPlaceHolderApi = retrofit.create(FirebaseApi.class);
        Call<User> call = jsonPlaceHolderApi.putPost(DatabaseInstance.getInstance(getContext()).getEmail()
                ,newUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}