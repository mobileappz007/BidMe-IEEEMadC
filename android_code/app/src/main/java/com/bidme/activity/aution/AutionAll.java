package com.bidme.activity.aution;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bidme.activity.FilterActivity;
import com.bidme.activity.SearchActivity;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.R;
import com.bidme.adapter.AutionAllAdapter;
import com.bidme.databinding.ActivityAutionAllBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.model.AutionAllDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutionAll extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AutionAll.class.getCanonicalName();
    private Context mContext;
    private ActivityAutionAllBinding binding;
    ArrayList<AutionAllDTO> allDTOArrayList;
    private AutionAllAdapter autionAllAdapter;
    private GridLayoutManager layoutManager;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AutionAll.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_aution_all);

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());

        binding.iAHN.ivMenu.setVisibility(View.GONE);
        binding.iAHN.ivBack.setVisibility(View.VISIBLE);
        binding.iAHN.tvindrapuri.setVisibility(View.GONE);
        binding.iAHN.spinner1.setVisibility(View.GONE);
        binding.iAHN.tvtitlemain.setVisibility(View.VISIBLE);
        binding.iAHN.tvtitlemain.setText("All Aunctions");

        binding.iAHN.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        binding.recycleview1.setOnClickListener(this);
        getAllAuction();


        // timer();
       // test();
    }
 /*   public void test(){
        try {
            if (getIntent().getStringExtra("OK").equals("OK")){
                Log.e("hhhhhhhhhhhhhhhhhh",params.toString());
                getAllAuction();
            }
        }
        catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        getAllAuction();
    }

    public void getAllAuction() {
        new HttpsRequest(Const.GET_ALL_Auctions, params, mContext).stringPost(TAG,
                new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        ProjectUtils.pauseProgressDialog();

                        if (flag) {
                            try {
                                binding.recycleview1.setVisibility(View.VISIBLE);

                                allDTOArrayList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<AutionAllDTO>>() {
                                }.getType();
                                allDTOArrayList = (ArrayList<AutionAllDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                                layoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
                                binding.recycleview1.setLayoutManager(layoutManager);
                                autionAllAdapter = new AutionAllAdapter(mContext, allDTOArrayList);
                                binding.recycleview1.setAdapter(autionAllAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                           // ProjectUtils.showToast(mContext, msg);
                            binding.recycleview1.setVisibility(View.GONE);

                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {

    }


    public void timer(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getAllAuction();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }




}
