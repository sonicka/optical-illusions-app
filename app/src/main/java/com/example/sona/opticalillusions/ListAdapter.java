package com.example.sona.opticalillusions;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sona.opticalillusions.model.Illusion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Adapter used to fill the list of illusions.
 * Created by Soňa on 05-Apr-17.
 */

class ListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> headerList;
    private LinkedHashMap<String, List<Illusion>> listLinkedMap;
    private int categoryHeight;
    private int nameSize;

    /**
     * Constructor for the adapter.
     * @param context context
     * @param listLinkedMap map
     */
    ListAdapter(Context context, LinkedHashMap<String, List<Illusion>> listLinkedMap, int categoryHeight, int nameSize) {
        this.context = context;
        this.headerList = new ArrayList<>(listLinkedMap.keySet());
        this.listLinkedMap = listLinkedMap;
        this.categoryHeight = categoryHeight;
        this.nameSize = nameSize;
    }

    /**
     * Returns the number of categories.
     * @return int
     */
    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    /**
     * Returns the number of illusions within given category.
     * @param groupPosition int
     * @return int
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return listLinkedMap.get(headerList.get(groupPosition)).size();
    }

    /**
     * Returns list of illusions within given category.
     * @param groupPosition int
     * @return ArrayList
     */
    @Override
    public Object getGroup(int groupPosition) {
        return new ArrayList<>(listLinkedMap.keySet()).get(groupPosition);
    }

    /**
     * Returns the illusion within given category on given position.
     * @param groupPosition int
     * @param childPosition int
     * @return Illusion
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listLinkedMap.get(headerList.get(groupPosition)).get(childPosition);
    }

    /**
     * Returns category ID.
     * @param groupPosition int
     * @return int
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Returns illusion ID.
     * @param childPosition int
     * @return int
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Define objects not to have stable IDs.
     * @return false
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Sets up the view of groups within the ListView.
     * @param groupPosition int
     * @param isExpanded boolean
     * @param convertView view
     * @param parent viewgroup
     * @return view
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        AutofitTextView textView = (AutofitTextView) convertView.findViewById(R.id.tw_list_header);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2*categoryHeight/3));

        textView.requestLayout();
        textView.getLayoutParams().height = categoryHeight/2;
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gudea-regular.ttf"));
        textView.setTextColor(ContextCompat.getColor(context, R.color.green));
        textView.setText(headerTitle);

        return convertView;
    }

    /**
     * Sets up the view of an illusion within each category.
     * @param groupPosition int
     * @param childPosition int
     * @param isLastChild boolean
     * @param convertView view
     * @param parent viewgroup
     * @return view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.illusion_list_item, null);
        }

        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;

        Illusion illusion = (Illusion) getChild(groupPosition, childPosition);

        ImageView imageViewItem = (ImageView) convertView.findViewById(R.id.iv_list_item);

        AutofitTextView textViewItem = (AutofitTextView) convertView.findViewById(R.id.tv_list_item);
        textViewItem.requestLayout();
        textViewItem.getLayoutParams().height = categoryHeight/2;
        textViewItem.getLayoutParams().width = 3*categoryHeight;
        textViewItem.setPadding(categoryHeight/3,0,0,0);

        imageViewItem.setImageResource(illusion.getThumbnail());
        imageViewItem.setLayoutParams(new RelativeLayout.LayoutParams(width,categoryHeight));

        RelativeLayout ll = (RelativeLayout) convertView.findViewById(R.id.ll_list);
        ll.requestLayout();
        //ll.getLayoutParams().width = 7*width/8;
        ll.getLayoutParams().height = categoryHeight;
        ll.setGravity(RelativeLayout.CENTER_IN_PARENT);
        //ll.setPadding(width/8,0,width/8,0);

        imageViewItem.requestLayout();
        imageViewItem.getLayoutParams().width = categoryHeight;
        imageViewItem.getLayoutParams().height = categoryHeight;


        textViewItem.setText(illusion.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
