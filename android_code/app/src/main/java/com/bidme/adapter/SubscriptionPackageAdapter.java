package com.bidme.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bidme.R;

import com.bidme.activity.authentication.Sign_in;
import com.bidme.databinding.AdapterSubscriptionStandardBinding;
import com.bidme.fragment.dashboad.Subscription;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.SubscriptionPackageDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SubscriptionPackageAdapter extends RecyclerView.Adapter<SubscriptionPackageAdapter.MyViewHolder> {
    private static final String TAG = SubscriptionPackageAdapter.class.getCanonicalName();
    private HashMap<String, String> params = new HashMap<>();
    private Context mContext;
    private AdapterSubscriptionStandardBinding binding;
    private ArrayList<SubscriptionPackageDTO> subscriptionpackageArrayList;
    private LayoutInflater layoutInflater;
    private SharedPrefrence prefrence;

    private UserDTO userDTO;
    private String userPubId;
    private int pos = -1;
    Subscription subscription;
    private int key;
    int CheckValid=1;
    AlertDialog.Builder builder1 ;


    public SubscriptionPackageAdapter(Context mContext, ArrayList<SubscriptionPackageDTO> subscriptionpackageArrayList, Subscription subscription,int key) {
        this.mContext = mContext;
        this.subscriptionpackageArrayList = subscriptionpackageArrayList;
        this.subscription = subscription;
        this.key=key;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        userPubId=userDTO.getUser_pub_id();
        builder1=new AlertDialog.Builder(mContext);

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_subscription_standard, parent, false);
        return new MyViewHolder(binding);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        pos = position;

        myViewHolder.binding.tvstanderd.setText(subscriptionpackageArrayList.get(position).getPackage_name());
        myViewHolder.binding.tvprice.setText(subscriptionpackageArrayList.get(position).getPrice()+" "+subscriptionpackageArrayList.get(position).getCurrency_code());
        myViewHolder.binding.tvtotaldays.setText(subscriptionpackageArrayList.get(position).getTotal_days()+" "+"Days");
        myViewHolder.binding.tvaunctioncount.setText(subscriptionpackageArrayList.get(position).getAuction_count());
        params.put(Const.PACKAGE_PUB_ID, subscriptionpackageArrayList.get(position).getPackage_pub_id());



        if(key==123){

            myViewHolder.binding.btnupgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "You have already subscribed", Toast.LENGTH_SHORT).show();


                }
            });
        }
        else {
            myViewHolder.binding.btnupgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteDailog();


                }
            });
        }



        AnimationDrawable animationDrawable = (AnimationDrawable) myViewHolder.binding.linerplan.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();


    }


    private void deleteDailog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to subscribe this package ?")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(userPubId.equals(Const.GUEST_USER_PUB_ID)){
                            alertGuestUser();

                        }
                        else{
                            params.put(Const.USER_PUB_ID, userPubId);
                            CheckValid=2;
                            addSubscription();

                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Subasta");
        alert.show();
    }

    private void addSubscription() {
        new HttpsRequest(Const.ADD_SUBSCRIBE, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    subscription.getCurrentSubHistory();


                } else {
                    if(CheckValid==1){
                      //  Toast.makeText(mContext, mContext.getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_SHORT).show();

                    }else{
                        ProjectUtils.showToast(mContext, msg);

                    }
                }
            }
        });


    }


    public int getItemCount() {
        return subscriptionpackageArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterSubscriptionStandardBinding binding;


        public MyViewHolder(AdapterSubscriptionStandardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    private void alertGuestUser() {


        builder1.setMessage(mContext.getResources().getString(R.string.guestMsg))
                .setCancelable(false)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent= new Intent(mContext,Sign_in.class);
                        mContext.startActivity(intent);

                        CheckValid=1;

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










