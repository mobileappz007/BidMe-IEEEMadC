package com.bidme.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.activity.ChatstalkingActivity;
import com.bidme.databinding.AdapterMyChatBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.MyChatsDTO;
import com.bidme.model.UserDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Locale;

public class MyChatsAdapter extends RecyclerView.Adapter<MyChatsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MyChatsDTO> objects;
    private ArrayList<MyChatsDTO> myChatsDTOArrayList;
    private LayoutInflater layoutInflater;
    private UserDTO userDTO;
    private int bitmap;

    public MyChatsAdapter(Context mContext, ArrayList<MyChatsDTO> objects, UserDTO userDTO) {
        this.mContext = mContext;
        this.objects = objects;
        this.userDTO = userDTO;
        this.myChatsDTOArrayList = new ArrayList<>();
        this.myChatsDTOArrayList.addAll(objects);
    }

    @NonNull
    @Override
    public MyChatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {

            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        AdapterMyChatBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_my_chat, parent, false);

        return new MyChatsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyChatsAdapter.MyViewHolder myViewHolder, final int position) {

        Glide
                .with(mContext)
                .load(objects.get(position).getUser_image())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.ivPro);
        myViewHolder.binding.tvTitle.setText(ProjectUtils.capWordFirstLetter(objects.get(position).getUser_name()));
        myViewHolder.binding.tvMsg.setText(objects.get(position).getMessage());
        myViewHolder.binding.tvDate.setText(ProjectUtils.convertTimestampDateToTime(Long.parseLong(objects.get(position).getDate())));

        myViewHolder.binding.rvChat.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(mContext, ChatstalkingActivity.class);
                        if (objects.get(position).getReceiver_user_pub_id().equalsIgnoreCase(userDTO.getUser_pub_id())){
                            in.putExtra(Const.RECEIVER_ID, objects.get(position).getSender_user_pub_id());
                            in.putExtra(Const.RECEIVER_NAME, objects.get(position).getUser_name());
                            in.putExtra(Const.RECEIVER_IMAGE, objects.get(position).getUser_image());
                        }else {

                            in.putExtra(Const.RECEIVER_ID, objects.get(position).getReceiver_user_pub_id());
                            in.putExtra(Const.RECEIVER_NAME, objects.get(position).getUser_name());
                            in.putExtra(Const.RECEIVER_IMAGE, objects.get(position).getUser_image());

                        }
                        mContext.startActivity(in);

                    }
                });


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterMyChatBinding binding;

        public MyViewHolder(@NonNull AdapterMyChatBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public void filter(String s) {
        s = s.toLowerCase(Locale.getDefault());
        objects.clear();
        if (s.length() == 0) {
            objects.addAll(myChatsDTOArrayList);

        } else {
            for (MyChatsDTO myChatsDTO : myChatsDTOArrayList) {
                if (myChatsDTO.getUser_name().toLowerCase(Locale.getDefault()).contains(s)) {
                    objects.add(myChatsDTO);
                }


            }
        }
        notifyDataSetChanged();
    }
}
