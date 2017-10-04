package com.example.android.newssearchproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by AgiChrisPC on 09/07/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static class ViewHolder {
        TextView titleTV, typeTV, sectTV, pubDTV;
    }

    public NewsAdapter(@NonNull Context c, @NonNull List<News> news) {
        super(c, 0, news);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View cV, @NonNull ViewGroup parent) {
        View vListItem = cV;

        final News currentNews = getItem(pos);
        ViewHolder vHol;

        if (vListItem == null) {
            vListItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            vHol = new ViewHolder();
            vHol.titleTV = vListItem.findViewById(R.id.titleXml);
            vHol.typeTV = vListItem.findViewById(R.id.typeXml);
            vHol.sectTV = vListItem.findViewById(R.id.sectionXml);
            vHol.pubDTV = vListItem.findViewById(R.id.dateXml);
            vListItem.setTag(vHol);
        } else vHol = (ViewHolder) vListItem.getTag();

        vHol.titleTV.setText(currentNews.getTitle());
        vHol.typeTV.setText(currentNews.getType());
        vHol.sectTV.setText(currentNews.getSection());
        vHol.pubDTV.setText(currentNews.getPubD());

        return vListItem;
    }
}
