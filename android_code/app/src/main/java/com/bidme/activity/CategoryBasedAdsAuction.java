package com.bidme.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.aution.AutionAll;
import com.bidme.activity.aution.PostAuction;
import com.bidme.adapter.AdvertiseAdapterDashboard;

import com.bidme.adapter.AutionAllAdapter;
import com.bidme.adapter.FilterAunctionsAdapter;
import com.bidme.databinding.ActivityCategoryBasedAdsAuctionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.AdvertiseDTO;
import com.bidme.model.AutionAllDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryBasedAdsAuction extends AppCompatActivity implements View.OnClickListener {

    private String TAG = CategoryBasedAdsAuction.class.getCanonicalName();
    private ActivityCategoryBasedAdsAuctionBinding binding;
    private Context mContext;
    private UserDTO userDTO;
    private String catid;
    private String maxPrice;
    private String minPrice;
    private String progress;
    private SharedPrefrence sharedPrefrence;
    private ArrayList<AutionAllDTO> filterDTOArrayList;
    private FilterAunctionsAdapter filterAunctionsAdapter;
    private ArrayList<AdvertiseDTO> advertiseDTOList;
    private AdvertiseAdapterDashboard advertiseAdapterDashboard;
    private RecyclerView.LayoutManager linearLayoutManager;
    private RecyclerView.LayoutManager linearLayoutManager2;
    private ImageView ivmenu, search, filer, ivback;
    private TextView title;
    private AutionAllAdapter autionAllAdapter;
    private View navHeader;
    private String catName;
    String lattitude = "";
    String longitude = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_based_ads_auction);
        mContext = CategoryBasedAdsAuction.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);

        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        catid = getIntent().getStringExtra(Const.GET_CAT_ID);
        maxPrice = getIntent().getStringExtra(Const.MAX_ID);
        minPrice = getIntent().getStringExtra(Const.MIN_ID);
        progress = getIntent().getStringExtra(Const.DISTANCE_ID);
        catName = getIntent().getStringExtra(Const.CAT_TITLE);


        binding.fabPostAuctionCat.setOnClickListener(this);

        binding.tvViewMore.setOnClickListener(this);

        navHeader = findViewById(R.id.iAHN);
        ivmenu = navHeader.findViewById(R.id.ivMenu);
        search = navHeader.findViewById(R.id.ivSearch1);
        filer = navHeader.findViewById(R.id.ivFilter);
        ivback = navHeader.findViewById(R.id.ivBack);
        title = navHeader.findViewById(R.id.tvtitlemain);
        ivmenu.setVisibility(View.GONE);
        ivback.setVisibility(View.VISIBLE);
        search.setVisibility(View.GONE);
        filer.setVisibility(View.GONE);
        ivback.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText(catName);

        binding.chatSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                try {
                    autionAllAdapter.filter(s);
                } catch (Exception e) {

                }
                return false;
            }
        });
        ;


        //ivmenu.setOnClickListener(this);
        search.setOnClickListener(this);
        filer.setOnClickListener(this);

        getAllFilterAunction();
        // getAdvertiseList();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFilterAunction();

    }

    private void getAllFilterAunction() {

        new HttpsRequest(Const.GET_ALL_AUCTION, getfilterdetail(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        filterDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<AutionAllDTO>>() {
                        }.getType();

                        filterDTOArrayList = (ArrayList<AutionAllDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        linearLayoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
                        binding.rvAuction.setLayoutManager(linearLayoutManager);
                        autionAllAdapter = new AutionAllAdapter(mContext, filterDTOArrayList);
                        binding.rvAuction.setAdapter(autionAllAdapter);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rlAllAuc.setVisibility(View.GONE);
                    binding.fabPostAuctionCat.setVisibility(View.GONE);

                    binding.chatSearch.setVisibility(View.GONE);
                    //ProjectUtils.showToast(mContext, msg);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab_PostAuctionCat:
                startActivity(new Intent(mContext, PostAuction.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;

        /*    case R.id.fab_PostAds:
                startActivity(new Intent(mContext, PostAuction.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;*/
            case R.id.tvViewMore:
                Intent intent2 = new Intent(CategoryBasedAdsAuction.this, AutionAll.class);
                startActivity(intent2);

                break;
            case R.id.ivBack:
                finish();
                break;


          /*  case R.id.ivMenu:
                Intent in = new Intent(CategoryBasedAdsAuction.this, Dashboard.class);
                startActivity(in);
                break;*/
            case R.id.ivSearch1:
                Intent intent = new Intent(CategoryBasedAdsAuction.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ivFilter:
                Intent intent1 = new Intent(CategoryBasedAdsAuction.this, FilterActivity.class);
                startActivity(intent1);
                break;


        }

    }


    private HashMap<String, String> getfilterdetail() {
        HashMap<String, String> params = new HashMap<>();

        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        params.put(Const.GET_CAT_ID, catid);
        if (maxPrice != null && minPrice != null) {
            params.put(Const.MAX_ID, maxPrice);
            params.put(Const.MIN_ID, minPrice);
        }
            if (progress != null) {
                if (!progress.equals("0")) {
                    params.put(Const.DISTANCE_ID, progress);
                    params.put(Const.LONGITUDE, sharedPrefrence.getValue(Const.LONGITUDE));
                    params.put(Const.LATITUDE, sharedPrefrence.getValue(Const.LATITUDE));

                }
            }


        Log.e("getfilterdetail", params.toString());
        return params;
    }

}
