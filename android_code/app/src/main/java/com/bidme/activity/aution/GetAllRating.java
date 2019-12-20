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
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.AdapterAllRating;
import com.bidme.databinding.ActivityGetAllRatingBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.GetRatingDTO;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllRating extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GetAllRating.class.getCanonicalName();
    private Context mContext;
    private ArrayList<GetRatingDTO> getRatingDTOArrayList;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterAllRating adapterAllRating;
    private ActivityGetAllRatingBinding binding;
    private HashMap<String, String> params = new HashMap<>();
    private String userId;
    private String pro_pub_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = GetAllRating.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_all_rating);
        userId = getIntent().getStringExtra(Const.USER_PUB_ID);
        pro_pub_id = getIntent().getStringExtra(Const.Pro_pub_id);

        params.put(Const.USER_PUB_ID, userId);
        params.put(Const.Pro_pub_id, pro_pub_id);
        binding.simpleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllRating();
            }
        });

        getAllRating();
        binding.icRatBack.setOnClickListener(this);

    }

    private void getAllRating() {
        new HttpsRequest(Const.GET_ALL_RATING, params, mContext).stringPost(TAG,
                new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        binding.simpleSwipeRefreshLayout.setRefreshing(false);

                        if (flag) {
                            try {
                                binding.rvAllRating.setVisibility(View.VISIBLE);
                                getRatingDTOArrayList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<GetRatingDTO>>() {
                                }.getType();
                                getRatingDTOArrayList = (ArrayList<GetRatingDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                                layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                                binding.rvAllRating.setLayoutManager(layoutManager);
                                adapterAllRating = new AdapterAllRating(mContext, getRatingDTOArrayList,"1");
                                binding.rvAllRating.setAdapter(adapterAllRating);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            binding.rvAllRating.setVisibility(View.GONE);
                            binding.nodata.setVisibility(View.VISIBLE);
                            binding.simpleSwipeRefreshLayout.setVisibility(View.GONE);
                            binding.simpleSwipeRefreshLayout.setRefreshing(false);

                            ProjectUtils.showToast(mContext, msg);

                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icRatBack:
                finish();
                break;
        }

    }
}
