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
import com.bidme.adapter.MyAuctionAdapter;
import com.bidme.databinding.FregmentMyAunctionAunctionsBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyAutionDTO;
import com.bidme.model.UserDTO;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAuctions extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = MyAuctions.class.getCanonicalName();
    private FregmentMyAunctionAunctionsBinding binding;
    private ArrayList<MyAutionDTO> myAutionDTOArrayList;
    private MyAuctionAdapter myAuctionAdapter;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private GridLayoutManager layoutManager;
    private String user_pub_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fregment_my_aunction_aunctions, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        user_pub_id=userDTO.getUser_pub_id();
        params.put(Const.USER_PUB_ID, user_pub_id);

        setUiAction();
        return binding.getRoot();
    }

    private void setUiAction() {
        layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        binding.rvMyAuction.setLayoutManager(layoutManager);
        binding.swipeRefreshMyauction.setOnRefreshListener(this);
        binding.swipeRefreshMyauction.post(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Log.e("Runnable", "FIRST");
                                                   if (NetworkManager.isConnectToInternet(getActivity())) {
                                                       binding.swipeRefreshMyauction.setRefreshing(true);
                                                       getMyAuction();
                                                   } else {
                                                      // ProjectUtils.InternetAlertDialog(getActivity());
                                                   }
                                               }
                                           }
        );
    }

    private void getMyAuction() {
        new HttpsRequest(Const.GET_MY_AUNCTION, params, getActivity()).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                binding.swipeRefreshMyauction.setRefreshing(false);


                if (flag) {

                    try {
                        myAutionDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<MyAutionDTO>>() {
                        }.getType();
                        myAutionDTOArrayList = (ArrayList<MyAutionDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();
                      //  ProjectUtils.showToast(getActivity(), msg);

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
        myAuctionAdapter = new MyAuctionAdapter(getActivity(), myAutionDTOArrayList);
        binding.rvMyAuction.setAdapter(myAuctionAdapter);
    }

  @Override
    public void onResume() {
        super.onResume();
        getMyAuction();
    }

    @Override
    public void onRefresh() {
        getMyAuction();
    }
}
