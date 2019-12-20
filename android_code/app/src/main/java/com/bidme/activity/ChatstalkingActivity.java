package com.bidme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.MessageAdapter;
import com.bidme.databinding.ActivityChatstalkingBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.ChatMessageDTO;
import com.bidme.model.ThreadIDDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatstalkingActivity extends AppCompatActivity {
    private String TAG = ChatstalkingActivity.class.getCanonicalName();


    private List<ChatMessageDTO> chatMessages = new ArrayList<>();
    private ArrayAdapter<ChatMessageDTO> adapter;

    private ActivityChatstalkingBinding binding;
    private Context mContext;
    private String username;
    private String receiver_image = "";
    private String msg;
    SharedPrefrence sharedPrefrence;
    private ThreadIDDTO threadIDDTO;
    private UserDTO userDTO;
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> sendParams = new HashMap<>();
    private HashMap<String, String> getThread = new HashMap<>();
    private RecyclerView.LayoutManager layoutManager;
    private MessageAdapter messageAdapter;
    private String threadid = "";
    IntentFilter intentFilter = new IntentFilter();
    private final int interval = 1000; // 1 Second
    private Handler handler = new Handler();
    String Pro_pub_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ChatstalkingActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatstalking);
        sharedPrefrence = SharedPrefrence.getInstance(ChatstalkingActivity.this);
        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        username = getIntent().getStringExtra(Const.RECEIVER_NAME);
        receiver_image = getIntent().getStringExtra(Const.RECEIVER_IMAGE);

        getThread.put(Const.SENDER_ID, userDTO.getUser_pub_id());
        getThread.put(Const.RECEIVER_ID, getIntent().getStringExtra(Const.RECEIVER_ID));

        sendParams.put(Const.SENDER_ID, userDTO.getUser_pub_id());
        sendParams.put(Const.RECEIVER_ID, getIntent().getStringExtra(Const.RECEIVER_ID));

        intentFilter.addAction(Const.BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);


        if(getIntent().hasExtra(Const.Pro_pub_id)){
            Pro_pub_id=getIntent().getStringExtra(Const.Pro_pub_id);
        }


        findUI();
        timer();
        //setTimer();

       // getSingleChatHistory();

    }


    public void timer(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSingleChatHistory();
               // Toast.makeText(mContext,"ashu",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }





    private void setTimer() {
        Timer _Request_Trip_Timer = new Timer();
        _Request_Trip_Timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //Toast.makeText(mContext, "C'Mom no hands!", Toast.LENGTH_SHORT).show();
                chatMessages.clear();
             getSingleChatHistory();



            }
        }, 5, 5000);


    }

    private void getThreadId() {

        getThread.put(Const.Pro_pub_id,Pro_pub_id);

        new HttpsRequest(Const.THREAD_ID, getThread, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        threadIDDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ThreadIDDTO.class);
                        getSingleChatHistory();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }


    private void findUI() {

        // sendParams.put(Const.SENDER_NAME, userDTO.getName());



        binding.tvname.setText(username);


        Glide
                .with(mContext)
                .load(receiver_image)
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(binding.ivalia);


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSingleChatHistory();

            }
        });


        binding.tvname.setText(username);


        binding.btnChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.msgType.getText().length() > 0) {
                    sendMessage();
                } else {
                    Toast.makeText(mContext, "Please type your message", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getThreadId();


    }

    private void getSingleChatHistory() {
        params.put(Const.SENDER_ID, userDTO.getUser_pub_id());
        params.put(Const.RECEIVER_ID, getIntent().getStringExtra(Const.RECEIVER_ID));
        params.put(Const.THREAD, threadIDDTO.getThread_id());
        ProjectUtils.showLog(TAG, " param --->" + params.toString());

        new HttpsRequest(Const.GET_SINGLE_CHAT_HISTORY, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                binding.swipeRefreshLayout.setRefreshing(false);

                if (flag) {
                    try {
                        chatMessages = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<ChatMessageDTO>>() {
                        }.getType();

                        chatMessages = (ArrayList<ChatMessageDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);

                        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        binding.recycleChat.setLayoutManager(layoutManager);
                        messageAdapter = new MessageAdapter(mContext, (ArrayList<ChatMessageDTO>) chatMessages, userDTO, receiver_image);
                        binding.recycleChat.setAdapter(messageAdapter);
                        binding.recycleChat.scrollToPosition(chatMessages.size() - 1);
                        binding.recycleChat.smoothScrollToPosition(chatMessages.size() - 1);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                   // ProjectUtils.showToast(mContext, msg);
                }
            }
        });

    }

    private void sendMessage() {
        sendParams.put(Const.MESSAGE, binding.msgType.getText().toString());
        sendParams.put(Const.THREAD, threadIDDTO.getThread_id());
        binding.swipeRefreshLayout.setRefreshing(false);

        new HttpsRequest(Const.SEND_MESSAGE, sendParams, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    getSingleChatHistory();
                    //ProjectUtils.showToast(mContext, msg);
                    binding.msgType.setText("");

                } else {
                   // ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSingleChatHistory();
        }
    };


}



