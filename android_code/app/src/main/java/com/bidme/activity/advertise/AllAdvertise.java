package com.bidme.activity.advertise;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bidme.activity.FilterActivity;
import com.bidme.activity.SearchActivity;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.R;
import com.bidme.adapter.AdvertiseAdapter;
import com.bidme.databinding.ActivityAllAdvertiseBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.model.AdvertiseAllDTO;
import com.bidme.utils.ProjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllAdvertise extends AppCompatActivity {
    private static final String TAG = AllAdvertise.class.getCanonicalName();
    private RecyclerView.LayoutManager layoutManager;
    Context mContext;
    private ActivityAllAdvertiseBinding binding;
    ArrayList<AdvertiseAllDTO> advertiseDTOArrayList;
    AdvertiseAdapter advertiseAdapter;
    private AdvertiseAllDTO advertiseAllDTO;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AllAdvertise.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_advertise);
        setUIAction();
        binding.iAHN.ivMenu.setVisibility(View.GONE);
        binding.iAHN.ivBack.setVisibility(View.VISIBLE);
        binding.iAHN.tvindrapuri.setVisibility(View.VISIBLE);
        binding.iAHN.spinner1.setVisibility(View.VISIBLE);

        binding.iAHN.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, Dashboard.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.putExtra("index",0);
                startActivity(in);
            }
        });
        binding.iAHN.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, FilterActivity.class);
                startActivity(in);

            }
        });
        binding.iAHN.ivSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, SearchActivity.class);
                startActivity(in);

            }
        });

    }

    private void setUIAction() {
        advertiseDTOArrayList = new ArrayList<>();
       /* advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());
        advertiseDTOArrayList.add(new AdvertiseAllDTO());*/

        layoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
        binding.recycleview1.setHasFixedSize(true);
        binding.recycleview1.setLayoutManager(layoutManager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAdvertise();
    }

    private void getAllAdvertise() {
        new HttpsRequest(Const.GET_ALL_ADVERTISE, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {
                        binding.recycleview1.setVisibility(View.VISIBLE);
                        advertiseDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AdvertiseAllDTO>>() {
                        }.getType();
                        advertiseDTOArrayList = (ArrayList<AdvertiseAllDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        advertiseAdapter = new AdvertiseAdapter(AllAdvertise.this, advertiseDTOArrayList);
                        binding.recycleview1.setAdapter(advertiseAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);


                }

            }
        });

    }


}

