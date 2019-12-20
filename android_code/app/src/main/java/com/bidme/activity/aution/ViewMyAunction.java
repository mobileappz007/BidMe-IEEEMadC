package com.bidme.activity.aution;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.AdapterAllRating;
import com.bidme.adapter.ViewAunctionBidAdapter;
import com.bidme.databinding.ActivityViewMyAunctionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.GetRatingDTO;
import com.bidme.model.MyAutionDTO;
import com.bidme.model.NotificationDTO;
import com.bidme.model.UserDTO;
import com.bidme.model.ViewAllAuctionDTO;
import com.bidme.myauctionfregment.MyAuctions;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class ViewMyAunction extends AppCompatActivity {
    private ActivityViewMyAunctionBinding binding;
    private ViewAllAuctionDTO viewAllAuctionDTO;
    HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    Context mContext;
    private static final String TAG = ViewAuction.class.getCanonicalName();
    private ViewAunctionBidAdapter viewAunctionBidAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GetRatingDTO> getRatingDTOArrayList;
    private AdapterAllRating adapterAllRating;
    private MyAuctions myAuctions = new MyAuctions();
    private String flag = "2";
    private String status = "";
    private String tag;

    MyAutionDTO myAutionDTO;
    NotificationDTO notificationDTO;

    String remainTime = "";
    String newTIme = "691200";
    String E_Date;
int validForcountdown=0;

    String productPubId;
    private HashMap<String, String> paramsAllrating = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAuctions.getActivity();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_my_aunction);
        mContext = ViewMyAunction.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);

        tag = getIntent().getStringExtra("tag");

        if (tag.equals("1")) {
            notificationDTO = (NotificationDTO) getIntent().getSerializableExtra(Const.NOTIFICATION_DTO);
            productPubId = notificationDTO.getPro_pub_id();
            params.put(Const.Pro_pub_id, productPubId);

        } else {
            myAutionDTO = (MyAutionDTO) getIntent().getSerializableExtra(Const.MY_AUCTIONDTO);
            productPubId = myAutionDTO.getPro_pub_id();
            params.put(Const.Pro_pub_id, productPubId);
            validForcountdown=1;
            checkMyauctionStatus();

        }


        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        paramsAllrating.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        paramsAllrating.put(Const.Pro_pub_id, productPubId);


        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, AddAuction.class);
                in.putExtra(Const.MY_AUCTIONDTO, getIntent().getSerializableExtra(Const.MY_AUCTIONDTO));
                in.putExtra(Const.FLAG, 1);
                mContext.startActivity(in);


            }
        });

        binding.llViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAllAuctionDTO.getGallery().size() >= 0) {
                    Intent in = new Intent(mContext, GalleryActivity.class);
                    in.putExtra(Const.DTO, viewAllAuctionDTO.getGallery());
                    in.putExtra(Const.Pro_pub_id, viewAllAuctionDTO.getPro_pub_id());
                    in.putExtra(Const.USER_PUB_ID, viewAllAuctionDTO.getUser_pub_id());
                    in.putExtra(Const.FLAG, flag);
                    startActivity(in);
                } else {
                    ProjectUtils.showToast(mContext, "No gallery first upload image then you can update more.");
                }
            }
        });

        binding.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAllAuctionDTO.getGallery().size() >= 0) {
                    Intent in = new Intent(mContext, GalleryActivity.class);
                    in.putExtra(Const.USER_PUB_ID, viewAllAuctionDTO.getUser_pub_id());
                    in.putExtra(Const.USER_PUB_ID, viewAllAuctionDTO.getPro_pub_id());
                    in.putExtra(Const.DTO, viewAllAuctionDTO.getGallery());
                    in.putExtra(Const.FLAG, flag);

                    startActivity(in);
                } else {
                    ProjectUtils.showToast(mContext, "No gallery first upload image then you can update more.");
                }
            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDailog();

            }
        });

        binding.tvViewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewMyAunction.this, GetAllRating.class);
                intent1.putExtra(Const.USER_PUB_ID, userDTO.getUser_pub_id());
                intent1.putExtra(Const.Pro_pub_id, viewAllAuctionDTO.getPro_pub_id());
                startActivity(intent1);

            }
        });
        binding.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + viewAllAuctionDTO.getLatitude() + "," + viewAllAuctionDTO.getLongitude()));
                startActivity(intent2);
            }
        });

        binding.tvViewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewMyAunction.this, GetAllRating.class);
                intent1.putExtra(Const.USER_PUB_ID, userDTO.getUser_pub_id());
                intent1.putExtra(Const.Pro_pub_id, viewAllAuctionDTO.getPro_pub_id());
                startActivity(intent1);
            }
        });

        binding.tvViewAllBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(ViewMyAunction.this, ViewBidAuntion.class);
                inten.putExtra(Const.Pro_pub_id, viewAllAuctionDTO.getPro_pub_id());
                startActivity(inten);
            }
        });



        binding.tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(ViewMyAunction.this, ViewWinnerProfileActivity.class);
                inten.putExtra(Const.WinnerDTO, viewAllAuctionDTO.getWinner_details());
                startActivity(inten);
            }
        });

        //timer();

    }



    private void checkMyauctionStatus() {
        if (myAutionDTO.getStatus().equalsIgnoreCase("1")) {
            binding.deactivate.setVisibility(View.VISIBLE);
            binding.relayconuntdowns.setVisibility(View.GONE);

            Log.e(TAG, "status: ----------------" + myAutionDTO.getStatus());

        } else {
            binding.deactivate.setVisibility(View.GONE);
            binding.relayconuntdowns.setVisibility(View.VISIBLE);


        }
    }


    private void deleteAunctions() {

        new HttpsRequest(Const.DELETE_AUNTION, params, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    finish();

                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                } else {

                }
            }
        }));

    }

    private void deleteDailog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to delete this Aunction?")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAunctions();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                      /*  Toast.makeText(mContext, "you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();*/
                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Subasta");
        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkManager.isConnectToInternet(mContext)) {
            getSingleAuction();
            getAllRating();
        } else {
            ProjectUtils.showToast(mContext, getString(R.string.internet_connection));
        }
    }

    private void getSingleAuction() {

        new HttpsRequest(Const.GET_SINGLE_AUNCTION, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        viewAllAuctionDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ViewAllAuctionDTO.class);

                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    // ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public void showData() {
        E_Date = viewAllAuctionDTO.getE_date();
        remainTime=viewAllAuctionDTO.getRemaining_time();

        if(validForcountdown==1){
            if(myAutionDTO.getStatus().equalsIgnoreCase("1")){
                binding.relayconuntdowns.setVisibility(View.GONE);
                binding.deactivate.setVisibility(View.VISIBLE);
                binding.activate.setVisibility(View.GONE);
            }else {
                binding.relayconuntdowns.setVisibility(View.VISIBLE);
                binding.deactivate.setVisibility(View.GONE);
                binding.activate.setVisibility(View.VISIBLE);

                if(remainTime.equalsIgnoreCase("")){


                }else{
                    counter();
                }

            }

        }else{

            if(remainTime.equalsIgnoreCase("")){


            }else{
                counter();
            }
        }




        try {
            Glide
                    .with(mContext)
                    .load(viewAllAuctionDTO.getGallery().get(0).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(binding.ivGallery);
        } catch (Exception e) {
            Glide
                    .with(mContext)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(binding.ivGallery);

        }
        binding.deactivate.setImageResource(R.drawable.deactivate);


        binding.tvMediaCount.setText(viewAllAuctionDTO.getGallery_count());

        Glide
                .with(mContext)
                .load(viewAllAuctionDTO.getUsers().getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(binding.ivProfile);

        binding.tvName.setText(ProjectUtils.capWordFirstLetter(viewAllAuctionDTO.getUsers().getName()));
        binding.tvDate.setText(ProjectUtils.changeDateFormate(viewAllAuctionDTO.getCreated_at()));
        binding.tvPrice.setText(viewAllAuctionDTO.getPrice() + " " + viewAllAuctionDTO.getCurrency_code());
        binding.tvShortDescription.setText(viewAllAuctionDTO.getTitle());
        binding.tvAddress.setText(viewAllAuctionDTO.getAddress());
       // binding.tvDescription.setText(viewAllAuctionDTO.getDescription());
        binding.ShortDescription.setText(viewAllAuctionDTO.getSort_description());
        binding.StartDate.setText("Start Date - "+viewAllAuctionDTO.getS_date());
        binding.EndDate.setText("End Date - "+viewAllAuctionDTO.getE_date());
        binding.tvAddress.setText(viewAllAuctionDTO.getAddress());
        binding.catname.setText(viewAllAuctionDTO.getCat_title());

        binding.simpleRatingBar1.setRating(Float.parseFloat(viewAllAuctionDTO.getAllrating()));

        if (viewAllAuctionDTO.getBids().size() > 0) {
            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.rvBids.setLayoutManager(layoutManager);
            viewAunctionBidAdapter = new ViewAunctionBidAdapter(viewAllAuctionDTO.getBids(), mContext, "1");
            binding.rvBids.setAdapter(viewAunctionBidAdapter);
        } else {
            binding.linear.setVisibility(View.GONE);
        }


        try {
            if(viewAllAuctionDTO.getWinner_details()==null){
                    binding.tvViewProfile.setVisibility(View.GONE);

            }
        }catch (Exception e){


        }



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
                                adapterAllRating = new AdapterAllRating(mContext, getRatingDTOArrayList, "1");
                                binding.rvAllRating.setAdapter(adapterAllRating);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            binding.linRating.setVisibility(View.GONE);
                            //  ProjectUtils.showToast(mContext, msg);

                        }
                    }
                });


    }

    private void counter() {

        if (Long.valueOf(newTIme) > Long.valueOf(remainTime)) {
            Log.e("condition", newTIme + "---" + remainTime + "---" + Long.valueOf(remainTime) * 60 * 1000);



            binding.relayconuntdowns.setVisibility(View.VISIBLE);
            binding.counter.setVisibility(View.VISIBLE);
            binding.counter.start(Long.valueOf(remainTime) *1000);
            binding.counter.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    binding.counter.stop();
                    binding.relayconuntdowns.setVisibility(View.GONE);
                    binding.counter.setVisibility(View.GONE);
                }
            });

        } else {
            binding.relayconuntdowns.setVisibility(View.GONE);
            binding.counter.setVisibility(View.GONE);

        }




    }

    private void timer(){
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

