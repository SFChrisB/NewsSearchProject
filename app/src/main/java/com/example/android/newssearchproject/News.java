package com.example.android.newssearchproject;

/**
 * Created by AgiChrisPC on 09/07/2017.
 */

public class News {
    private String mTitle, mType, mSection, mNewsUrl, mPubD;

    public News(String title, String type, String section, String newsUrl, String pubDate) {
        mTitle = title;
        mType = type;
        mSection = section;
        mNewsUrl = newsUrl;
        mPubD = pubDate;
    }
    public String getTitle() {return mTitle;}
    public String getType() {return mType;}
    public String getSection() {return mSection;}
    public String getNUrl() {return mNewsUrl;}
    public String getPubD() {return mPubD;}
}
