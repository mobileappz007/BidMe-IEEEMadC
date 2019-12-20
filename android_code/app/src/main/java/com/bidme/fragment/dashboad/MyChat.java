package com.bidme.fragment.dashboad;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;

import com.bidme.adapter.MyChatsAdapter;
import com.bidme.databinding.FragmentMyChatBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyChatsDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bidme.utils.ProjectUtils.TAG;

public class MyChat extends Fragment implements View.OnClickListener {
    private View view;
    private Context mContext;
    private FragmentMyChatBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MyChatsDTO> myChatsDTOArrayList;
    private MyChatsAdapter myChatsAdapter;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence preferences;
    private UserDTO userDTO;
    private ArrayList<MyChatsDTO>object;
    IntentFilter intentFilter = new IntentFilter();



    private Dashboard dashboard;
    TextView textView;


    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        if (mcontext instanceof Activity) {
            this.dashboard = (Dashboard) mcontext;

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_chat, container, false);
        view = binding.getRoot();
        preferences = SharedPrefrence.getInstance(getActivity());
        userDTO = preferences.getParentUser(Const.USER_DTO);
        params.put(Const.SENDER_ID, userDTO.getUser_pub_id());

        intentFilter.addAction(Const.ALL_CHAT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);


     //   getAllchat();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllchat();
            }
        });


         binding.chatSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String s) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String s) {

                 try {
                     myChatsAdapter.filter(s);
                 }catch (Exception e){

                 }
                 return false;
             }
         });





        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycle1.setAdapter(myChatsAdapter);
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
        getAllchat();
    }

    private void getAllchat() {

        new HttpsRequest(Const.GET_CHAT_HISTORY, params, getActivity()).stringPost(TAG, new Helper() {

            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                binding.swipeRefreshLayout.setRefreshing(false);

                if (flag) {
                    try {

                        object = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<MyChatsDTO>>() {
                        }.getType();

                        object = (ArrayList<MyChatsDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        binding.recycle1.setLayoutManager(layoutManager);
                        binding.recycle1.setHasFixedSize(true);
                        myChatsAdapter = new MyChatsAdapter(getActivity(), object, userDTO);
                        binding.recycle1.setAdapter(myChatsAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    binding.swipeRefreshLayout.setRefreshing(false);

                    binding.chatSearch.setVisibility(View.GONE);
                    binding.tvNo.setVisibility(View.VISIBLE);

                  //  ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }

    }
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            getAllchat();
        }
    };




}
