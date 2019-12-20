package com.bidme.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.activity.advertise.ViewAdvertise;
import com.bidme.databinding.AdapterItemMyAdsBinding;
import com.bidme.model.MyAdsDTO;

import java.util.ArrayList;

public class MyadsAdapter extends RecyclerView.Adapter<MyadsAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<MyAdsDTO>advertiseListDTOArrayList;

    public MyadsAdapter(Context mContext, ArrayList<MyAdsDTO> advertiseListDTOArrayList) {
        this.mContext = mContext;
        this.advertiseListDTOArrayList = advertiseListDTOArrayList;
    }

    @NonNull
    @Override
    public MyadsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterItemMyAdsBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_item_my_ads, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyadsAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.binding.tvtitle.setText(advertiseListDTOArrayList.get(position).getProductname());
        myViewHolder.binding.tvDescription.setText(advertiseListDTOArrayList.get(position).getProductduration());
        myViewHolder.binding.tvPrice.setText(advertiseListDTOArrayList.get(position).getProductprice());
        Glide
                .with(mContext)
                .load(advertiseListDTOArrayList.get(position).getProductimage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.iv1);

        myViewHolder.binding.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ViewAdvertise.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return advertiseListDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterItemMyAdsBinding binding;


        public MyViewHolder(@NonNull AdapterItemMyAdsBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}
