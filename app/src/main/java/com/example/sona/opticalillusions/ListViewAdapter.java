package com.example.sona.opticalillusions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Soňa on 07-Apr-17.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

    private static final String TAG = ListViewAdapter.class.getSimpleName();
    private int numberOfListItems;

    public ListViewAdapter(int numberOfListItems) {
        this.numberOfListItems = numberOfListItems;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.illusion_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberOfListItems;
    }


    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView listItemView;

        public ListViewHolder(View itemView) {
            super(itemView);

            listItemView = (TextView) itemView.findViewById(R.id.tv_list_item);
        }

        void bind (int listIndex) {
            listItemView.setText(String.valueOf(listIndex));
        }
    }
}
