package com.bidme.activity.authentication;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bidme.R;
import com.bidme.databinding.ActivityDialogboxForgetpasswordBinding;

public class dialogbox_forgetpassword extends AppCompatActivity {
    private Context sContext;
    private String TAG = dialogbox_forgetpassword.class.getCanonicalName();
    private ActivityDialogboxForgetpasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dialogbox_forgetpassword);
        sContext = dialogbox_forgetpassword.this;
        setUiAction();
    }

    private void setUiAction() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(sContext, "Check Email", Toast.LENGTH_SHORT).show();

            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
