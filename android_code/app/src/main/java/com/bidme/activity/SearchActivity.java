package com.bidme.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.AutionAllAdapter;
import com.bidme.databinding.ActivitySearchBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.AutionAllDTO;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getCanonicalName();

    private Context mContext;
    private ActivitySearchBinding binding;
    private List<String> arrayList = new ArrayList<>();
    private HashMap<String, String> params = new HashMap<>();
    private AutionAllAdapter autionAllAdapter;
    private ArrayList<AutionAllDTO> autionAllDTOArrayList;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SearchActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);


        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                try {
                    params.put(Const.SEARCH, s);
                    getSearchAuctions();

                } catch (Exception e) {

                }

                return false;
            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSearchAuctions();
    }

    private void getSearchAuctions() {

        new HttpsRequest(Const.GET_ALL_SEARCH, params, mContext).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    try {

                        autionAllDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AutionAllDTO>>() {
                        }.getType();
                        autionAllDTOArrayList = (ArrayList<AutionAllDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        layoutManager = new GridLayoutManager(mContext,2, LinearLayoutManager.VERTICAL, false);
                        binding.searchAuctionRecycle.setLayoutManager(layoutManager);
                        binding.searchAuctionRecycle.setHasFixedSize(true);
                        autionAllAdapter = new AutionAllAdapter(SearchActivity.this, autionAllDTOArrayList);
                        binding.searchAuctionRecycle.setAdapter(autionAllAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        }));

    }
}
