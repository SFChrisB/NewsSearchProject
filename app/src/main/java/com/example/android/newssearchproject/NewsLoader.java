package com.example.android.newssearchproject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by AgiChrisPC on 09/07/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mNUrl;

    public NewsLoader(Context c, String url) {
        super(c);
        mNUrl = url;
    }

    @Override
    protected void onStartLoading() {forceLoad();}

    @Override
    public List<News> loadInBackground() {
        if (mNUrl == null)return null;

        List<News> newsData = QueryUtils.fetchNewsData(mNUrl);
        return newsData;
    }
}