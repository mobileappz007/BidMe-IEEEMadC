package com.bidme.fragment.dashboad;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.adapter.SubscriptionPackageAdapter;
import com.bidme.databinding.ActivitySubscriptionPackageBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.CurrentSubDTO;
import com.bidme.model.SubscriptionPackageDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Subscription extends Fragment implements View.OnClickListener {

    private Dashboard dashboard;
    private String TAG = Subscription.class.getCanonicalName();
    private ActivitySubscriptionPackageBinding binding;
    private SubscriptionPackageAdapter subscriptionPackageAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SubscriptionPackageDTO> subscriptionpackageArrayList;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CurrentSubDTO currentSubDTO;
    private int key;
    private Button btnupgrade;
    String thisDate;


    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        if (mcontext instanceof Activity) {
            this.dashboard = (Dashboard) mcontext;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_subscription_package, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Const.USER_DTO);


        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);

        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        getCurrentSubHistory();
        getAllsubpackage();


        return binding.getRoot();
    }


    private void getAllsubpackage() {

        new HttpsRequest(Const.GET_All_SUBSCRIPTION_PACKAGE, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {

                        subscriptionpackageArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<SubscriptionPackageDTO>>() {
                        }.getType();

                        subscriptionpackageArrayList = (ArrayList<SubscriptionPackageDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvplans.setLayoutManager(layoutManager);
                        binding.rvplans.setHasFixedSize(true);
                        subscriptionPackageAdapter = new SubscriptionPackageAdapter(getActivity(), subscriptionpackageArrayList, Subscription.this, key);
                        binding.rvplans.setAdapter(subscriptionPackageAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    binding.noPackage.setVisibility(View.VISIBLE);
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public void getCurrentSubHistory() {

        new HttpsRequest(Const.CURRENT_SUB_HISTORY, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    key = 123;

                    try {

                        binding.linear.setVisibility(View.VISIBLE);
                        currentSubDTO = new Gson().fromJson(response.getJSONObject("data").toString(), CurrentSubDTO.class);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    key = 456;
                    binding.linear.setVisibility(View.GONE);


                    // ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });


    }


    private void showData() {

        binding.pkgName.setText(currentSubDTO.getPackage_name());
        binding.aunctionCount.setText(currentSubDTO.getAuction_count());
        binding.price.setText(currentSubDTO.getTotal_price()+ " " +currentSubDTO.getCurrency_code());
        binding.date.setText("Validity -" + ProjectUtils.changeDateFormate(currentSubDTO.getStart_date()) + " " + " to " + " " + ProjectUtils.changeDateFormate(currentSubDTO.getEnd_date()));

        if (currentSubDTO.getEnd_date().matches(thisDate)) {
            binding.status.setBackground(R.drawable.button_red);
        } else {
            binding.status.setBackground(R.drawable.button_normal_green);
        }
        getAllsubpackage();

    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onDetach() {
        super.onDetach();
        this.dashboard = null;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentSubHistory();
        getAllsubpackage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }

    }
}
