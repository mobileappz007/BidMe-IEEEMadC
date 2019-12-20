package com.bidme.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.bidme.model.CommonDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArrayAdapterWithContainsFilter<S> extends ArrayAdapter {

    private List<CommonDTO> items = null;
    private ArrayList<CommonDTO> arraylist;

    public ArrayAdapterWithContainsFilter(Activity context, int items_view, ArrayList<CommonDTO> items) {
        super(context, items_view, items);
        this.items = items;
        this.arraylist = new ArrayList<CommonDTO>();
        this.arraylist.addAll(items);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    // Filter Class
    public void getContainsFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arraylist);
        } else {
            for (CommonDTO item : arraylist) {
                if (item.getCatName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}