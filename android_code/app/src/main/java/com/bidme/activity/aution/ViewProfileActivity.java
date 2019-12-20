package com.bidme.activity.aution;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.bidme.R;
import com.bidme.databinding.ActivityViewProfileBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ViewProfileActivity.class.getCanonicalName();
    private ActivityViewProfileBinding binding;
    private Context mContext;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String userId;
    private String rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ViewProfileActivity.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile);
        userId = getIntent().getStringExtra(Const.USER_PUB_ID);

        rating=getIntent().getStringExtra(Const.GET_ALL_RATING);


        prefrence = SharedPrefrence.getInstance(mContext);

        setUiAction();
        //userDTO = prefrence.getParentUser(Const.USER_DTO);
        params.put(Const.USER_PUB_ID, userId);


        getViewProfileData();
    }

    private void setUiAction() {
        binding.profileback.setOnClickListener(this);
        binding.cetPhoneno.setOnClickListener(this );
    }

    private void getViewProfileData() {


        new HttpsRequest(Const.VIEW_PROFILE, params, mContext).stringPost(TAG, new Helper() {

            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void showData() {
        binding.cetAddress.setText(userDTO.getAddress());
        binding.ctvName.setText(ProjectUtils.capWordFirstLetter(userDTO.getName()));
        binding.etEmail.setText(userDTO.getEmail());
        binding.cetPhoneno.setText(userDTO.getMobile_no());

        Glide
                .with(mContext)
                .load(userDTO.getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(binding.civProfile);

        try {
            binding.simpleRatingBar1.setRating(Float.parseFloat(rating));

        }
        catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileback:
                onBackPressed();
                break;

            case R.id.cetPhoneno:
                callDialer();
                break;

        }

    }

    private void callDialer() {


        try
        {
            Uri u = Uri.parse("tel:" + binding.cetPhoneno.getText().toString());


            Intent i = new Intent(Intent.ACTION_DIAL, u);

            startActivity(i);
        }
        catch (SecurityException s)
        {


        }
    }
}
