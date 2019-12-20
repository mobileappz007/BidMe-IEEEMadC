package com.bidme.myauctionfregment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.MyBidAdapter;

import com.bidme.databinding.FregmentMyAunctionBidsBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyBidDTO;
import com.bidme.model.UserDTO;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAds extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = MyAds.class.getCanonicalName();

    private MyBidAdapter myBidAdapter;
    private FregmentMyAunctionBidsBinding binding;
    private ArrayList<MyBidDTO> myBidsDTOArrayList;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fregment_my_aunction_bids, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        setUiAction();
        return binding.getRoot();
    }

    private void setUiAction() {
        layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        binding.rvMyBids.setLayoutManager(layoutManager);

        binding.swipeRefreshMybids.setOnRefreshListener(this);
        binding.swipeRefreshMybids.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                Log.e("Runnable", "FIRST");
                                                if (NetworkManager.isConnectToInternet(getActivity())) {
                                                    binding.swipeRefreshMybids.setRefreshing(true);
                                                    getMyBid();


                                                } else {
                                                    ProjectUtils.InternetAlertDialog(getActivity());
                                                }
                                            }
                                        }
        );
    }

    public void getMyBid() {
        new HttpsRequest(Const.GET_MY_BID, params, getActivity()).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                binding.swipeRefreshMybids.setRefreshing(false);
                if (flag) {
                  //  ProjectUtils.showToast(getActivity(), msg);
                    try {
                        myBidsDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<MyBidDTO>>() {
                        }.getType();

                        myBidsDTOArrayList = (ArrayList<MyBidDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);
                   // ProjectUtils.showToast(getActivity(), msg);
                }
            }
        }));


    }

    public void showData() {
        myBidAdapter = new MyBidAdapter(getActivity(), myBidsDTOArrayList, MyAds.this);
        binding.rvMyBids.setAdapter(myBidAdapter);
    }

    @Override
    public void onRefresh() {
        getMyBid();
    }
}
