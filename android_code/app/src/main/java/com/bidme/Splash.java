package com.bidme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

import static com.bidme.activity.authentication.Sign_in.MyPREFERENCES;

public class Splash extends AppCompatActivity {
    private static final String TAG = Splash.class.getCanonicalName();

    private Context sContext;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 5000;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        sContext = Splash.this;
        prefrence = SharedPrefrence.getInstance(sContext);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Log.w(TAG, "getInstanceId :====>" + sharedPreferences.getString(Const.DEVICE_TOKEN, ""));
    }



    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, SPLASH_TIME_OUT);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (prefrence.getBooleanValue(Const.IS_REGISTERED)) {
                Intent in = new Intent(sContext, Dashboard.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.putExtra("index",0);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
            } else {
               signInWithGuestUser();
            /*    Intent in = new Intent(sContext, Sign_in.class);
                startActivity(in);
                finish();*/
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
            }

        }

    };


    private void signInWithGuestUser() {

        ProjectUtils.showProgressDialog(sContext, false, "Please wait...");
        JSONObject object = new JSONObject(getGuestParams());
        new HttpsRequest(Const.SIGN_IN, object, sContext).stringPostJson(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    // ProjectUtils.showToast(sContext, msg);
                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Const.USER_DTO);
                        prefrence.setBooleanValue(Const.IS_REGISTERED, true);
                        Intent in = new Intent(sContext, Dashboard.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.putExtra("index", 0);
                        startActivity(in);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // ProjectUtils.showToast(sContext, msg);
                }
            }
        });


    }
    private HashMap<String, String> getGuestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.NAME,"Guest");
        params.put(Const.PASSWORD, "123456");
        params.put(Const.DEVICE_TOKEN, sharedPreferences.getString(Const.DEVICE_TOKEN, ""));


        return params;
    }

}
