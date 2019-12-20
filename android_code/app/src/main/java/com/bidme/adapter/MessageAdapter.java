package com.bidme.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.databinding.ChatBubbleLeft1Binding;
import com.bidme.databinding.ChatBubbleRightBinding;
import com.bidme.model.ChatMessageDTO;
import com.bidme.model.UserDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ChatMessageDTO> chatMessageDTOArrayList;
    private LayoutInflater layoutInflater;
    private final int TYPE_INCOMING = 1;
    private final int TYPE_OUTGOING = 2;
    private UserDTO userDTO;
    String image;


    public MessageAdapter(Context mContext, ArrayList<ChatMessageDTO> chatMessageDTOArrayList, UserDTO userDTO, String image) {
        this.mContext = mContext;
        this.chatMessageDTOArrayList = chatMessageDTOArrayList;
        this.userDTO = userDTO;
        this.image = image;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int postion) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        switch (postion) {
            case TYPE_INCOMING:
                ChatBubbleLeft1Binding binding =
                        DataBindingUtil.inflate(layoutInflater, R.layout.chat_bubble_left1, parent, false);
                return new MyViewHolderIn(binding);
            case TYPE_OUTGOING:
                ChatBubbleRightBinding binding1 =
                        DataBindingUtil.inflate(layoutInflater, R.layout.chat_bubble_right, parent, false);
                return new MyViewHolderOut(binding1);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolderIn) {
            MyViewHolderIn myViewHolder = (MyViewHolderIn) viewHolder;
            myViewHolder.binding.txtMsg.setText(chatMessageDTOArrayList.get(position).getMessage());
            Glide
                    .with(mContext)
                    .load(image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(myViewHolder.binding.ivCat);
            myViewHolder.binding.txtTime.setText(ProjectUtils.convertTimestampDateToTime(Long.parseLong(chatMessageDTOArrayList.get(position).getDate())));

        } else if (viewHolder instanceof MyViewHolderOut) {
            MyViewHolderOut myViewHolder = (MyViewHolderOut) viewHolder;
            myViewHolder.binding.txtMsg.setText(chatMessageDTOArrayList.get(position).getMessage());
            Glide
                    .with(mContext)
                    .load(userDTO.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(myViewHolder.binding.ivCat);
            myViewHolder.binding.tvTime.setText(ProjectUtils.convertTimestampDateToTime(Long.parseLong(chatMessageDTOArrayList.get(position).getDate())));

        }


    }

    @Override
    public int getItemCount() {
        return chatMessageDTOArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if (!chatMessageDTOArrayList.get(position).getSender_user_pub_id().equals(userDTO.getUser_pub_id())) {
            return TYPE_INCOMING;
        }
        return TYPE_OUTGOING;
    }


    public class MyViewHolderIn extends RecyclerView.ViewHolder {
        public ChatBubbleLeft1Binding binding;

        public MyViewHolderIn(ChatBubbleLeft1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class MyViewHolderOut extends RecyclerView.ViewHolder {
        public ChatBubbleRightBinding binding;

        public MyViewHolderOut(ChatBubbleRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}