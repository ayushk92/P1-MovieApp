package com.example.mynanodegreeapps.movieapp;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by akhatri on 29/11/15.
 */


public class TrailerViewAdapter extends ArrayAdapter<Trailer> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Trailer> mGridData = new ArrayList<Trailer>();

    public TrailerViewAdapter(Context mContext, int layoutResourceId, ArrayList<Trailer> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        Log.d("TrailerViewAdapter",String.valueOf(mGridData.size()));
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Trailer> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.trailer_text_view);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Trailer item = mGridData.get(position);

        holder.textView.setText(item.getTrailerName());
        Log.d("Trailer",position + ":" + item.getTrailerName());
        return row;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mGridData.size();
    }

    @Override
    public Trailer getItem(int position) {
        return mGridData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView textView;
    }
}