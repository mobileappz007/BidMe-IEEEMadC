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
import com.bidme.activity.aution.ViewAuction;
import com.bidme.databinding.AdapterMyfavBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.MyFavDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFavAdapter extends RecyclerView.Adapter<MyFavAdapter.MyViewHolder> {

    private Context mContext;
    private static final String TAG = MyFavAdapter.class.getCanonicalName();
    private ArrayList<MyFavDTO> myFavDTOArrayList;
    private LayoutInflater layoutInflater;

    AdapterMyfavBinding binding;
    private HashMap<String, String> paramsUnfavrouite = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
     private int pos=-1;

    public MyFavAdapter(Context mContext, ArrayList<MyFavDTO> myFavDTOArrayList) {
        this.mContext = mContext;
        this.myFavDTOArrayList = myFavDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Const.USER_DTO);




        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterMyfavBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_myfav, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.binding.tvtitle.setText(ProjectUtils.capWordFirstLetter(myFavDTOArrayList.get(position).getTitle()));

        myViewHolder.binding.tvPrice.setText(myFavDTOArrayList.get(position).getPrice()+" "+myFavDTOArrayList.get(position).getCurrency_code());


        try {
            Glide
                    .with(mContext)
                    .load(myFavDTOArrayList.get(position).getImage_cust().get(0).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(myViewHolder.binding.iv1);
        }catch(Exception e){
        }

        myViewHolder.binding.tvDate.setText(ProjectUtils.changeDateFormate(myFavDTOArrayList.get(position).getCreated_at()));

    myViewHolder.binding.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent in = new Intent(mContext,ViewAuction.class);
            in.putExtra(Const.Pro_pub_id, myFavDTOArrayList.get(position).getPro_pub_id());
            in.putExtra(Const.FLAG, "2");

            // in.putExtra(Const.MY_AUCTIONDTO,myFavDTOArrayList.get(position));
            mContext.startActivity(in);
        }
    });
        myViewHolder.binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                paramsUnfavrouite.put(Const.USER_PUB_ID, userDTO.getUser_pub_id());
                paramsUnfavrouite.put(Const.Pro_pub_id, myFavDTOArrayList.get(position).getPro_pub_id());
                deleteDailog();




            }
        });

    }

    private void unfavoruite() {

        new HttpsRequest(Const.GET_MYUNFAV, paramsUnfavrouite, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    myFavDTOArrayList.remove(pos);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void deleteDailog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to unfavoruite this aunction")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        unfavoruite();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Delete favrouite auction.");
        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }


    @Override
    public int getItemCount() {
        return myFavDTOArrayList.size();
    }

    public  class MyViewHolder extends  RecyclerView.ViewHolder{
        AdapterMyfavBinding binding;


        public MyViewHolder(@NonNull AdapterMyfavBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }

}
