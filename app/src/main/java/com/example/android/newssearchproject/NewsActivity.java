package com.example.android.newssearchproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    public static final String REQUEST_URL =
            "http://content.guardianapis.com/search?";
    private static final int NEWSLIST_LOADER_ID = 1;
    public NewsAdapter mAdapter;
    public TextView mEmptyStateTextView;
    public ProgressBar pb;
    private NetworkInfo ni;
    private ListView newsListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsListView = (ListView) findViewById(R.id.list);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);

        newsListView.setEmptyView(mEmptyStateTextView);
        newsListView.setAdapter(mAdapter);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()) {
            getLoaderManager().initLoader(NEWSLIST_LOADER_ID, null, this);
        } else {
            pb.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.nullInternet);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int pos, long l) {
                News currentNews = mAdapter.getItem(pos);
                Uri newsListUri = Uri.parse(currentNews.getNUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsListUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        String seaQ = "";
        String ord = "";

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        seaQ = sharedPrefs.getString(
                getString(R.string.querySettingsKey),
                getString(R.string.querySettingsDef));

        ord = sharedPrefs.getString(
                getString(R.string.queryOrderKey),
                getString(R.string.queryOrderDef)
        );

        uriBuilder.appendQueryParameter("order-by", ord);
        uriBuilder.appendQueryParameter("q", seaQ);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        pb.setVisibility(View.GONE);
        if (news != null && !news.isEmpty()) {
            mEmptyStateTextView.setText("");
            mAdapter.addAll(news);
        } else mEmptyStateTextView.setText("No articles available.");
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {mAdapter.clear();}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem i) {
        int id = i.getItemId();
        if (id == R.id.settingsMain){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(i);
    }
}
