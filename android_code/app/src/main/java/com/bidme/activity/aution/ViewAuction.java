package com.bidme.activity.aution;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.ChatstalkingActivity;
import com.bidme.activity.authentication.Sign_in;
import com.bidme.adapter.AdapterAllRating;
import com.bidme.adapter.ViewAunctionBidAdapter;
import com.bidme.databinding.CommentBinding;
import com.bidme.model.GalleryDTO;
import com.bidme.databinding.ActivityViewAuctionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.GetRatingDTO;
import com.bidme.model.UserDTO;
import com.bidme.model.ViewAllAuctionDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.CustomTextViewBold;
import com.bidme.utils.ProjectUtils;
import com.willy.ratingbar.BaseRatingBar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class ViewAuction extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ViewAuction.class.getCanonicalName();
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private String id;
    private ArrayList<GalleryDTO> galleryDTOArrayList;
    private ViewAunctionBidAdapter viewAunctionBidAdapter;
    private ArrayList<GetRatingDTO> getRatingDTOArrayList;
    private ActivityViewAuctionBinding binding;
    private ViewAllAuctionDTO viewAllAuctionDTO;
    HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private Dialog dialogbox_add_bid, dialogbox_comment;
    private static String price = "";
    ImageView ivClose;
    EditText etBidPrice, etComment;
    TextView bidprice, tvbidproductname, tvbidprice;
    Button btnAddBid;
    String comment;
    String addComment;
    String ratingBar;
    String proPrice;
    Button btnComment;
    private AdapterAllRating adapterAllRating;
    private RecyclerView.LayoutManager layoutManager1;
    private HashMap<String, String> paramRating = new HashMap<>();
    private HashMap<String, String> paramsAddBid = new HashMap<>();
    private HashMap<String, String> paramsfavrouite = new HashMap<>();
    private HashMap<String, String> paramsUnfavrouite = new HashMap<>();
    private HashMap<String, String> paramsAllrating = new HashMap<>();
    private String flag="1";
    IntentFilter intentFilter = new IntentFilter();
    String remainTime = "";
    String newTIme = "691200000";
    String E_Date;
    private String user_pub_id;
    int checkValid=1;
    AlertDialog.Builder builder1 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_auction);
        mContext = ViewAuction.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);
        user_pub_id=userDTO.getUser_pub_id();
        params.put(Const.USER_PUB_ID, user_pub_id);

        builder1=new AlertDialog.Builder(this);
        if(getIntent().hasExtra(Const.RemaingTime)){
            remainTime=getIntent().getStringExtra(Const.RemaingTime);

        }
        Log.e(TAG, "viewauction time : -----------"+remainTime );

        if(getIntent().hasExtra(Const.WON_INDEX)){
            binding.llOffer.setVisibility(View.GONE);
            binding.ivwin.setVisibility(View.VISIBLE);
            binding.tvViewProfile.setVisibility(View.VISIBLE);
        }
        else{
            binding.llOffer.setVisibility(View.VISIBLE);
            binding.ivwin.setVisibility(View.GONE);
            binding.tvViewProfile.setVisibility(View.GONE);



        }




        if (getIntent().hasExtra(Const.Pro_pub_id)) {
            id = getIntent().getStringExtra(Const.Pro_pub_id);
            Log.e(TAG, "onCreate: --------"+id );
            params.put(Const.Pro_pub_id, id);
        }








        paramsAllrating.put(Const.USER_PUB_ID,userDTO.getUser_pub_id());
        paramsAllrating.put(Const.Pro_pub_id,id);


        setUiAction();










  /*      binding.simpleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {


            }
        });*/

       // timer();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUiAction();


    }

    private void counter() {
        Log.e(TAG, "counter: -----------"+remainTime );

        Log.e(TAG, "new time :----------- "+Long.valueOf(newTIme) );
        Log.e(TAG, "new remainTime :----------- "+Long.valueOf(remainTime) );

            if (Long.valueOf(newTIme) > Long.valueOf(remainTime)) {
                Log.e(TAG, "new time :----------- "+Long.valueOf(newTIme) );
                Log.e(TAG, "new remainTime :----------- "+Long.valueOf(remainTime) );

                binding.relayconuntdown.setVisibility(View.VISIBLE);
                binding.counter.setVisibility(View.VISIBLE);
                Log.e(TAG, "time--------------------"+ Long.valueOf(remainTime)*1000);
                binding.counter.start(Long.valueOf(remainTime)*1000);


                binding.counter.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        binding.counter.stop();
                        binding.relayconuntdown.setVisibility(View.GONE);
                        binding.counter.setVisibility(View.GONE);
                    }
                });

            } else {
                binding.relayconuntdown.setVisibility(View.GONE);
                binding.counter.setVisibility(View.GONE);

            }

    }




    public void setUiAction() {
        binding.llViewImage.setOnClickListener(this);
        // binding.llChatscreen.setOnClickListener(this);
        //binding.ivShare.setOnClickListener(this);
        binding.tvViewProfile.setOnClickListener(this);
        binding.tvViewRating.setOnClickListener(this);
        binding.llClick.setOnClickListener(this);
        binding.comment.setOnClickListener(this);
        binding.tvViewAllBid.setOnClickListener(this);
        binding.ivMap.setOnClickListener(this);

        getSingleAuction();
        getAllRating();

    }


    private void getSingleAuction() {

        new HttpsRequest(Const.GET_SINGLE_AUNCTION, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                Log.e("SINGLE AUCTION DATA",response.toString());
                if (flag) {
                    try {
                        viewAllAuctionDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ViewAllAuctionDTO.class);


                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                  //  ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void getAllRating() {
        new HttpsRequest(Const.GET_ALL_RATING, paramsAllrating, mContext).stringPost(TAG,
                new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        ProjectUtils.pauseProgressDialog();

                        if (flag) {
                            try {
                                binding.linRating.setVisibility(View.VISIBLE);
                                getRatingDTOArrayList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<GetRatingDTO>>() {
                                }.getType();
                                getRatingDTOArrayList = (ArrayList<GetRatingDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                                layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                                binding.rvAllRating.setLayoutManager(layoutManager);
                                adapterAllRating = new AdapterAllRating(mContext, getRatingDTOArrayList,"1");
                                binding.rvAllRating.setAdapter(adapterAllRating);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            binding.linRating.setVisibility(View.GONE);
                           // ProjectUtils.showToast(mContext, msg);

                        }
                    }
                });


    }

    public void getSelectingRating() {

        new HttpsRequest(Const.ADD_RATING, paramRating, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    getAllRating();
                    dialogbox_comment.dismiss();
                    ProjectUtils.showToast(mContext, msg);

                } else {
                    if(checkValid==1){
                        Toast.makeText(mContext, getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_LONG).show();


                    }else{
                        ProjectUtils.showToast(mContext, msg);

                     //   Toast.makeText(mContext, "Please give your rating and comment.", Toast.LENGTH_SHORT).show();

                    }
                   // ProjectUtils.showToast(mContext, msg);
                }
            }
        });

    }

    public void showData() {
       E_Date = viewAllAuctionDTO.getE_date();
       // remainTime=viewAllAuctionDTO.getRemaining_time();
        if(remainTime.equalsIgnoreCase("")){

        }else{
            counter();
        }



        Log.e("Sshow","show");
        try {
            Glide
                    .with(mContext)
                    .load(viewAllAuctionDTO.getGallery().get(0).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(binding.ivGallery);
        } catch (Exception e) {

        }

        binding.ivwin.setImageResource(R.drawable.win);

        binding.tvMediaCount.setText(viewAllAuctionDTO.getGallery_count());
        binding.tvZip.setText("Zip code"+" - "+viewAllAuctionDTO.getUsers().getzip());
        binding.tvTown.setText("Town"+" - "+viewAllAuctionDTO.getUsers().getTown());

        try{
            Glide
                    .with(mContext)
                    .load(viewAllAuctionDTO.getUsers().getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(binding.ivProfile);
            binding.tvName.setText(ProjectUtils.capWordFirstLetter(viewAllAuctionDTO.getUsers().getName()));

        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }

        binding.tvDate.setText(ProjectUtils.changeDateFormate(viewAllAuctionDTO.getCreated_at()));
        binding.tvPrice.setText(viewAllAuctionDTO.getPrice()+ " " +viewAllAuctionDTO.getCurrency_code());
        binding.tvShortDescription.setText(viewAllAuctionDTO.getTitle());
        binding.StartDate.setText("Start Date - "+viewAllAuctionDTO.getS_date());
        binding.EndDate.setText("End Date - "+viewAllAuctionDTO.getE_date());
        binding.tvShortDescription.setText(viewAllAuctionDTO.getTitle());
        binding.catname.setText(viewAllAuctionDTO.getCat_title());

        binding.tvAddress.setText(viewAllAuctionDTO.getAddress());
        binding.tvDescription.setText(viewAllAuctionDTO.getDescription());
        binding.tvAddress.setText(viewAllAuctionDTO.getAddress());
        binding.ShortDescription.setText(viewAllAuctionDTO.getSort_description());
        Log.e("LIST SIZE",""+viewAllAuctionDTO.getBids().size());

       if(viewAllAuctionDTO.getBids().size()>0){
           Log.e("LIST","show");
           binding.line1.setVisibility(View.VISIBLE);
           layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
           binding.rvBids.setLayoutManager(layoutManager);
           viewAunctionBidAdapter = new ViewAunctionBidAdapter(viewAllAuctionDTO.getBids(), mContext,"5");
           binding.rvBids.setAdapter(viewAunctionBidAdapter);
       }
       else {
           binding.line1.setVisibility(View.GONE);
       }


      //  binding.simpleRatingBar1.setRating(Float.parseFloat(viewAllAuctionDTO.getAllrating()));


        if (viewAllAuctionDTO.getFavourite().equalsIgnoreCase("0")) {
            binding.ivFavScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfav));
        } else {
            binding.ivFavScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_filledfav));
        }
        viewAunctionBidAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llViewImage:
                if (viewAllAuctionDTO.getGallery().size() > 0) {
                    Intent in = new Intent(mContext, GalleryActivity.class);
                    in.putExtra(Const.DTO, viewAllAuctionDTO.getGallery());
                    in.putExtra(Const.USER_PUB_ID,viewAllAuctionDTO.getUser_pub_id());
                    in.putExtra(Const.FLAG,flag);

                    startActivity(in);
                } else {
                    ProjectUtils.showToast(mContext, "No Gallery Found");
                }

                break;
            case R.id.llOffer:

                if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
                    alertGuestUser();

                }else if(user_pub_id.equalsIgnoreCase(viewAllAuctionDTO.getUser_pub_id())){
                    Toast.makeText(mContext, "You can not add bid you own auction.", Toast.LENGTH_SHORT).show();

                }else{
                    dialogbox_add_bid();

                }


                break;

            case R.id.llChatscreen:

                if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
                    alertGuestUser();

                }
                else{
                    Intent in = new Intent(mContext, ChatstalkingActivity.class);

                    in.putExtra(Const.RECEIVER_ID, viewAllAuctionDTO.getUsers().getUser_pub_id());
                    in.putExtra(Const.Pro_pub_id, viewAllAuctionDTO.getPro_pub_id());
                    in.putExtra(Const.RECEIVER_NAME, viewAllAuctionDTO.getUsers().getName());
                    in.putExtra(Const.RECEIVER_IMAGE, viewAllAuctionDTO.getUsers().getImage());
                    startActivity(in);
                }






                break;
            case R.id.ivBack1:
                onBackPressed();
               /* if(getIntent().getStringExtra(Const.FLAG).equals("1"))
                {
                    Intent intent=new Intent(mContext, AutionAll.class);
                    startActivity(intent);
                    finish();


                }else{
                    onBackPressed();

                }
*/
       /*         if (getIntent().getStringExtra("flow").equals("bro")){
                    *//*startActivity(new Intent(mContext,AutionAll.class).putExtra("OK","OK").putExtra("flow",getIntent().getStringExtra("flow")));
                    finish();*//*
                    df
                }
                else if (getIntent().getStringExtra("flow").equals("bro")){
                    *//*startActivity(new Intent(mContext,AutionAll.class).putExtra("OK","OK").putExtra("flow",getIntent().getStringExtra("flow")));
                    finish();*//*
                }
                else {
                    startActivity(new Intent(mContext,AutionAll.class).putExtra("OK","OK").putExtra("flow",getIntent().getStringExtra("flow")));
                    finish();
                }*/

                break;

            case R.id.ivFavScreen:
                if (viewAllAuctionDTO.getFavourite().equalsIgnoreCase("0")) {
                    setfavrouite();
                } else {
                    setUnFavrouite();

                }

                break;
            case R.id.llClick:
                Intent innn = new Intent(mContext, GalleryActivity.class);
                innn.putExtra(Const.DTO, viewAllAuctionDTO.getGallery());
                innn.putExtra(Const.USER_PUB_ID,viewAllAuctionDTO.getUser_pub_id());

                innn.putExtra(Const.FLAG,flag);
                startActivity(innn);
                break;
/*
            case R.id.ivShare:
                shareTextUrl();
                break;*/

            case R.id.tvViewProfile:

                    Intent intent = new Intent(ViewAuction.this, ViewProfileActivity.class);
                    intent.putExtra(Const.USER_PUB_ID, viewAllAuctionDTO.getUser_pub_id());
                    intent.putExtra(Const.GET_ALL_RATING,viewAllAuctionDTO.getAllrating());
                    startActivity(intent);

                break;
            case R.id.tvViewRating:

                    Intent intent1 = new Intent(ViewAuction.this, GetAllRating.class);
                    intent1.putExtra(Const.USER_PUB_ID, userDTO.getUser_pub_id());
                    intent1.putExtra(Const.Pro_pub_id,viewAllAuctionDTO.getPro_pub_id() );
                    startActivity(intent1);


                break;
            case R.id.comment:


                if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
                    alertGuestUser();


                }else{
                    dialogbox_comment();

                }
                break;
            case R.id.tvViewAllBid:
                Intent inten=new Intent(ViewAuction.this,ViewBidAuntion.class);
                inten.putExtra(Const.Pro_pub_id,id);
                startActivity(inten);
                break;
            case R.id.ivMap:
                Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+viewAllAuctionDTO.getLatitude()+","+viewAllAuctionDTO.getLongitude()));
                startActivity(intent2);
                break;
        }
    }

    private void setfavrouite() {

        if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
            alertGuestUser();


        }else{
            checkValid=2;
            paramsfavrouite.put(Const.USER_PUB_ID,user_pub_id);
            paramsfavrouite.put(Const.Pro_pub_id, id);
        }
        new HttpsRequest(Const.GET_FAVROUITE, paramsfavrouite, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    binding.ivFavScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_filledfav));
                    viewAllAuctionDTO.setFavourite("1");

                } else {
                    if(checkValid==1){
                        Toast.makeText(mContext, getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_LONG).show();


                    }else{
                        ProjectUtils.showToast(mContext, msg);

                    }
                }
            }
        });
    }

    private void shareTextUrl() {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "Hello...............");

        startActivity(Intent.createChooser(share, "Share link!"));
    }


    private void setUnFavrouite() {

        if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
            alertGuestUser();

        }else{
            checkValid=2;
            paramsUnfavrouite.put(Const.USER_PUB_ID, user_pub_id);
            paramsUnfavrouite.put(Const.Pro_pub_id, id);
        }
        new HttpsRequest(Const.GET_MYUNFAV, paramsUnfavrouite, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    binding.ivFavScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfav));
                    viewAllAuctionDTO.setFavourite("0");


                } else {
                    if(checkValid==1){
                        Toast.makeText(mContext, getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_LONG).show();


                    }else{
                        ProjectUtils.showToast(mContext, msg);

                    }
                }
            }
        });
    }

    private void dialogbox_add_bid() {

        dialogbox_add_bid = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialogbox_add_bid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_add_bid.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_add_bid.setContentView(R.layout.activity_add_bid);
        etBidPrice = dialogbox_add_bid.findViewById(R.id.etBidPrice);
        btnAddBid = dialogbox_add_bid.findViewById(R.id.btnAddBid);
        CustomTextViewBold tvbidproductname = dialogbox_add_bid.findViewById(R.id.tvBidProductname);
        CustomTextViewBold tvbidprice = dialogbox_add_bid.findViewById(R.id.tvBidProductprice);
        ivClose = dialogbox_add_bid.findViewById(R.id.ivClose);
        dialogbox_add_bid.show();
        dialogbox_add_bid.setCancelable(true);
        tvbidprice.setText( viewAllAuctionDTO.getPrice()+ " " +viewAllAuctionDTO.getCurrency_code());
        tvbidproductname.setText(viewAllAuctionDTO.getTitle());

        btnAddBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proPrice = etBidPrice.getText().toString();
                if (!proPrice.startsWith("0")) {
                    paramsAddBid.put(Const.BIDPRICE, proPrice);
                    setData();
                } else {
                    ProjectUtils.showToast(mContext, mContext.getResources().getString(R.string.valid_bid));
                }

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogbox_add_bid.dismiss();
            }
        });


    }


    private void dialogbox_comment() {

        dialogbox_comment = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialogbox_comment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final CommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialogbox_comment.getContext()),R.layout.comment,null,false);
        dialogbox_comment.setContentView(binding .getRoot());
        //   final CommentBinding binding=DataBindingUtil.setContentView(this,R.layout.comment);
        dialogbox_comment.show();
        dialogbox_comment.setCancelable(true);


        binding.simpleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                paramRating.put(Const.STAR, String.valueOf(binding.simpleRatingBar.getRating()));
            }
        });


        binding.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                paramRating.put(Const.COMMENT,binding.etComment.getText().toString());

                    checkValid=2;
                    paramRating.put(Const.USER_PUB_ID, user_pub_id);
                    paramRating.put(Const.Pro_pub_id, id);

                    getSelectingRating();




            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_comment.dismiss();
            }
        });


    }


    public void test(){
        final android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSingleAuction();
            }
        },5000);
    }

    private void setData() {
        dialogbox_add_bid.dismiss();
        if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
            alertGuestUser();

        }else{
            checkValid=2;
            paramsAddBid.put(Const.USER_PUB_ID, user_pub_id);
            paramsAddBid.put(Const.Pro_pub_id, id);

        }


        new HttpsRequest(Const.ADD_BID, paramsAddBid, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    test();
                    ProjectUtils.showToast(mContext, msg);



                } else {
                    if(checkValid==1){
                        Toast.makeText(mContext, getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_LONG).show();


                    }else{
                        ProjectUtils.showToast(mContext, msg);

                    }

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


    public void timer(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSingleAuction();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }


}




