package com.bidme.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.databinding.AdapterAdvertiseBinding;
import com.bidme.model.AdvertiseDTO;

import java.util.ArrayList;

public class AdvertiseAdapterDashboard extends RecyclerView.Adapter<AdvertiseAdapterDashboard.MyViewHolder> {
    private Context mContext;
    private ArrayList<AdvertiseDTO> advertiseDTOArrayList;
    private AdapterAdvertiseBinding binding;
    private LayoutInflater layoutInflater;

    public AdvertiseAdapterDashboard(Context mContext, ArrayList<AdvertiseDTO> advertiseDTOArrayList) {
        this.mContext = mContext;
        this.advertiseDTOArrayList = advertiseDTOArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if(layoutInflater==null){
            layoutInflater= LayoutInflater.from(parent.getContext());
        }
        AdapterAdvertiseBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_advertise,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int position) {
        Glide
                .with(mContext)
                .load(advertiseDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.ivProduct);
        myViewHolder.binding.tvProductname.setText(advertiseDTOArrayList.get(position).getTitle());
        myViewHolder.binding.tvPrice.setText(advertiseDTOArrayList.get(position).getPrice());


    }

    @Override
    public int getItemCount() {

        return advertiseDTOArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        AdapterAdvertiseBinding binding;


        public MyViewHolder(AdapterAdvertiseBinding  itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
