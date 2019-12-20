package com.bidme.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.activity.aution.ViewAuction;
import com.bidme.databinding.AdapterItemMyBidsBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyBidDTO;
import com.bidme.model.UserDTO;
import com.bidme.myauctionfregment.MyAds;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.CustomEditText;
import com.bidme.utils.CustomTextViewBold;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bidme.R.layout.adapter_item_my_bids;

public class MyBidAdapter extends RecyclerView.Adapter<MyBidAdapter.MyViewHolder> {
    private static final String TAG = MyBidAdapter.class.getCanonicalName();

    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<MyBidDTO> getAllBidDTOArrayList;
    private AdapterItemMyBidsBinding binding;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private int pos = -1;
    private HashMap<String, String> paramsAddBid = new HashMap<>();
    MyAds myAds;


    public MyBidAdapter(Context mContext, ArrayList<MyBidDTO> getAllBidDTOArrayList, MyAds myAds) {
        this.mContext = mContext;
        this.getAllBidDTOArrayList = getAllBidDTOArrayList;
        this.myAds = myAds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, adapter_item_my_bids, parent, false);
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);


        return new MyBidAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {


        myViewHolder.binding.tvProductname.setText(ProjectUtils.capWordFirstLetter(getAllBidDTOArrayList.get(position).getTitle()));
        myViewHolder.binding.tvAuctionPrice.setText( getAllBidDTOArrayList.get(position).getAuction_price()+ " " +getAllBidDTOArrayList.get(position).getCurrency_code() );
        myViewHolder.binding.tvMinBidTxt.setText(ProjectUtils.changeDateFormate(getAllBidDTOArrayList.get(position).getCreated_at()));
        myViewHolder.binding.tvPrice.setText( getAllBidDTOArrayList.get(position).getPrice()+ " " +getAllBidDTOArrayList.get(position).getCurrency_code() );

        try {
            Glide
                    .with(mContext)
                    .load(getAllBidDTOArrayList.get(position).getImage_cust().get(0).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(myViewHolder.binding.image);
        }catch(Exception e){
        }

        myViewHolder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_add_bid(position);
            }
        });
        myViewHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                params.put(Const.BID_PUB_ID, getAllBidDTOArrayList.get(position).getBid_pub_id());
                deleteDailog();
            }
        });

        myViewHolder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(mContext, ViewAuction.class);
                inn.putExtra(Const.Pro_pub_id, getAllBidDTOArrayList.get(position).getPro_pub_id());
                inn.putExtra(Const.FLAG, "2");
                //inn.putExtra(Const.MY_AUCTIONDTO, getAllBidDTOArrayList.get(position));
                mContext.startActivity(inn);
            }
        });


    }

    private void deleteDailog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to delete this bid?")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteBidAunctions();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Delete bid.");

        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }


    private void deleteBidAunctions() {


        new HttpsRequest(Const.DELETE_BID, params, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    getAllBidDTOArrayList.remove(pos);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                } else {

                }
            }
        }));

    }


    @Override
    public int getItemCount() {
        return getAllBidDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterItemMyBidsBinding binding;

        public MyViewHolder(@NonNull AdapterItemMyBidsBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;
        }
    }


    private void dialogbox_add_bid(final int i) {

        final Dialog dialogbox_add_bid = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialogbox_add_bid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_add_bid.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_add_bid.setContentView(R.layout.activity_add_bid);
        final CustomEditText etBidPrice = dialogbox_add_bid.findViewById(R.id.etBidPrice);
        Button btnAddBid = dialogbox_add_bid.findViewById(R.id.btnAddBid);
        CustomTextViewBold tvbidproductname = dialogbox_add_bid.findViewById(R.id.tvBidProductname);
        CustomTextViewBold tvbidprice = dialogbox_add_bid.findViewById(R.id.tvBidProductprice);
        ImageView ivClose = dialogbox_add_bid.findViewById(R.id.ivClose);
        dialogbox_add_bid.show();
        dialogbox_add_bid.setCancelable(true);

        btnAddBid.setText(mContext.getResources().getString(R.string.update_bid));

        tvbidprice.setText(getAllBidDTOArrayList.get(i).getCurrency_code() + " " + getAllBidDTOArrayList.get(i).getAuction_price());
        tvbidproductname.setText(getAllBidDTOArrayList.get(i).getTitle());
        etBidPrice.setText(getAllBidDTOArrayList.get(i).getPrice());

        btnAddBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!etBidPrice.getText().toString().trim().startsWith("0")) {

                    paramsAddBid = new HashMap<>();
                    paramsAddBid.put(Const.BIDPRICE, etBidPrice.getText().toString().trim());
                    paramsAddBid.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
                    paramsAddBid.put(Const.Pro_pub_id, getAllBidDTOArrayList.get(i).getPro_pub_id());
                    paramsAddBid.put(Const.BID_PUB_ID, getAllBidDTOArrayList.get(i).getBid_pub_id());

                    setData();
                    dialogbox_add_bid.dismiss();
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

    private void setData() {
        new HttpsRequest(Const.UPDATE_BID, paramsAddBid, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    Toast.makeText(mContext, "successs", Toast.LENGTH_SHORT).show();
                    myAds.getMyBid();

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });

    }


}
