package com.example.pnwalerts.ui.categories;

import android.content.Intent;
import android.content.res.Resources;
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
import com.example.pnwalerts.model.DatabaseInstance;
import com.example.pnwalerts.model.ReadItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        mRecyclerView = root.findViewById(R.id.cat_recycler_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().setTitle("Categories");

        generateFakeReadItems();
        updateUI();

        return root;
    }

//    private void doHttpRequest() {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/"//"https://us-central1-hackathon-government-acc-ba79f.cloudfunctions.net/api/alert/byUser/"
//        ).addConverterFactory(GsonConverterFactory.create()).build();
//
//        FirebaseApi jsonPlaceHolderApi = retrofit.create(FirebaseApi.class);
//        Call<List<Alert>> call = jsonPlaceHolderApi.getEmailPost("sawzik98@gmail.com");
//
//        call.enqueue(new Callback<List<Alert>>() {
//            @Override
//            public void onResponse(Call<List<Alert>> call, Response<List<Alert>> response) {
//
//                if(!response.isSuccessful()) {
//                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
//                }
//
//                List<Alert> alertList = response.body();
//
//                updateUI(alertList);
//            }
//
//            @Override
//            public void onFailure(Call<List<Alert>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void updateUI() {
        List<CategoryItem> catList = generateFakeData();

        if(mAdapter == null) {
            mAdapter = new CategoryAdapter(catList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setCategoryList(catList);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.smoothScrollToPosition(catList.size());
    }

    private List<CategoryItem> generateFakeData() {
        List<CategoryItem> catList = new ArrayList<>();


        Resources res = getResources();
        //String[] catItemList = new String[] {"Snohomish", "Everett", "Lake Stevens"};

        CategoryItem newItem;
        for(String item : res.getStringArray(R.array.categories_array)) {
            newItem = new CategoryItem();
            newItem.setCategoryName(item);
            //newItem.setNotificationCount(getFakeReadItemsNotificationCount(item));
            catList.add(newItem);
        }
        return catList;
    }

    private void generateFakeReadItems() {
        ReadItem testItem = new ReadItem();
        testItem.setHash("xx1");
        testItem.setCategory("Everett");
        testItem.setWasRead(false);
        //DatabaseInstance.getInstance(getContext()).addReadItem(testItem);
        DatabaseInstance.getInstance(getContext()).updateItem(testItem);

        testItem.setHash("xx2");
        testItem.setWasRead(false);
        //DatabaseInstance.getInstance(getContext()).addReadItem(testItem);
        DatabaseInstance.getInstance(getContext()).updateItem(testItem);

        testItem.setHash("xx3");
        testItem.setWasRead(false);
        //DatabaseInstance.getInstance(getContext()).addReadItem(testItem);
        DatabaseInstance.getInstance(getContext()).updateItem(testItem);
    }

    private int getFakeReadItemsNotificationCount(String category) {
        int count = 0;
        for(ReadItem item : DatabaseInstance.getInstance(getContext()).getReadItemList(category)) {
            if(item == null)
                return 0;
            if(!item.wasRead())
                ++count;
        }
        return count;
    }


    private class CategoryHolder extends RecyclerView.ViewHolder{

        TextView mCategoryHeader, mCategoryCount;

        public CategoryHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.category_item, parent, false));
            //implement textviews

            mCategoryHeader = itemView.findViewById(R.id.category_item_name);
            //mCategoryCount = itemView.findViewById(R.id.category_notification);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment newFragment = FilteredCategoriesFragment.getInstance
                            (mCategoryHeader.getText().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            newFragment).commit();
//                    Intent newIntent = new Intent(getActivity(), FIlteredCategoriesActivity.class);
//                    newIntent.putExtra("item_category", mCategoryHeader.getText().toString());
//                    startActivity(newIntent);
                }
            });
        }


        public void bind(CategoryItem catItem) {

            mCategoryHeader.setText(catItem.getCategoryName());
            //mCategoryCount.setText(String.valueOf(catItem.getNotificationCount()));
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<CategoryItem> mCatList;

        public CategoryAdapter(List<CategoryItem> catList) {
            mCatList = catList;
        }

        public CategoryHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            CategoryItem alertItem = mCatList.get(position);
            holder.bind(alertItem);
        }

        @Override
        public int getItemCount() {
            return mCatList.size();
        }

        public void setCategoryList(List<CategoryItem> catList) {
            mCatList = catList;
        }
    }
}