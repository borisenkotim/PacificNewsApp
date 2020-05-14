package com.example.pnwalerts.ui.categories;

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
import com.example.pnwalerts.model.CategoryItem;
import com.example.pnwalerts.ui.home.HomeFragment;
import com.example.pnwalerts.ui.news.NewsArticleActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilteredCategoriesFragment extends Fragment {

    private String mCategory;
    private RecyclerView mRecyclerView;
    private FilteredCategoryAdapter mAdapter;

    public static FilteredCategoriesFragment getInstance(String category) {
        Bundle args = new Bundle();
        args.putSerializable("item_category", category);
        FilteredCategoriesFragment fragment = new FilteredCategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//        mCategory = (String) getArguments().getSerializable("item_category");
//
//        getActivity().setTitle(mCategory);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtered_categories, container, false);

        mCategory = (String) getArguments().getSerializable("item_category");

        getActivity().setTitle(mCategory);

        mRecyclerView = view.findViewById(R.id.cat_frag_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        doHttpRequest(mCategory);

        return view;
    }

    private void updateUI(List<Alert> listOfAlerts) {
        List<Alert> alertList = listOfAlerts;//generateFakeNewsData();

        if(alertList == null || alertList.size() == 0) {
            Toast.makeText(getActivity(), "No news for this category", Toast.LENGTH_SHORT).show();
        }

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
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                }

                List<Alert> alertList = response.body();

                updateUI(alertList);
            }

            @Override
            public void onFailure(Call<List<Alert>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FilteredCategoryHolder extends RecyclerView.ViewHolder{

        TextView mNewsHeader, mNewsCategory, mNewsDate;

        Alert currentAlert;

        public FilteredCategoryHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.filtered_category_item, parent, false));
            //implement textviews

            mNewsHeader = itemView.findViewById(R.id.news_header);
            mNewsCategory = itemView.findViewById(R.id.news_category);
            mNewsDate = itemView.findViewById(R.id.news_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), mCategoryHeader.getText().toString(), Toast.LENGTH_SHORT).show();
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


        public void bind(Alert alertItem) {
            currentAlert = alertItem;
            mNewsHeader.setText(alertItem.getHeader());
            mNewsCategory.setText(alertItem.getCategory());
            mNewsDate.setText(alertItem.getDate().toString());
        }
    }

    private class FilteredCategoryAdapter extends RecyclerView.Adapter<FilteredCategoryHolder> {

        private List<Alert> mCatList;

        public FilteredCategoryAdapter(List<Alert> catList) {
            mCatList = catList;
        }

        public FilteredCategoryHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
