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
import com.bidme.adapter.MyFavAdapter;
import com.bidme.databinding.FregementMyAunctionFavBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyFavDTO;
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

public class MyFav extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = MyFav.class.getCanonicalName();

    private FregementMyAunctionFavBinding binding;
    private ArrayList<MyFavDTO> myFavDTOArrayList;
    private MyFavAdapter myFavAdapter;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fregement_my_aunction_fav, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        setUiAction();
        return binding.getRoot();
    }

    private void setUiAction() {
        layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        binding.rvMyfav.setLayoutManager(layoutManager);

        binding.swipeRefreshMyfav.setOnRefreshListener(this);
        binding.swipeRefreshMyfav.post(new Runnable() {
                                           @Override
                                           public void run() {

                                               Log.e("Runnable", "FIRST");
                                               if (NetworkManager.isConnectToInternet(getActivity())) {
                                                   binding.swipeRefreshMyfav.setRefreshing(true);
                                                   getMyFavourite();


                                               } else {
                                                   ProjectUtils.InternetAlertDialog(getActivity());
                                               }
                                           }
                                       }
        );
    }

    private void getMyFavourite() {

        new HttpsRequest(Const.GET_MYFAV, params, getActivity()).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                binding.swipeRefreshMyfav.setRefreshing(false);
                if (flag) {
                    // ProjectUtils.showToast(getActivity(), msg);
                    try {

                        myFavDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<MyFavDTO>>() {
                        }.getType();
                        myFavDTOArrayList = (ArrayList<MyFavDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

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
        myFavAdapter = new MyFavAdapter(getActivity(), myFavDTOArrayList);
        binding.rvMyfav.setAdapter(myFavAdapter);
    }

    @Override
    public void onRefresh() {
        getMyFavourite();

    }
}
