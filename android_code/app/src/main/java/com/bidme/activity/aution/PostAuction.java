package com.bidme.activity.aution;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.PostAunctionAdapter;
import com.bidme.databinding.ActivityPostAuctionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.interfaces.RecycleViewClickListner;
import com.bidme.model.CategoryDTO;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostAuction extends AppCompatActivity {
    private static final String TAG = PostAuction.class.getCanonicalName();


    Context mContext;

    private ActivityPostAuctionBinding binding;
    private ArrayList<CategoryDTO> CategoryDTOArrayList;
    private RecyclerView.LayoutManager layoutManager;
    private PostAunctionAdapter postAunctionAdapter;
    RecycleViewClickListner listner;

    String category_id;

    CategoryDTO CategoryDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = PostAuction.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_auction);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllCategory();

    }


    private void getAllCategory() {


        new HttpsRequest(Const.GET_ALL_CATEGORY, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {

                        CategoryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();

                        CategoryDTOArrayList = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);

                        layoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
                        binding.customgrid.setHasFixedSize(true);
                        binding.customgrid.setLayoutManager(layoutManager);
                        postAunctionAdapter = new PostAunctionAdapter(PostAuction.this, CategoryDTOArrayList);
                        binding.customgrid.setAdapter(postAunctionAdapter);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });

    }

    public void getCategoryid(int position) {
        CategoryDTO = CategoryDTOArrayList.get(position);
        category_id = CategoryDTO.getCat_id();
        Intent intent = new Intent(mContext, AddAuction.class);
        intent.putExtra(Const.GET_CAT_ID, category_id);
        startActivity(intent);


    }
}

