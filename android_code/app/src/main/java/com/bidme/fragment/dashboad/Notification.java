package com.bidme.fragment.dashboad;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.adapter.NotificationAdapter;
import com.bidme.databinding.FragmentNotificationBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.NotificationDTO;
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

public class Notification extends Fragment implements View.OnClickListener {
    private FragmentNotificationBinding binding;
    private String TAG = Notification.class.getCanonicalName();
    private Dashboard dashboard;
    private Context mContext;
    private ArrayList<NotificationDTO> notificationDTOArrayList;
    private NotificationAdapter notificationAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;

    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        if (mcontext instanceof Activity) {
            this.dashboard = (Dashboard) mcontext;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        params.put(Const.USER_PUB_ID,userDTO.getUser_pub_id());
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotification();
            }
        });
        setUiAction();
        return binding.getRoot();
    }

    private void setUiAction() {

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getNotification();
        } else {
            ProjectUtils.InternetAlertDialog(getActivity());
        }
    }

    private void getNotification() {
        new HttpsRequest(Const.GET_NOTIFICATION, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    //ProjectUtils.showToast(getActivity(), msg);
                    binding.swipeRefreshLayout.setRefreshing(false);
                    try {
                        binding.recycleview1.setVisibility(View.VISIBLE);
                        binding.relative.setVisibility(View.GONE);
                        notificationDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<NotificationDTO>>() {
                        }.getType();
                        notificationDTOArrayList = (ArrayList<NotificationDTO>) new Gson().fromJson(response.getJSONArray("my_notifications").toString(), getpetDTO);

                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        binding.recycleview1.setLayoutManager(linearLayoutManager);

                        notificationAdapter = new NotificationAdapter(getActivity(), notificationDTOArrayList);
                        binding.recycleview1.setAdapter(notificationAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    binding.swipeRefreshLayout.setRefreshing(false);

                    binding.relative.setVisibility(View.VISIBLE);
                    binding.recycleview1.setVisibility(View.GONE);

                  //  ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}