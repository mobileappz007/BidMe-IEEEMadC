package com.bidme.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.bidme.activity.CategoryBasedAdsAuction;
import com.bidme.databinding.AdapterCategoryBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.CategoryDTO;
import com.bidme.utils.Colors;

import java.util.ArrayList;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<CategoryDTO> categoryDTOList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryDTO> categoryDTOList) {
        this.mContext = mContext;
        this.categoryDTOList = categoryDTOList;
    }


    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterCategoryBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_category, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder myViewHolder, final int position) {
        Glide
                .with(mContext)
                .load(categoryDTOList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.circle)
                .into(myViewHolder.binding.civCat);

        myViewHolder.binding.tvCat.setText(categoryDTOList.get(position).getCat_title());
        myViewHolder.binding.civCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, CategoryBasedAdsAuction.class);
                in.putExtra(Const.GET_CAT_ID, categoryDTOList.get(position).getCat_id());
                in.putExtra(Const.CAT_TITLE, categoryDTOList.get(position).getCat_title());
                in.putExtra(Const.FLAG, "1");

                mContext.startActivity(in);
            }
        });

        int i = new Random().nextInt(21);

        GradientDrawable bgShape = (GradientDrawable) myViewHolder.binding.llcat.getBackground();
        bgShape.setColor(Color.parseColor("#" + Colors.mColors[i]));
        myViewHolder.binding.tvCat.setTextColor(Color.parseColor("#" + Colors.mColors[i]));


    }

    @Override
    public int getItemCount() {
        return categoryDTOList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCategoryBinding binding;

        public MyViewHolder(AdapterCategoryBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
