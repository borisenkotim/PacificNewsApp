package com.example.pnwalerts.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pnwalerts.ApiCalls.FirebaseApi;
import com.example.pnwalerts.R;
import com.example.pnwalerts.model.Alert;
import com.example.pnwalerts.model.Category;
import com.example.pnwalerts.model.DatabaseInstance;
import com.example.pnwalerts.model.NewsAlert;
import com.example.pnwalerts.ui.news.NewsArticleActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private NewsAlertAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = root.findViewById(R.id.home_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().setTitle("Home");

        //updateUI();
        doHttpRequest();

        //Toast.makeText(getContext(), DatabaseInstance.getInstance(getContext()).getEmail(), Toast.LENGTH_SHORT).show();

        return root;
    }

    private void doHttpRequest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/"//"https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/byUser/"
        ).addConverterFactory(GsonConverterFactory.create()).build();

        FirebaseApi jsonPlaceHolderApi = retrofit.create(FirebaseApi.class);
        Call<List<Alert>> call = jsonPlaceHolderApi.getEmailPost(DatabaseInstance.getInstance(getContext()).getEmail());

        call.enqueue(new Callback<List<Alert>>() {
            @Override
            public void onResponse(Call<List<Alert>> call, Response<List<Alert>> response) {

                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                }

                List<Alert> alertList = response.body();

                updateUI(alertList);
            }

            @Override
            public void onFailure(Call<List<Alert>> call, Throwable t) {
                //Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(List<Alert> listOfAllerts) {
        List<Alert> alertList = listOfAllerts;//generateFakeNewsData();

        if(mAdapter == null) {
            mAdapter = new NewsAlertAdapter(alertList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setAlertList(alertList);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.smoothScrollToPosition(alertList.size());
    }

    private List<NewsAlert> generateFakeNewsData() {
        List<NewsAlert> alertList = new ArrayList<>();
        NewsAlert newAlert = new NewsAlert();
        Category newCategory = new Category();
        newCategory.setLocation("Everett");
        newCategory.setName("Weather");
        newAlert.setHeader("Tornado Detected");
        newAlert.setCategory(newCategory);
        newAlert.setSubmissionTime(new Date(System.currentTimeMillis()));
        alertList.add(newAlert);

        NewsAlert newAlert2 = new NewsAlert();
        Category newCategory2 = new Category();
        newCategory2.setLocation("Everett");
        newCategory2.setName("Police");
        newAlert2.setHeader("Crime Stuff");
        newAlert2.setCategory(newCategory2);
        newAlert2.setSubmissionTime(new Date(System.currentTimeMillis() + 2000));
        alertList.add(newAlert2);
        return alertList;
    }

    private class NewsAlertHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mHeaderText, mLocationText, mCategoryText, mDateText;
        Alert currentAlert;

        public NewsAlertHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.alert_item, parent, false));

            mHeaderText = itemView.findViewById(R.id.news_headline);
            mLocationText = itemView.findViewById(R.id.news_location);
            mCategoryText = itemView.findViewById(R.id.news_category);
            mDateText = itemView.findViewById(R.id.news_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newIntent = new Intent(getActivity(), NewsArticleActivity.class);
                    newIntent.putExtra("item_header", currentAlert.getHeader());
                    newIntent.putExtra("item_location", currentAlert.getLocation());
                    newIntent.putExtra("item_category", currentAlert.getCategory());
                    newIntent.putExtra("item_date", currentAlert.getDate().toString());
                    newIntent.putExtra("item_text", currentAlert.getText());
                    startActivity(newIntent);
                }
            });
        }


        @Override
        public void onClick(View view) {
            //go to full news activity.
            //Intent newIntent = new Intent();
            //startActivity(newIntent);
            //Toast.makeText(getContext(), "Item was clicked: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            //Snackbar testBar = Snackbar.make(view, "Test", Snackbar.LENGTH_LONG);
            //testBar.show();
        }

        public void bind(Alert newsAlert) {
            currentAlert = newsAlert;
            mHeaderText.setText(newsAlert.getHeader());
            mLocationText.setText(newsAlert.getLocation());
            mCategoryText.setText(newsAlert.getCategory());
            mDateText.setText(newsAlert.getDate().toString());
        }
    }

    private class NewsAlertAdapter extends RecyclerView.Adapter<NewsAlertHolder> {

        private List<Alert> mAlertList;

        public NewsAlertAdapter(List<Alert> alertList) {
            mAlertList = alertList;
        }

        public NewsAlertHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new NewsAlertHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsAlertHolder holder, int position) {
            Alert alertItem = mAlertList.get(position);
            holder.bind(alertItem);
        }

        @Override
        public int getItemCount() {
            return mAlertList.size();
        }

        public void setAlertList(List<Alert> alertList) {
            mAlertList = alertList;
        }
    }
}