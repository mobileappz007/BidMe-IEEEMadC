package com.bidme.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bidme.activity.aution.AddAuction;
import com.bidme.activity.aution.ViewMyAunction;
import com.bidme.databinding.AdapterItemMyAunctionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyAutionDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAuctionAdapter extends RecyclerView.Adapter<MyAuctionAdapter.MyViewHolder> {
    private static final String TAG = MyAuctionAdapter.class.getCanonicalName();
    private Context mContext;
    private ArrayList<MyAutionDTO> myAutionDTOArrayList;
    private LayoutInflater layoutInflater;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private int pos = -1;




    public MyAuctionAdapter(Context mContext, ArrayList<MyAutionDTO> myAutionDTOArrayList) {
        this.mContext = mContext;
        this.myAutionDTOArrayList = myAutionDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterItemMyAunctionBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_item_my_aunction, parent, false);

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        params.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
        params.put(Const.Pro_pub_id, myAutionDTOArrayList.get(position).getPro_pub_id());

        myViewHolder.binding.tvtitle.setText(ProjectUtils.capWordFirstLetter(myAutionDTOArrayList.get(position).getTitle()));
        myViewHolder.binding.tvShortDescription.setText(ProjectUtils.capWordFirstLetter(myAutionDTOArrayList.get(position).getSort_description()));
        myViewHolder.binding.tvDate.setText(ProjectUtils.changeDateFormate(myAutionDTOArrayList.get(position).getCreated_at()));
        myViewHolder.binding.tvPrice.setText( myAutionDTOArrayList.get(position).getPrice()+ " " +myAutionDTOArrayList.get(position).getCurrency_code() );
       try {
           Glide
                   .with(mContext)
                   .load(myAutionDTOArrayList.get(position).getImage_cust().get(0).getImage())
                   .centerCrop()
                   .placeholder(R.drawable.noimage)
                   .into(myViewHolder.binding.iv1);
       }catch(Exception e){
        }

       if(myAutionDTOArrayList.get(position).getStatus().equalsIgnoreCase("1")){

           myViewHolder.binding.deactivate.setVisibility(View.VISIBLE);
           myViewHolder.binding.activate.setVisibility(View.GONE);

       }else{
           myViewHolder.binding.deactivate.setVisibility(View.GONE);
           myViewHolder.binding.activate.setVisibility(View.VISIBLE);
       }


        myViewHolder.binding.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(mContext, ViewMyAunction.class);
                inn.putExtra(Const.MY_AUCTIONDTO,myAutionDTOArrayList.get(position));
                inn.putExtra("tag","2");
                mContext.startActivity(inn);
            }
        });

        myViewHolder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, AddAuction.class);
                in.putExtra(Const.MY_AUCTIONDTO, myAutionDTOArrayList.get(position));
                in.putExtra(Const.FLAG, 1);

                mContext.startActivity(in);
            }
        });
        myViewHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                deleteDailog();

            }
        });

    }

    private void deleteAunctions() {




        new HttpsRequest(Const.DELETE_AUNTION, params, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    myAutionDTOArrayList.remove(pos);
                    notifyDataSetChanged();


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

                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Delete auction.");

        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }




    @Override
    public int getItemCount() {
        return myAutionDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterItemMyAunctionBinding binding;

        public MyViewHolder(@NonNull AdapterItemMyAunctionBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

