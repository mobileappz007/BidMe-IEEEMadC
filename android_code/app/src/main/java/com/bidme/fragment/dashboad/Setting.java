package com.bidme.fragment.dashboad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bidme.BuildConfig;
import com.bidme.R;
import com.bidme.activity.SupportAcitivity;
import com.bidme.activity.authentication.ChangPassword;
import com.bidme.activity.authentication.UpdateProfile;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.activity.policy.FQ;
import com.bidme.activity.policy.TermsCondition;
import com.bidme.activity.policy.PrivacyPolicy;
import com.bidme.databinding.FragmentSettingBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;


public class Setting extends Fragment {
    private FragmentSettingBinding binding;
    private Dashboard dashboard;
    private UserDTO userDTO;
    private TextView tvUserName;
    private ImageView ivProfilpic;
    SharedPrefrence sharedPrefrence;


    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        if (mcontext instanceof Activity) {
            this.dashboard = (Dashboard) mcontext;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);




        binding.llID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), UpdateProfile.class);
                startActivity(in);



            }
        });

        binding.tvchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), ChangPassword.class);
                startActivity(in);
            }
        });


        binding.tvinvited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl();
            }
        });

        binding.tvsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SupportAcitivity.class);
                startActivity(in);

            }
        });
        binding.tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(in);

            }
        });
        binding.tvCookiePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), TermsCondition.class);
                startActivity(in);

            }
        });
        binding.tvFQPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), FQ.class);
                startActivity(in);

            }
        });
        return binding.getRoot();
    }

    private void shareTextUrl() {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.invite)+"\n https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID);

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onDetach() {
        super.onDetach();
        this.dashboard = null;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        Glide.with(Setting.this)
                .load(userDTO.getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(binding.imagedp);
        binding.tvname.setText(ProjectUtils.capWordFirstLetter(userDTO.getName()));
        binding.tvdate.setText("Joined on "+ProjectUtils.changeDateFormate(userDTO.getCreated_at()));
    }
}
