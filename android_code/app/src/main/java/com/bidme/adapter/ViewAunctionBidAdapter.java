package com.bidme.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.databinding.AdapterViewauntionBidBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.BidsDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bidme.R.layout.adapter_viewauntion_bid;

public class ViewAunctionBidAdapter extends RecyclerView.Adapter<ViewAunctionBidAdapter.MyViewHolder> {
    private String TAG = ViewAunctionBidAdapter.class.getCanonicalName();

    private ArrayList<BidsDTO> bidsDTOArrayList;
    Context mContext;
    LayoutInflater layoutInflater;
    AdapterViewauntionBidBinding bidBinding;
    private int pos = -1;
    private String tag;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    private String user_pub_id;
    private String pro_pub_id;
    private String block_user_pub_id;

    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> params2 = new HashMap<>();





    public ViewAunctionBidAdapter(ArrayList<BidsDTO> bidsDTOArrayList, Context mContext,String tag) {
        this.bidsDTOArrayList = bidsDTOArrayList;
        this.mContext = mContext;
        this.tag=tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        bidBinding = DataBindingUtil.inflate(layoutInflater, adapter_viewauntion_bid, parent, false);

        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        user_pub_id = userDTO.getUser_pub_id();


        return new ViewAunctionBidAdapter.MyViewHolder(bidBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.bidBinding.tvname.setText(ProjectUtils.capWordFirstLetter(bidsDTOArrayList.get(position).getName()));
        myViewHolder.bidBinding.tvprice.setText( bidsDTOArrayList.get(position).getPrice()+ " " +bidsDTOArrayList.get(position).getCurrency_code() );
        myViewHolder.bidBinding.tvdate.setText(ProjectUtils.changeDateFormate(bidsDTOArrayList.get(position).getCreated_at()));
        Glide
                .with(mContext)
                .load(bidsDTOArrayList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.bidBinding.ivProfilePic);
        pro_pub_id=bidsDTOArrayList.get(position).getPro_pub_id();
        block_user_pub_id=bidsDTOArrayList.get(position).getUser_pub_id();

        params.put(Const.Pro_pub_id,pro_pub_id);
        params2.put(Const.Pro_pub_id,pro_pub_id);
        params.put(Const.USER_PUB_ID,user_pub_id);
        params.put(Const.BLOCK_USER_PUB_ID,block_user_pub_id);
        params2.put(Const.BLOCK_USER_PUB_ID,block_user_pub_id);



        myViewHolder.bidBinding.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                params.put(Const.BID_PUB_ID,bidsDTOArrayList.get(position).getBid_pub_id());
                deleteDailog();
            }
        });


        if(bidsDTOArrayList.get(position).getStatus().equals("2")){
            myViewHolder.bidBinding.unblock.setVisibility(View.VISIBLE);
        }else if(bidsDTOArrayList.get(position).getStatus().equals("1")){
            myViewHolder.bidBinding.block.setVisibility(View.VISIBLE);

        }else{
            myViewHolder.bidBinding.block.setVisibility(View.VISIBLE);

        }



        if(tag.equalsIgnoreCase("1")){
            myViewHolder.bidBinding.block.setVisibility(View.VISIBLE);
            myViewHolder.bidBinding.Delete.setVisibility(View.VISIBLE);

        }else{
            myViewHolder.bidBinding.block.setVisibility(View.GONE);
            myViewHolder.bidBinding.unblock.setVisibility(View.GONE);
            myViewHolder.bidBinding.Delete.setVisibility(View.GONE);
        }

        myViewHolder.bidBinding.block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockUser();


            }
        });
            myViewHolder.bidBinding.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unblockUser();

            }
        });



        if(bidsDTOArrayList.get(position).getIswin().equals("1")){
            bidBinding.wintag.setVisibility(View.VISIBLE);
        }else {
            bidBinding.wintag.setVisibility(View.GONE);
        }

        //notifyDataSetChanged();


    }

    private void deleteDailog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want this user's bid?")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteBidUser();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Delete auction.");

        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }



    private void deleteBidUser() {

        new HttpsRequest(Const.DELETE_BID_user, params, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    bidsDTOArrayList.remove(pos);
                    notifyDataSetChanged();


                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                }
            }
        }));


    }

    @Override
    public int getItemCount() {

        if (tag.equals("5")) {
            return Math.min(bidsDTOArrayList.size(), 5);
        } else {
            return bidsDTOArrayList.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterViewauntionBidBinding bidBinding;

        public MyViewHolder(@NonNull AdapterViewauntionBidBinding itemBinding) {
            super(itemBinding.getRoot());
            this.bidBinding = itemBinding;
        }
    }

    private void blockUser() {

        new HttpsRequest(Const.BLOCK_USER, params, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    bidBinding.unblock.setVisibility(View.VISIBLE);
                    bidBinding.block.setVisibility(View.GONE);
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                }
            }
        }));

    }
  private void unblockUser() {

        new HttpsRequest(Const.UnBlockUser, params2, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    bidBinding.block.setVisibility(View.VISIBLE);
                    bidBinding.unblock.setVisibility(View.GONE);

                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                }
            }
        }));

    }

}
