package com.bidme.activity.categories;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import com.bidme.activity.FilterActivity;
import com.bidme.activity.SearchActivity;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.R;
import com.bidme.adapter.CategoryListAdapter;
import com.bidme.databinding.ActivityCategorylistBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.model.CategoryDTO;
import com.bidme.utils.ProjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView.LayoutManager layoutManager;
    CategoryListAdapter categoryListAdapter;
    Context mContext;
    ArrayList<CategoryDTO> CategoryDTOArrayList;


    private ActivityCategorylistBinding binding;
    private String TAG = CategoryList.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categorylist);
        mContext = CategoryList.this;

        setUIAction();

    }


    private void setUIAction() {
        binding.iAHN.ivMenu.setVisibility(View.GONE);
        binding.iAHN.ivBack.setVisibility(View.VISIBLE);
        binding.iAHN.tvtitlemain.setText("Categories");

        binding.iAHN.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(mContext, Dashboard.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.putExtra("index",0);
                startActivity(in);
            }
        });
        binding.iAHN.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, FilterActivity.class);
                startActivity(in);

            }
        });
        binding.iAHN.ivSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, SearchActivity.class);
                startActivity(in);

            }
        });


    }

    @Override
    protected void onResume() {
        getAllCategory();
        super.onResume();
    }

    private void getAllCategory() {
        new HttpsRequest(Const.GET_ALL_CATEGORY, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {
                        binding.recycleview1.setVisibility(View.VISIBLE);
                        CategoryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();

                        CategoryDTOArrayList = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }

    private void showData() {
        layoutManager = new GridLayoutManager(this, 1);
        binding.recycleview1.setHasFixedSize(true);
        binding.recycleview1.setLayoutManager(layoutManager);
        categoryListAdapter = new CategoryListAdapter(mContext, CategoryDTOArrayList);
        binding.recycleview1.setAdapter(categoryListAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
