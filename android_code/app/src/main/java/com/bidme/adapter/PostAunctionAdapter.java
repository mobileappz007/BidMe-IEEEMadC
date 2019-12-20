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

import com.bidme.activity.aution.PostAuction;
import com.bidme.activity.aution.PostSubAunction;
import com.bidme.databinding.ItemPostAunctionBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.CategoryDTO;
import com.bidme.utils.Colors;

import java.util.ArrayList;
import java.util.Random;

public class PostAunctionAdapter extends RecyclerView.Adapter<PostAunctionAdapter.MyViewHolder> {
    private PostAuction postAuction;
    private Context mContext;

    private ArrayList<CategoryDTO> CategoryDTOArrayList;
    private LayoutInflater layoutInflater;


    public PostAunctionAdapter(Context mContext
            , ArrayList<CategoryDTO> CategoryDTOArrayList) {
        this.mContext = mContext;

        this.CategoryDTOArrayList = CategoryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemPostAunctionBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_post_aunction, parent, false);
        return new PostAunctionAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        Glide
                .with(mContext)
                .load(CategoryDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.civCat);
        myViewHolder.binding.osTexts.setText(CategoryDTOArrayList.get(position).getCat_title());

        int i = new Random().nextInt(21);

        GradientDrawable bgShape = (GradientDrawable) myViewHolder.binding.llcat.getBackground();
        bgShape.setColor(Color.parseColor("#" + Colors.mColors[i]));
        myViewHolder.binding.osTexts.setTextColor(Color.parseColor("#" + Colors.mColors[i]));
        myViewHolder.binding.llAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, PostSubAunction.class);
                in.putExtra(Const.GET_CAT_ID, CategoryDTOArrayList.get(position).getCat_id());
                in.putExtra(Const.CAT_TITLE, CategoryDTOArrayList.get(position).getCat_title());
                mContext.startActivity(in);


            }
        });


    }

    @Override
    public int getItemCount() {
        return CategoryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPostAunctionBinding binding;

        public MyViewHolder(@NonNull ItemPostAunctionBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;


        }


    }
}