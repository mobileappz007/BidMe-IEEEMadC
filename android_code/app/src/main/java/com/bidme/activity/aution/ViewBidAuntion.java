package com.bidme.activity.aution;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.bidme.R;
import com.bidme.adapter.ViewAunctionBidAdapter;
import com.bidme.databinding.ActivityViewBidAuntionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.BidsDTO;
import com.bidme.model.UserDTO;
import com.bidme.model.ViewAllAuctionDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewBidAuntion extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ViewBidAuntion.class.getCanonicalName();
    private Context mContext;
    private ArrayList<BidsDTO>bidsDTOArrayList;
    private ActivityViewBidAuntionBinding bidAuntionBinding;
    private RecyclerView.LayoutManager layoutManager;
    private ViewAunctionBidAdapter viewAunctionBidAdapter;
    private ViewAllAuctionDTO viewAllAuctionDTO;
    HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=ViewBidAuntion.this;
        bidAuntionBinding= DataBindingUtil. setContentView(this,R.layout.activity_view_bid_auntion);
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        bidAuntionBinding.simpleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getSingleAuction();

            }

        });


        id=getIntent().getStringExtra(Const.Pro_pub_id);
        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        params.put(Const.Pro_pub_id,id);


        getSingleAuction();

    }


    private void getSingleAuction() {

        new HttpsRequest(Const.GET_SINGLE_AUNCTION, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                bidAuntionBinding.simpleSwipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    try {
                        viewAllAuctionDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ViewAllAuctionDTO.class);
                        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        bidAuntionBinding.rvBids.setLayoutManager(layoutManager);
                        viewAunctionBidAdapter = new ViewAunctionBidAdapter(viewAllAuctionDTO.getBids(), mContext,"5");
                        bidAuntionBinding.rvBids.setAdapter(viewAunctionBidAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    bidAuntionBinding.nodata.setVisibility(View.VISIBLE);
                    bidAuntionBinding.simpleSwipeRefreshLayout.setVisibility(View.GONE);
                    bidAuntionBinding.simpleSwipeRefreshLayout.setRefreshing(false);
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icBidBack:
                finish();
                break;
        }

    }
}
