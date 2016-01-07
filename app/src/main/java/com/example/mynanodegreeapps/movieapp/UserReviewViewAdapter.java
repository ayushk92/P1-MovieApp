package com.example.mynanodegreeapps.movieapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akhatri on 06/01/16.
 */
public class UserReviewViewAdapter extends ArrayAdapter<UserReview> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<UserReview> mGridData = new ArrayList<UserReview>();

    public UserReviewViewAdapter(Context mContext, int layoutResourceId, ArrayList<UserReview> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<UserReview> mGridData) {
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
            holder.authorTextView = (TextView) row.findViewById(R.id.author);
            holder.contentTextView = (TextView) row.findViewById(R.id.content);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        UserReview item = mGridData.get(position);

        holder.authorTextView.setText(item.getAuthor());
        holder.contentTextView.setText(item.getContent());
        return row;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mGridData.size();
    }

    @Override
    public UserReview getItem(int position) {
        return mGridData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView authorTextView;
        TextView contentTextView;
    }
}
