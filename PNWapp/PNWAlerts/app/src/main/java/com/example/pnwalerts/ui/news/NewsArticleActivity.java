package com.example.pnwalerts.ui.news;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pnwalerts.R;
import com.example.pnwalerts.model.NewsAlert;

public class NewsArticleActivity extends AppCompatActivity {

    TextView mHeaderText, mLocationText, mCategoryText, mDateText, mNewsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article_layout);

        //String hash = savedInstanceState.getString("item_hash");

        mHeaderText = findViewById(R.id.news_header);
        mLocationText = findViewById(R.id.news_location);
        mCategoryText = findViewById(R.id.news_category);
        mDateText = findViewById(R.id.news_date);
        mNewsText = findViewById(R.id.news_text);

        String header = getIntent().getExtras().getString("item_header");
        String location = getIntent().getExtras().getString("item_location");
        String category = getIntent().getExtras().getString("item_category");
        String date = getIntent().getExtras().getString("item_date");
        String text = getIntent().getExtras().getString("item_text");

        mHeaderText.setText(header);
        mLocationText.setText(location);
        mCategoryText.setText(category);
        mDateText.setText(date);
        mNewsText.setText(text);

        //setNewsArticle(hash);
    }

    private void setNewsArticle(String hash) {
        //get news article using the hash
        //set the object.
        NewsAlert newItem = getAlertFromCloudDatabase(hash);
        mHeaderText.setText(newItem.getHeader());
        mLocationText.setText(newItem.getCategory().getLocation());
        mCategoryText.setText(newItem.getCategory().getName());
        mDateText.setText(newItem.getSubmissionTime().toString());
        mNewsText.setText(newItem.getNewsText());
    }

    private NewsAlert getAlertFromCloudDatabase(String hash) {
        return new NewsAlert();
    }
}
