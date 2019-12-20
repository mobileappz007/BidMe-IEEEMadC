package com.bidme.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.databinding.AdapterItemFilterAunctionsBinding;
import com.bidme.model.AutionAllDTO;

import java.util.ArrayList;

import static com.bidme.R.layout.adapter_item_filter_aunctions;

public class FilterAunctionsAdapter extends RecyclerView.Adapter<FilterAunctionsAdapter.MyViewHolder> {
   private Context mContext;
   private ArrayList<AutionAllDTO>filterDTOArrayList;
   private LayoutInflater layoutInflater;

   private AdapterItemFilterAunctionsBinding binding;

    public FilterAunctionsAdapter(Context mContext, ArrayList<AutionAllDTO> filterDTOArrayList) {
        this.mContext = mContext;
        this.filterDTOArrayList = filterDTOArrayList;
    }

    @NonNull
    @Override
    public FilterAunctionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if(layoutInflater!=null){
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.getContext());
            }
            AdapterItemFilterAunctionsBinding binding = DataBindingUtil.inflate(layoutInflater, adapter_item_filter_aunctions, parent, false);


        }
        return new FilterAunctionsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAunctionsAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.binding.tvProductname.setText(filterDTOArrayList.get(position).getTitle());
        myViewHolder.binding.tvPrice.setText(filterDTOArrayList.get(position).getPrice());
        myViewHolder.binding.tvcurrency.setText(filterDTOArrayList.get(position).getCurrency_code());
        myViewHolder.binding.tvAddress.setText(filterDTOArrayList.get(position).getAddress());
        myViewHolder.binding.tvLocation.setText(filterDTOArrayList.get(position).getLatitude());
        Glide.with(mContext)
                .load(filterDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.image);


    }

    @Override
    public int getItemCount() {
        return filterDTOArrayList.size();
    }

    public class MyViewHolder extends ViewHolder {
        AdapterItemFilterAunctionsBinding binding;


        public MyViewHolder(@NonNull  AdapterItemFilterAunctionsBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
