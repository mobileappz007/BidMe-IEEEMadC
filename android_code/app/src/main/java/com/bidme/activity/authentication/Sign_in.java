package com.bidme.activity.authentication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.activity.social.FacebookHelper;
import com.bidme.activity.social.GoogleSignInHelper;
import com.bidme.databinding.ActivitySignInBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.UserDTO;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.KeyHashGenerator;
import com.bidme.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Sign_in extends AppCompatActivity implements View.OnClickListener, FacebookHelper.OnFbSignInListener, GoogleSignInHelper.OnGoogleSignInListener {
    private Context sContext;
    private String TAG = Sign_in.class.getCanonicalName();
    private ActivitySignInBinding binding;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private Dialog dialogbox_forgetpassword;
    private HashMap<String, String> paramsForgot = new HashMap<>();
    ImageView ImgClose;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private GoogleSignInHelper googleSignInHelper;
    private FacebookHelper fbConnectHelper;
    private boolean isFbLogin = false;
    private String userName;
    private String email;
    private boolean isHide = false;
    private int count = 1;
    private Long timer;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        sContext = Sign_in.this;
        prefrence = SharedPrefrence.getInstance(sContext);

        KeyHashGenerator.generateKey(this);
        fbConnectHelper = new FacebookHelper(this, this);

        googleSignInHelper = new GoogleSignInHelper(this, this);
        googleSignInHelper.connect();
        timer = Long.valueOf(30000);


        setUiAction();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Log.w(TAG, "getInstanceId :====>" + sharedPreferences.getString(Const.DEVICE_TOKEN, ""));
        //firebase();
    }

    private void firebase() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);

                    }
                });

    }

    private void setUiAction() {
        binding.btnSignIn.setOnClickListener(this);
        binding.btnFacebook.setOnClickListener(this);
        binding.btnGmail.setOnClickListener(this);
        binding.tvCreateAccount.setOnClickListener(this);
        binding.tvFrogotPass.setOnClickListener(this);
        binding.btnFacebook.setOnClickListener(this);
        binding.btnGmail.setOnClickListener(this);
        binding.ivPass.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                if (count == 5) {
                    timer();
                    binding.btnSignIn.setVisibility(View.GONE);
                    binding.relacoundown.setVisibility(View.VISIBLE);
                    binding.textPls.setVisibility(View.VISIBLE);
                    binding.counter.setVisibility(View.VISIBLE);


                } else {
                    binding.btnSignIn.setVisibility(View.VISIBLE);
                    binding.relacoundown.setVisibility(View.GONE);
                    binding.textPls.setVisibility(View.GONE);
                    binding.counter.setVisibility(View.GONE);


                    ClickForSubmit();

                }
                break;

            case R.id.tvFrogotPass:
                dialogForgotPassword();
                break;
            case R.id.tvCreateAccount:
                Intent in = new Intent(sContext, Sign_Up.class);
                startActivity(in);
                break;
            case R.id.btnFacebook:
                fbLogin();
                break;

            case R.id.btnGmail:
                gmailLogin();
                break;
            case R.id.ivPass:
                if (isHide) {
                    binding.ivPass.setImageResource(R.drawable.eye);
                    binding.etPassword.setTransformationMethod(null);
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isHide = false;
                } else {
                    binding.ivPass.setImageResource(R.drawable.passwordhide);
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isHide = true;
                }
                break;


        }
    }






    public void timer() {
        binding.btnSignIn.setVisibility(View.GONE);
        binding.relacoundown.setVisibility(View.VISIBLE);
        binding.counter.setVisibility(View.VISIBLE);
        binding.textPls.setVisibility(View.VISIBLE);


        countDownTimer = new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                long milliseconds = millisUntilFinished - 1000;
                long minutes = ((milliseconds / 1000) / 60);
                int seconds = (int) (milliseconds / 1000) % 60;


                binding.counter.setText("" + seconds);


            }

            public void onFinish() {
                binding.btnSignIn.setVisibility(View.VISIBLE);
                binding.relacoundown.setVisibility(View.GONE);
                binding.counter.setVisibility(View.GONE);
                binding.textPls.setVisibility(View.GONE);


                binding.counter.setText("finished!");
                count = 0;
            }
        }.start();
    }

    private void gmailLogin() {
        ProjectUtils.showProgressDialog(sContext, false, "Please wait...");
        googleSignInHelper.signIn();
        isFbLogin = false;
    }

    private void fbLogin() {
        ProjectUtils.showProgressDialog(sContext, false, "Please wait...");
        fbConnectHelper.connect();
        isFbLogin = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleSignInHelper.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleSignInHelper.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);


    }


    private void signUpRequest() {
        ProjectUtils.showProgressDialog(sContext, false, "Please wait...");
        JSONObject object = new JSONObject(getParams());
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
                    count++;
                    ProjectUtils.showToast(sContext, msg);
                }
            }
        });
    }

    public void dialogForgotPassword() {
        dialogbox_forgetpassword = new Dialog(sContext/*, android.R.style.Theme_Dialog*/);
        dialogbox_forgetpassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_forgetpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_forgetpassword.setContentView(R.layout.activity_dialogbox_forgetpassword);
        final EditText etEmail = (EditText) dialogbox_forgetpassword.findViewById(R.id.etEmail);
        Button btnSubmit = (Button) dialogbox_forgetpassword.findViewById(R.id.btnSubmit);
        ImageView ivClose = dialogbox_forgetpassword.findViewById(R.id.ivClose);
        dialogbox_forgetpassword.show();
        dialogbox_forgetpassword.setCancelable(false);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsForgot.put(Const.EMAIL, ProjectUtils.getEditTextValue(etEmail));
                if (!ProjectUtils.isEmailValid(etEmail.getText().toString().trim())) {
                    showSickbar(getResources().getString(R.string.verifyemail));

                } else {
                    if (NetworkManager.isConnectToInternet(sContext)) {
                        forgotPassword();
                    } else {
                        ProjectUtils.InternetAlertDialog(sContext);
                    }
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_forgetpassword.dismiss();
            }
        });
    }

    private void forgotPassword() {

        ProjectUtils.showProgressDialog(sContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Const.FORGOT_PASSWORD, paramsForgot, sContext).stringPost(TAG, new Helper() {

            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(sContext, msg);
                    dialogbox_forgetpassword.dismiss();
                } else {
                    ProjectUtils.showToast(sContext, msg);
                }
            }
        });

    }


    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.NAME, ProjectUtils.getEditTextValue(binding.etEmail));
        params.put(Const.PASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        params.put(Const.DEVICE_TOKEN, sharedPreferences.getString(Const.DEVICE_TOKEN, ""));

        return params;
    }

    public void ClickForSubmit() {
        if (binding.etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.valid_user_namee));
        } else if (binding.etPassword.getText().toString().trim().equalsIgnoreCase("")) {

            showSickbar(getResources().getString(R.string.verifypass));
        } else {

            if (NetworkManager.isConnectToInternet(sContext)) {
                signUpRequest();

            } else {
                ProjectUtils.InternetAlertDialog(sContext);
            }
        }
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.rlSnackbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();


    }

    @Override
    public void OnFbSignInComplete(GraphResponse graphResponse, String error) {

        if (error == null) {
            try {
                JSONObject jsonObject = graphResponse.getJSONObject();
                userName = jsonObject.getString("name");
                email = jsonObject.getString("email");
                String id = jsonObject.getString("id");
                String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
            }
        }

    }

    @Override
    public void OnGSignInSuccess(GoogleSignInAccount googleSignInAccount) {
        if (googleSignInAccount != null) {
            userName = (googleSignInAccount.getGivenName() + googleSignInAccount.getFamilyName());
            email = (googleSignInAccount.getEmail());
        }

    }

    @Override
    public void OnGSignInError(String error) {
        Log.e(TAG, error);

    }
}

