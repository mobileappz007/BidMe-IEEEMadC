package com.bidme.activity.social;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.adapter.SubHistoryAdapter;
import com.bidme.databinding.FregmentSubscriptionHistoryBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.SubHistoryDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllSubscriptionHistory extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;
    private String TAG = AllSubscriptionHistory.class.getCanonicalName();
    private FregmentSubscriptionHistoryBinding binding;
    private Dashboard dashboard;
    private SubHistoryAdapter subHistoryAdapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SubHistoryDTO> subHistoryDTOArrayList;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=AllSubscriptionHistory.this;
       binding= DataBindingUtil. setContentView(this,R.layout.fregment_subscription_history);
       binding.icBack.setOnClickListener(this);

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        params.put(Const.USER_PUB_ID,userDTO.getUser_pub_id());

        binding.svHistory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    subHistoryAdapter.filter(s);
                }catch (Exception e){

                }
                return false;

            }
        });
        getSubscriptionHistory();
    }

    private void getSubscriptionHistory() {

        new HttpsRequest(Const.GET_SUB_HISTORY, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    binding.svHistory.setVisibility(View.VISIBLE);
                    binding.tvNo.setVisibility(View.GONE);
                    try {

                        subHistoryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<SubHistoryDTO>>() {
                        }.getType();

                        subHistoryDTOArrayList = (ArrayList<SubHistoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        binding.rvhistory.setLayoutManager(layoutManager);
                        binding.rvhistory.setHasFixedSize(true);
                        subHistoryAdapter = new SubHistoryAdapter(mContext, subHistoryDTOArrayList);
                        binding.rvhistory.setAdapter(subHistoryAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    binding.svHistory.setVisibility(View.GONE);
                    binding.tvNo.setVisibility(View.VISIBLE);
                   // ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icBack:
                onBackPressed();
                break;
        }

    }
}
