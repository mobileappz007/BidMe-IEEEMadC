package com.bidme.activity.advertise;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bidme.R;
import com.bidme.databinding.ActivityViewAdvertiseBinding;


public class ViewAdvertise extends AppCompatActivity {
    private ActivityViewAdvertiseBinding binding;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_advertise);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = getIntent().getStringExtra("pro_pub_id");


    }


}

