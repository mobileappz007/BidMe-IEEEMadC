package com.bidme.activity.aution;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.databinding.ActivityViewProfileBinding;
import com.bidme.databinding.ActivityWinnerViewProfileBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.UserDTO;
import com.bidme.model.WinnersDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import java.util.HashMap;

public class ViewWinnerProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ViewWinnerProfileActivity.class.getCanonicalName();
    private ActivityWinnerViewProfileBinding binding;
    private Context mContext;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String userId;
    private String rating;

    WinnersDTO winnersDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ViewWinnerProfileActivity.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_winner_view_profile);
        userId = getIntent().getStringExtra(Const.USER_PUB_ID);

        if(getIntent().hasExtra(Const.WinnerDTO)){

            winnersDTO= (WinnersDTO) getIntent().getSerializableExtra(Const.WinnerDTO);

            showData();

        }



        prefrence = SharedPrefrence.getInstance(mContext);


        setUiAction();


    }

    private void setUiAction() {
        binding.profileback.setOnClickListener(this);
        binding.cetPhoneno.setOnClickListener(this);
    }


    private void showData() {
        try {
            binding.cetAddress.setText(winnersDTO.getAddress());
            binding.ctvName.setText(ProjectUtils.capWordFirstLetter(winnersDTO.getName()));
            binding.etEmail.setText(winnersDTO.getEmail());
            binding.cetPhoneno.setText(winnersDTO.getMobile_no());

            Glide
                    .with(mContext)
                    .load(winnersDTO.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(binding.civProfile);

        }catch (Exception e){

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


        try {
            Uri u = Uri.parse("tel:" + binding.cetPhoneno.getText().toString());


            Intent i = new Intent(Intent.ACTION_DIAL, u);

            startActivity(i);
        } catch (SecurityException s) {


        }
    }
}
