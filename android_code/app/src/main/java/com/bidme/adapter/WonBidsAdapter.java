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
import com.bidme.activity.aution.ViewAuction;
import com.bidme.databinding.AdapterWonBidsBinding;
import com.bidme.fragment.dashboad.MyWonBid;
import com.bidme.interfaces.Const;
import com.bidme.model.WonBidDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;

import static com.bidme.R.layout.adapter_won_bids;

public class WonBidsAdapter extends RecyclerView.Adapter<WonBidsAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<WonBidDTO> wonBidDTOArrayList;
    private AdapterWonBidsBinding binding;
    private MyWonBid myWonBid;


    public WonBidsAdapter(Context mContext, ArrayList<WonBidDTO> wonBidDTOArrayList,MyWonBid myWonBid) {
        this.mContext = mContext;
        this.wonBidDTOArrayList = wonBidDTOArrayList;
        this.myWonBid=myWonBid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, adapter_won_bids, parent, false);

        return new WonBidsAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.binding.tvtitle.setText(ProjectUtils.capWordFirstLetter(wonBidDTOArrayList.get(i).getTitle()));
        myViewHolder.binding.tvPrice.setText(wonBidDTOArrayList.get(i).getPrice()+" "+wonBidDTOArrayList.get(i).getCurrency_code());
        myViewHolder.binding.tvProPrice.setText(wonBidDTOArrayList.get(i).getPro_price()+" "+wonBidDTOArrayList.get(i).getCurrency_code());
        myViewHolder.binding.tvDate.setText("Bid Date"+" - "+ ProjectUtils.changeDateFormate(wonBidDTOArrayList.get(i).getCreated_at()));
        try {
            Glide
                    .with(mContext)
                    .load(wonBidDTOArrayList.get(i).getImage_cust().get(0).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(myViewHolder.binding.iv1);
        }catch(Exception e){
        }

        myViewHolder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(mContext, ViewAuction.class);

                inn.putExtra(Const.Pro_pub_id, wonBidDTOArrayList.get(i).getPro_pub_id());
                inn.putExtra(Const.WON_INDEX, 1);
                inn.putExtra(Const.FLAG, "2");
                //inn.putExtra(Const.MY_AUCTIONDTO, getAllBidDTOArrayList.get(position));
                mContext.startActivity(inn);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wonBidDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private AdapterWonBidsBinding binding;

        public MyViewHolder(@NonNull AdapterWonBidsBinding itembinding) {
            super(itembinding.getRoot());
            this.binding=itembinding;
        }
    }
}
