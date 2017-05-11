package com.example.sona.opticalillusions;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sona.opticalillusions.model.Illusion;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Adapter used to fill the grid of illusions.
 * Created by Soňa on 04-Apr-17.
 */

final class ImageAdapter extends RealmBaseAdapter<Illusion> {
    private Context context;
    private int size;

    public ImageAdapter(Context c, OrderedRealmCollection<Illusion> illusions, int size) {
        super(illusions);
        context = c;
        this.size = size;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.illusion_grid_item, parent, false);
        }

        Illusion item = getItem(position);

        ImageView imageViewItem = (ImageView) convertView.findViewById(R.id.iv_grid_item);
        imageViewItem.setImageResource(item.getThumbnail());
        imageViewItem.requestLayout();
        imageViewItem.getLayoutParams().width = size;
        imageViewItem.getLayoutParams().height = size;

        return convertView;
    }
}