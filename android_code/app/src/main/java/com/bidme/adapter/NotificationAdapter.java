package com.bidme.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bidme.R;
import com.bidme.activity.SupportAcitivity;
import com.bidme.activity.aution.ViewAuction;
import com.bidme.activity.aution.ViewMyAunction;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.databinding.AdapterNotificationBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.NotificationDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private ArrayList<NotificationDTO>notificationDTOArrayList;
    private Context mContext;
    private AdapterNotificationBinding binding;
    private   LayoutInflater layoutInflater;


    public NotificationAdapter( Context mContext,ArrayList<NotificationDTO> notificationDTOArrayList) {
        this.notificationDTOArrayList = notificationDTOArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_notification, parent, false);
        return new NotificationAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.binding.title.setText(notificationDTOArrayList.get(position).getTittle());
        myViewHolder.binding.message.setText(notificationDTOArrayList.get(position).getMessage());
        myViewHolder.binding.date.setText(ProjectUtils.changeDateFormate(notificationDTOArrayList.get(position).getCreated_at()));
        myViewHolder.binding.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationDTOArrayList.get(position).getType().equals(Const.WIN_BID_TYPE)){

                    Intent intent = new Intent(mContext, ViewAuction.class);
                    intent.putExtra(Const.Pro_pub_id,notificationDTOArrayList.get(position).getPro_pub_id());
                    intent.putExtra(Const.FLAG,"2");
                    mContext.startActivity(intent);
                }else if(notificationDTOArrayList.get(position).getType().equals(Const.BID_FAV_MESSAGE)){
                    Intent intent = new Intent(mContext, ViewAuction.class);
                    intent.putExtra(Const.Pro_pub_id,notificationDTOArrayList.get(position).getPro_pub_id());
                    intent.putExtra(Const.FLAG,"2");

                    mContext.startActivity(intent);
                }else if(notificationDTOArrayList.get(position).getType().equals(Const.BID_TYPE)){
                    Intent intent = new Intent(mContext, ViewMyAunction.class);
                    intent.putExtra(Const.NOTIFICATION_DTO,notificationDTOArrayList.get(position));
                    intent.putExtra("tag","1");
                    mContext.startActivity(intent);
                }else if(notificationDTOArrayList.get(position).getType().equals(Const.RESOLVED_ISSUE)){
                    Intent intent = new Intent(mContext, SupportAcitivity.class);
                    mContext.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Const.INDEX,
                            ProjectUtils.indexOfNotification(notificationDTOArrayList.get(position).getType()));
                    mContext.startActivity(intent);
                }




            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNotificationBinding binding;
        public MyViewHolder(@NonNull   AdapterNotificationBinding itemBinding) {

            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}
