package com.bidme.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.authentication.Sign_in;
import com.bidme.activity.categories.CategoryList;
import com.bidme.adapter.SupportAdapter;
import com.bidme.databinding.ActivitySupportAcitivityBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.SupportDTO;
import com.bidme.model.UserDTO;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.CustomEditText;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SupportAcitivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ActivitySupportAcitivityBinding binding;
    private Dialog dialogbox_add_bid;
    private ArrayList<SupportDTO> supportDTOArrayList;
    private RecyclerView.LayoutManager layoutManager;
    private SupportAdapter supportAdapter;
    private String TAG = CategoryList.class.getCanonicalName();
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> paramsAddSupport = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String editMessage;
    private String editDesc;
     CustomEditText editTitle;
    CustomEditText editDescription;
    private String user_pub_id;
    private int checkValid =1;
    AlertDialog.Builder builder1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_acitivity);
        mContext = SupportAcitivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        user_pub_id=userDTO.getUser_pub_id();
        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        builder1=new AlertDialog.Builder(this);





        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
            }
        });
        binding.myFAB.setOnClickListener(this);


        binding.chatSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                try {
                    supportAdapter.filter(s);
                }catch (Exception e){

                }
                return false;
            }
        });

        setData();


    }

    private void setData() {


        new HttpsRequest(Const.GET_ALL_SUPPORT, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                binding.swipeRefreshLayout.setRefreshing(false);

                if (flag) {
                    try {
                        supportDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<SupportDTO>>() {
                        }.getType();
                        binding.tvNo.setVisibility(View.GONE);
                        supportDTOArrayList = (ArrayList<SupportDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        binding.recycleview1.setLayoutManager(layoutManager);
                        supportAdapter = new SupportAdapter(mContext, supportDTOArrayList);
                        binding.recycleview1.setAdapter(supportAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);
                      binding.chatSearch.setVisibility(View.GONE);
                     //ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myFAB:

                if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
                    alertGuestUser();

                }else{
                    dialogbox_support_msg();

                }
                break;
            case R.id.icBack:
                onBackPressed();

                break;

        }

    }

    private void dialogbox_support_msg() {

        dialogbox_add_bid = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialogbox_add_bid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_add_bid.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_add_bid.setContentView(R.layout.dialog_support_msg);
         editTitle = dialogbox_add_bid.findViewById(R.id.editTitle);
         editDescription = dialogbox_add_bid.findViewById(R.id.editDescription);
        ImageView ivClose = dialogbox_add_bid.findViewById(R.id.ivClose);
        Button btnSubmit = dialogbox_add_bid.findViewById(R.id.btnSubmit);
        dialogbox_add_bid.show();
        dialogbox_add_bid.setCancelable(true);
       /* editMessage = editTitle.getText().toString().trim();
        editDesc = editDescription.getText().toString().trim();*/


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.put(Const.GET_SUP_TITLE,editTitle.getText().toString());
                params.put(Const.GET_SUP_DESC, editDescription.getText().toString() );
                params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());


                    paramsAddSupport.put(Const.GET_SUP_TITLE,editTitle.getText().toString());
                    paramsAddSupport.put(Const.GET_SUP_DESC, editDescription.getText().toString() );
                    paramsAddSupport.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
                    checkValid=2;

                    checkEditField();







            }
        });


        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogbox_add_bid.dismiss();
            }
        });


    }

    private void checkEditField() {

        if ( editTitle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Please enter your message title.", Toast.LENGTH_SHORT).show();
        } else if (editDescription.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Please enter the description", Toast.LENGTH_SHORT).show();
        }else {

            if (NetworkManager.isConnectToInternet(mContext)) {

               addSupportMsg();
            } else {
                if(checkValid==1){
                    //Toast.makeText(mContext, getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_SHORT).show();

                }else{
                    ProjectUtils.InternetAlertDialog(mContext);

                }
            }
        }



    }

    private void addSupportMsg() {


        new HttpsRequest(Const.ADD_SUPPRT, paramsAddSupport, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    setData();
                    dialogbox_add_bid.dismiss();
                    //Toast.makeText(mContext, "Your message submitted.", Toast.LENGTH_SHORT).show();

                   ProjectUtils.showToast(mContext, msg);

                } else {
                  // ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }


    private void alertGuestUser() {


        builder1.setMessage(getResources().getString(R.string.guestMsg))
                .setCancelable(false)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent= new Intent(mContext,Sign_in.class);
                        startActivity(intent);
                        finishAffinity();

                        checkValid=1;

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        //Toast.makeText(getApplicationContext(), "you clicked NO",
                        // Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("GetRid");
        alert.setIcon(R.drawable.ic_logout1);
        alert.show();
    }
}
