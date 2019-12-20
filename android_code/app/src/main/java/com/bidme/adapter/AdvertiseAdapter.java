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
import com.bidme.databinding.AdapterAdvertiseAllBinding;
import com.bidme.model.AdvertiseAllDTO;

import java.util.ArrayList;

public class AdvertiseAdapter extends RecyclerView.Adapter<AdvertiseAdapter.MyViewHolder> {

    Context mContext;

    ArrayList<AdvertiseAllDTO>advertiseDTOArrayList;
    LayoutInflater layoutInflater;

    public AdvertiseAdapter( Context mContext, ArrayList<AdvertiseAllDTO> advertiseDTOArrayList) {
        this.mContext = mContext;
        this.advertiseDTOArrayList = advertiseDTOArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
       AdapterAdvertiseAllBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_advertise_all, parent, false);
        return new MyViewHolder(binding);



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.binding.tvProductname.setText(advertiseDTOArrayList.get(position).getTitle());
        myViewHolder.binding.tvPrice.setText(advertiseDTOArrayList.get(position).getPrice());
      myViewHolder.binding.tvTime.setText(advertiseDTOArrayList.get(position).getStatus());
        Glide
                .with(mContext)
                .load(advertiseDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.ivProduct);



        myViewHolder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ViewAdvertise.class);
                in.putExtra("add_pro_id",advertiseDTOArrayList.get(position).getAdd_pro_id());
                mContext.startActivity(in);


            }
        });


    }



    @Override
    public int getItemCount() {
        return advertiseDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    AdapterAdvertiseAllBinding binding;


        public MyViewHolder( AdapterAdvertiseAllBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}
