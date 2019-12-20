package com.bidme.fragment.dashboad;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;

import com.bidme.databinding.FregmentMyAuctionBinding;
import com.bidme.myauctionfregment.MyAds;
import com.bidme.myauctionfregment.MyAuctions;
import com.bidme.myauctionfregment.MyFav;

public class MyAutions extends Fragment implements View.OnClickListener {
    private FregmentMyAuctionBinding binding;
    private Dashboard dashboard;
    private FragmentManager fragmentManager;
    private MyAuctions myAuctions = new MyAuctions();
    private MyAds myAds = new MyAds();
    private MyFav myFav = new MyFav();
    private MyWonBid myWonBid = new MyWonBid();
    private String pro_pub_id;


    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        if (mcontext instanceof Activity) {
            this.dashboard = (Dashboard) mcontext;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fregment_my_auction, container, false);
        fragmentManager = getChildFragmentManager();
        setUiAction();







      return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Value is here","-------------------------------------"+dashboard.number);
    }



    private void setUiAction() {
        fragmentManager.beginTransaction().add(R.id.container, myAuctions, "myAuctions").commit();
        binding.tvmyauction.setOnClickListener(this);
        binding.tvmybids.setOnClickListener(this);
        binding.tvFavourites.setOnClickListener(this);
        binding.tvwonbids.setOnClickListener(this);
        test();
    }
    public void test(){
        if (dashboard.number==2){
            setSelected(false, true, false,false);
            fragmentManager.beginTransaction().replace(R.id.container, myAds, "myAds").commit();

        }else {
            setSelected(true, false, false,false);
            fragmentManager.beginTransaction().replace(R.id.container, myAuctions, "myAuctions").commit();
        }


    }

    public void onDetach() {
        super.onDetach();
        this.dashboard = null;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvmyauction:
                setSelected(true, false, false,false);
                fragmentManager.beginTransaction().replace(R.id.container, myAuctions, "myAuctions").commit();
                break;
            case R.id.tvmybids:
                setSelected(false, true, false,false);
                fragmentManager.beginTransaction().replace(R.id.container, myAds, "myAds").commit();
                break;
            case R.id.tvFavourites:
                setSelected(false, false, true,false);
                fragmentManager.beginTransaction().replace(R.id.container, myFav, "myFav").commit();
                break;
          case R.id.tvwonbids:
                setSelected(false, false, false,true);
                fragmentManager.beginTransaction().replace(R.id.container, myWonBid, "myWonBid").commit();
                break;

        }

    }


    private void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN,boolean fourBTN) {

        if (firstBTN) {
            binding.tvAuctionSelect.setVisibility(View.VISIBLE);
            binding.tvBidsSelect.setVisibility(View.GONE);
            binding.tvFavouritesSelect.setVisibility(View.GONE);
            binding.tvwonBidsSelect.setVisibility(View.GONE);


        }
        if (secondBTN) {
            binding.tvAuctionSelect.setVisibility(View.GONE);
            binding.tvBidsSelect.setVisibility(View.VISIBLE);
            binding.tvFavouritesSelect.setVisibility(View.GONE);
            binding.tvwonBidsSelect.setVisibility(View.GONE);


        }
        if (thirdBTN) {
            binding.tvAuctionSelect.setVisibility(View.GONE);
            binding.tvBidsSelect.setVisibility(View.GONE);
            binding.tvFavouritesSelect.setVisibility(View.VISIBLE);
            binding.tvwonBidsSelect.setVisibility(View.GONE);


        }if (fourBTN) {
            binding.tvAuctionSelect.setVisibility(View.GONE);
            binding.tvBidsSelect.setVisibility(View.GONE);
            binding.tvFavouritesSelect.setVisibility(View.GONE);
            binding.tvwonBidsSelect.setVisibility(View.VISIBLE);

        }
        binding.tvAuctionSelect.setSelected(firstBTN);
        binding.tvBidsSelect.setSelected(secondBTN);
        binding.tvFavouritesSelect.setSelected(thirdBTN);
        binding.tvwonBidsSelect.setSelected(fourBTN);

    }
}

