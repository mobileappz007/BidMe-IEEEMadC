package com.bidme.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;

import com.bidme.activity.FilterActivity;
import com.bidme.databinding.AdapterCategoryListBinding;
import com.bidme.databinding.AdapterItemFilterBinding;
import com.bidme.model.CategoryDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.Colors;

import java.util.ArrayList;
import java.util.Random;

import static com.bidme.R.layout.adapter_item_filter;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    Context mContext;
    SharedPrefrence sharedPrefrence;
    FilterActivity filterActivity;
    ArrayList<CategoryDTO> CategoryDTOArrayList;
    LayoutInflater layoutInflater;
    AdapterCategoryListBinding binding;
    int pos =-1;
    int i =1;


    public FilterAdapter(FilterActivity filterActivity, ArrayList<CategoryDTO> CategoryDTOArrayList) {
        this.filterActivity = filterActivity;
        this.mContext = filterActivity.getApplicationContext();
        this.CategoryDTOArrayList = CategoryDTOArrayList;
    }


    @NonNull
    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int positon) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterItemFilterBinding binding = DataBindingUtil.inflate(layoutInflater, adapter_item_filter, parent, false);

        return new FilterAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.MyViewHolder myViewHolder, final int position) {
        Glide
                .with(mContext)
                .load(CategoryDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.circle)
                .into(myViewHolder.binding.civCat);
        myViewHolder.binding.tvCatN.setText(CategoryDTOArrayList.get(position).getCat_title());

        if (pos == position) {
            myViewHolder.binding.llCategory.setBackgroundResource(R.drawable.bg_circle_gray);


        } else {
            myViewHolder.binding.llCategory.setBackgroundResource(R.drawable.bg_circle_trans);
        }

        if(i==1){

            filterActivity.getGetId(CategoryDTOArrayList.get(0).getCat_id());
            filterActivity.getName(CategoryDTOArrayList.get(0).getCat_title());
            myViewHolder.binding.llCategory.setBackgroundResource(R.drawable.bg_circle_gray);
            i=i+i;


        }else { myViewHolder.binding.llcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                filterActivity.getGetId(CategoryDTOArrayList.get(position).getCat_id());
                filterActivity.getName(CategoryDTOArrayList.get(position).getCat_title());
                pos = position;
                notifyDataSetChanged();

            }
        });}



        int i = new Random().nextInt(21);

        GradientDrawable bgShape = (GradientDrawable) myViewHolder.binding.llcat.getBackground();
        bgShape.setColor(Color.parseColor("#" + Colors.mColors[i]));
        myViewHolder.binding.tvCatN.setTextColor(Color.parseColor("#" + Colors.mColors[i]));


    }

    @Override
    public int getItemCount() {
        return CategoryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterItemFilterBinding binding;

        public MyViewHolder(@NonNull AdapterItemFilterBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
