package com.bidme.fragment.dashboad;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.adapter.MyadsAdapter;
import com.bidme.databinding.FregmentMyAdsBinding;
import com.bidme.model.MyAdsDTO;

import java.util.ArrayList;

public class Ads extends Fragment implements View.OnClickListener {
    private View view;

    private FregmentMyAdsBinding binding;
    private ArrayList<MyAdsDTO> advertiseListDTOArrayList;
    private GridLayoutManager manager;
    private MyadsAdapter myadsAdapter;
    private Dashboard dashboard;
    private Context mcontext;


    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        if (mcontext instanceof Activity) {
            this.dashboard = (Dashboard) mcontext;
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myadsAdapter = new MyadsAdapter(getActivity(), advertiseListDTOArrayList);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fregment_my_ads, container, false);
        view = binding.getRoot();
        setUIAction(view);


        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycleview1.setAdapter(myadsAdapter);
    }


    public void onDetach() {
        super.onDetach();
        this.dashboard = null;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void setUIAction(View view) {
        binding.tvSelling.setOnClickListener(this);
        binding.tvFavourites.setOnClickListener(this);
        binding.tvSold.setOnClickListener(this);
       /* advertiseListDTOArrayList = new ArrayList<>();
        advertiseListDTOArrayList.add(new MyAdsDTO("Smart watch", "4 days ago", "500", R.drawable.smartwatch));
        advertiseListDTOArrayList.add(new MyAdsDTO("Car", "2 days ago", "100000", R.drawable.car123));
        advertiseListDTOArrayList.add(new MyAdsDTO("Bike", "3 days ago", "78000", R.drawable.bikesss));
        advertiseListDTOArrayList.add(new MyAdsDTO("Home", "6 days ago", "250000", R.drawable.homess));
        advertiseListDTOArrayList.add(new MyAdsDTO("Mobile", "5 days ago", "12000", R.drawable.mobiless));*/
        manager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        binding.recycleview1.setLayoutManager(manager);
        myadsAdapter = new MyadsAdapter(getActivity(), advertiseListDTOArrayList);
        binding.recycleview1.setAdapter(myadsAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelling:
                setSelected(true, false, false);
                break;

            case R.id.tvSold:
                setSelected(false, true, false);
                break;

            case R.id.tvFavourites:
                setSelected(false, false, true);
                break;


        }

    }

    private void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN) {

        if (firstBTN) {
            binding.tvSellingSelect.setVisibility(View.VISIBLE);
            binding.tvSoldSelect.setVisibility(View.GONE);
            binding.tvFavouritesSelect.setVisibility(View.GONE);

        }
        if (secondBTN) {
            binding.tvSellingSelect.setVisibility(View.GONE);
            binding.tvSoldSelect.setVisibility(View.VISIBLE);
            binding.tvFavouritesSelect.setVisibility(View.GONE);

        }
        if (thirdBTN) {
            binding.tvSellingSelect.setVisibility(View.GONE);
            binding.tvSoldSelect.setVisibility(View.GONE);
            binding.tvFavouritesSelect.setVisibility(View.VISIBLE);

        }
        binding.tvSellingSelect.setSelected(firstBTN);
        binding.tvSoldSelect.setSelected(secondBTN);
        binding.tvFavouritesSelect.setSelected(secondBTN);

    }
}

