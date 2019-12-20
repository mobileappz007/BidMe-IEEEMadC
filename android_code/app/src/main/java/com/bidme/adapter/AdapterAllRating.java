package com.bidme.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.databinding.AdapterAllRatingBinding;
import com.bidme.model.GetRatingDTO;
import com.bidme.model.UserDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;

public class AdapterAllRating extends RecyclerView.Adapter<AdapterAllRating.MyViewHolder> {
    private Context mContext;
    private ArrayList<GetRatingDTO>getRatingDTOArrayList;
    private LayoutInflater layoutInflater;
     private AdapterAllRatingBinding binding;
     private UserDTO userDTO;
    private String tag;
    public AdapterAllRating(Context mContext, ArrayList<GetRatingDTO> getRatingDTOArrayList,String tag) {
        this.mContext = mContext;
        this.getRatingDTOArrayList = getRatingDTOArrayList;
        this.tag=tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_all_rating, parent, false);
        return new AdapterAllRating.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {

        myViewHolder.binding.userName.setText(ProjectUtils.capWordFirstLetter(getRatingDTOArrayList.get(position).getName()));
        myViewHolder.binding.tvComment.setText(getRatingDTOArrayList.get(position).getComment());
        myViewHolder.binding.simpleRatingBar.setRating(Float.parseFloat(getRatingDTOArrayList.get(position).getStar()));
        Glide
                .with(mContext)
                .load(getRatingDTOArrayList.get(position).getUser_image())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.civCat);


    }

    @Override
    public int getItemCount() {

        if (tag.equals("5")) {
            return Math.min(getRatingDTOArrayList.size(), 5);
        } else {
            return getRatingDTOArrayList.size();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterAllRatingBinding binding;

        public MyViewHolder(@NonNull    AdapterAllRatingBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}
