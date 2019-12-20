package com.bidme.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.activity.authentication.Sign_in;
import com.bidme.activity.aution.ViewAuction;
import com.bidme.databinding.AdapterItemAutionAllBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.AutionAllDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.CustomTextViewBold;
import com.bidme.utils.ProjectUtils;


import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

public class  AutionAllAdapter extends RecyclerView.Adapter<AutionAllAdapter.MyViewHolder> {
    private static final String TAG = AutionAllAdapter.class.getCanonicalName();

    private LayoutInflater layoutInflater;
    private AdapterItemAutionAllBinding binding;
    private Context mContext;
    private Dialog dialogbox_add_bid;
    private ArrayList<AutionAllDTO> object;
    private ArrayList<AutionAllDTO>autionAllDTOArrayList;
    EditText etBidPrice;
    TextView bidprice;
    Button btnAddBid;
    String proPrice;
    ImageView ivClose;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private final int limit = 4;
    String END_date;
    CountDownTimer countDownTimer;
    String dateStr = "";
    String conDate;
    LocalDateTime now;
    Date date1 = new Date();

    String remainTime = "";
    String newTIme = "691200";
    private String user_pub_id;
    int checkValid=1;
    AlertDialog.Builder builder1 ;
    String time="86400";







    int pos = -1;
    private HashMap<String, String> paramsAddBid = new HashMap<>();


    public AutionAllAdapter(Context mContext, ArrayList<AutionAllDTO> object) {

        this.mContext = mContext;
        this.object = object;
        this.autionAllDTOArrayList=new ArrayList<>();
        this.autionAllDTOArrayList.addAll(object);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        user_pub_id=userDTO.getUser_pub_id();

        builder1=new AlertDialog.Builder(mContext);


        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterItemAutionAllBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_item_aution_all, parent, false);

/*
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        now = LocalDateTime.now();
        time = dtf.format(now);
*/

        return new MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        pos = position;
        getMilliFromDate(dateStr);
        END_date = object.get(position).getE_date();
       remainTime = autionAllDTOArrayList.get(position).getRemaining_time();

        if(remainTime.equals("")){

        }else{
            if (Long.valueOf(remainTime) > Long.valueOf(time)) {

                myViewHolder.binding.relayconuntdown.setVisibility(View.VISIBLE);
                myViewHolder.binding.counter.setVisibility(View.VISIBLE);
                myViewHolder.binding.counter1.setVisibility(View.GONE);

                myViewHolder.binding.counter.start(Long.valueOf(remainTime) * 1000);
                myViewHolder.binding.counter.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        myViewHolder.binding.counter.stop();
                        myViewHolder.binding.relayconuntdown.setVisibility(View.GONE);
                        myViewHolder.binding.counter.setVisibility(View.GONE);
                    }
                });

            } else {
                myViewHolder.binding.relayconuntdown.setVisibility(View.VISIBLE);
                myViewHolder.binding.counter1.setVisibility(View.VISIBLE);
                myViewHolder.binding.counter.setVisibility(View.GONE);

                myViewHolder.binding.counter1.start(Long.valueOf(remainTime) * 1000);
                myViewHolder.binding.counter1.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        myViewHolder.binding.counter1.stop();
                        myViewHolder.binding.relayconuntdown.setVisibility(View.GONE);
                        myViewHolder.binding.counter1.setVisibility(View.GONE);
                        myViewHolder.binding.counter.setVisibility(View.GONE);
                    }
                });

            }
        }






        myViewHolder.binding.tvProductname.setText(ProjectUtils.capWordFirstLetter(object.get(position).getTitle()));
        if(object.get(position).getSort_description().equals("")){
            myViewHolder.binding.tvShortDescription.setVisibility(View.GONE);
        }else {
            myViewHolder.binding.tvShortDescription.setText(object.get(position).getSort_description());

        }
        myViewHolder.binding.tvAddress.setText(object.get(position).getAddress());
        myViewHolder.binding.tvPrice.setText(object.get(position).getPrice() + " " +object.get(position).getCurrency_code());
try {

    Glide
            .with(mContext)
            .load(object.get(position).getImage_cust().get(0).getImage())
            .centerCrop()
            .placeholder(R.drawable.noimage)
            .into(myViewHolder.binding.image);

}catch (Exception e){

}
        myViewHolder.binding.ivbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
                    alertGuestUser();
                }else {
                    dialogbox_add_bid(position);
                }

            }
        });



        myViewHolder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(mContext, ViewAuction.class);
                in.putExtra(Const.Pro_pub_id, object.get(position).getPro_pub_id());
                in.putExtra(Const.CAT_TITLE, object.get(position).getTitle());
                in.putExtra(Const.RemaingTime, object.get(position).getRemaining_time());
                in.putExtra(Const.FLAG,"1");



                mContext.startActivity(in);



            }
        });


    }

    private void dialogbox_add_bid(final int pos) {

        dialogbox_add_bid = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialogbox_add_bid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_add_bid.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_add_bid.setContentView(R.layout.activity_add_bid);
        etBidPrice = dialogbox_add_bid.findViewById(R.id.etBidPrice);
        btnAddBid = dialogbox_add_bid.findViewById(R.id.btnAddBid);

        final CustomTextViewBold tvbidproductname = dialogbox_add_bid.findViewById(R.id.tvBidProductname);
        final CustomTextViewBold tvbidprice = dialogbox_add_bid.findViewById(R.id.tvBidProductprice);
        ivClose = dialogbox_add_bid.findViewById(R.id.ivClose);
        dialogbox_add_bid.show();
        dialogbox_add_bid.setCancelable(true);
        tvbidprice.setText(object.get(pos).getPrice()+" "+object.get(pos).getCurrency_code());
        tvbidproductname.setText(object.get(pos).getTitle());


        btnAddBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proPrice = etBidPrice.getText().toString();




                    checkValid=2;
                    paramsAddBid.put(Const.Pro_pub_id, object.get(pos).getPro_pub_id());
                    paramsAddBid.put(Const.USER_PUB_ID,user_pub_id);
                    paramsAddBid.put(Const.PRO_PRICE, proPrice);
                    setData();



                dialogbox_add_bid.dismiss();

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogbox_add_bid.dismiss();
            }
        });
    }

    private void setData() {
        new HttpsRequest(Const.ADD_BID, paramsAddBid, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    object.remove(pos);
                    notifyDataSetChanged();

                    Toast.makeText(mContext, "successs", Toast.LENGTH_SHORT).show();


                } else {
                    if(checkValid==1){
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_SHORT).show();


                    }else{
                        ProjectUtils.showToast(mContext, msg);

                    }
                }
            }
        });

    }

    public long getMilliFromDate(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        conDate = formatter.format(date);

        Log.e(TAG, "condate: -----------------------" + conDate);
        try {
            date = formatter.parse(dateFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " + date);

        Log.e(TAG, "getMilliFromDate: ---------------" + date);

        return date.getTime();
    }


    @Override
    public int getItemCount() {

        return object.size();



    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterItemAutionAllBinding binding;

        public MyViewHolder(AdapterItemAutionAllBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public void filter(String s) {
        s = s.toLowerCase(Locale.getDefault());
        object.clear();
        if (s.length() == 0) {
            object.addAll(autionAllDTOArrayList);

        } else {
            for (AutionAllDTO autionAllDTO : autionAllDTOArrayList) {
                if (autionAllDTO.getTitle().toLowerCase(Locale.getDefault()).contains(s)) {
                    object.add(autionAllDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void alertGuestUser() {


        builder1.setMessage(mContext.getResources().getString(R.string.guestMsg))
                .setCancelable(false)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent= new Intent(mContext,Sign_in.class);
                        mContext.startActivity(intent);

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
