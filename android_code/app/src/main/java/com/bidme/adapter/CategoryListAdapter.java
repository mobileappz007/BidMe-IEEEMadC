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
import com.bidme.databinding.AdapterCategoryListBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.CategoryDTO;
import com.bidme.utils.Colors;

import java.util.ArrayList;
import java.util.Random;

import static com.bidme.R.layout.adapter_category_list;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ImageviewHolder> {


    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<CategoryDTO> CategoryDTOArrayList;
    // RecyclerViewClickListner mListner;

    public CategoryListAdapter(Context mContext, ArrayList<CategoryDTO> CategoryDTOArrayList) {
        this.mContext = mContext;
        this.CategoryDTOArrayList = CategoryDTOArrayList;
    }


    @NonNull
    @Override
    public ImageviewHolder onCreateViewHolder(ViewGroup parent, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterCategoryListBinding binding = DataBindingUtil.inflate(layoutInflater, adapter_category_list, parent, false);

        return new ImageviewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageviewHolder viewHolder, final int position) {
        Glide
                .with(mContext)
                .load(CategoryDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.circle)
                .into(viewHolder.binding.civCat);
        viewHolder.binding.tvCat.setText(CategoryDTOArrayList.get(position).getCat_title());

        int i = new Random().nextInt(21);

        GradientDrawable bgShape = (GradientDrawable) viewHolder.binding.llcat.getBackground();
        bgShape.setColor(Color.parseColor ("#"+ Colors.mColors[i]));
        viewHolder.binding.tvCat.setTextColor(Color.parseColor ("#"+ Colors.mColors[i]));
        viewHolder.binding.line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext,CategoryBasedAdsAuction.class);
                in.putExtra(Const.CAT_TITLE,CategoryDTOArrayList.get(position).getCat_title());
                mContext.startActivity(in);
            }
        });



    }

    @Override
    public int getItemCount() {
        return CategoryDTOArrayList.size();
    }

    public static class ImageviewHolder extends RecyclerView.ViewHolder {

        AdapterCategoryListBinding binding;
        // private RecyclerViewClickListner mListner;


        public ImageviewHolder(final AdapterCategoryListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            //mListner=listener;
            // itemBinding.getRoot().setOnClickListener(this);


        }

      /*  @Override
        public void onClick(View v) {
            mListner.onClick(v,getAdapterPosition());


        }*/
    }
}
