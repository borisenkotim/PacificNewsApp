package com.example.pnwalerts.ui.categories;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pnwalerts.ApiCalls.FirebaseApi;
import com.example.pnwalerts.R;
import com.example.pnwalerts.model.Alert;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FIlteredCategoriesActivity extends AppCompatActivity {

    private String mCategory;
    private RecyclerView mRecyclerView;
    private FilteredCategoryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filtered_categories);

        mCategory = getIntent().getExtras().getString("item_category");//savedInstanceState.getString("item_category");

        if(mCategory.equals(null) || mCategory.equals("")) {
            Toast.makeText(this, "No news to show for this category", Toast.LENGTH_SHORT).show();
        }
        else {
            setTitle(mCategory);

            mRecyclerView = findViewById(R.id.cat_frag_layout);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            doHttpRequest(mCategory);
        }
    }


//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_filtered_categories, container, false);
//
//        mCategory = savedInstanceState.getString("item_category");
//
//        setTitle(mCategory);
//
//        mRecyclerView = view.findViewById(R.id.cat_recycler_layout);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        doHttpRequest(mCategory);
//
//        return view;
//    }

    private void updateUI(List<Alert> listOfAlerts) {
        List<Alert> alertList = listOfAlerts;//generateFakeNewsData();

        if(mAdapter == null) {
            mAdapter = new FilteredCategoryAdapter(alertList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setCategoryList(alertList);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.smoothScrollToPosition(alertList.size());
    }

    private void doHttpRequest(String category) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/"//"https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/byUser/"
        ).addConverterFactory(GsonConverterFactory.create()).build();

        FirebaseApi jsonPlaceHolderApi = retrofit.create(FirebaseApi.class);
        Call<List<Alert>> call = jsonPlaceHolderApi.getCategoryPost(category);

        call.enqueue(new Callback<List<Alert>>() {
            @Override
            public void onResponse(Call<List<Alert>> call, Response<List<Alert>> response) {

                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                }

                List<Alert> alertList = response.body();

                updateUI(alertList);
            }

            @Override
            public void onFailure(Call<List<Alert>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FilteredCategoryHolder extends RecyclerView.ViewHolder{

        TextView mNewsHeader, mNewsCategory;

        public FilteredCategoryHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.filtered_category_item, parent, false));
            //implement textviews

            mNewsHeader = itemView.findViewById(R.id.news_header);
            mNewsCategory = itemView.findViewById(R.id.news_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), mCategoryHeader.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        public void bind(Alert alertItem) {
            mNewsHeader.setText(alertItem.getHeader());
            mNewsCategory.setText(alertItem.getCategory());
        }
    }

    private class FilteredCategoryAdapter extends RecyclerView.Adapter<FilteredCategoryHolder> {

        private List<Alert> mCatList;

        public FilteredCategoryAdapter(List<Alert> catList) {
            mCatList = catList;
        }

        public FilteredCategoryHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new FilteredCategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FilteredCategoryHolder holder, int position) {
            Alert alertItem = mCatList.get(position);
            holder.bind(alertItem);
        }

        @Override
        public int getItemCount() {
            return mCatList.size();
        }

        public void setCategoryList(List<Alert> catList) {
            mCatList = catList;
        }
    }
}
