package com.deecoders.meribindiya.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Bookings;
import com.deecoders.meribindiya.activity.EditProfile;
import com.deecoders.meribindiya.activity.Home;
import com.deecoders.meribindiya.activity.Otp;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.util.CircularNetworkImageView;
import com.deecoders.meribindiya.util.MyPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainMenu extends Fragment {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.homePanel)
    LinearLayout homePanel;
    @BindView(R.id.bookingPanel)
    LinearLayout bookingPanel;
    @BindView(R.id.profilePanel)
    LinearLayout profilePanel;
    @BindView(R.id.walletPanel)
    LinearLayout walletPanel;
    @BindView(R.id.referPanel)
    LinearLayout referPanel;
    @BindView(R.id.privacyPanel)
    LinearLayout privacyPanel;
    @BindView(R.id.callUsPanel)
    LinearLayout callUsPanel;
    @BindView(R.id.ratePanel)
    LinearLayout ratePanel;
    @BindView(R.id.logoutPanel)
    LinearLayout logoutPanel;
    Unbinder unbinder;

    public static MainMenu newInstance(String type) {
        MainMenu fragment = new MainMenu();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(MyPref.getGender(getActivity()).equals("male")){
            image.setImageResource(R.drawable.male);
        }
        else{
            image.setImageResource(R.drawable.female);
        }
        name.setText(MyPref.getName(getActivity()));
        phone.setText(MyPref.getMobile(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.homePanel, R.id.bookingPanel, R.id.profilePanel, R.id.walletPanel, R.id.referPanel, R.id.privacyPanel, R.id.callUsPanel, R.id.ratePanel, R.id.logoutPanel})
    public void onViewClicked(View view) {
        Constants.clickEffect(view);
        switch (view.getId()) {
            case R.id.homePanel:
                ((Home)getActivity()).drawerToggle();
                break;
            case R.id.bookingPanel:
                Intent intent1 = new Intent(getActivity(), Bookings.class);
                startActivity(intent1);
                break;
            case R.id.profilePanel:
                Intent intent2 = new Intent(getActivity(), EditProfile.class);
                startActivity(intent2);
                break;
            case R.id.walletPanel:
                break;
            case R.id.referPanel:
                break;
            case R.id.privacyPanel:
                break;
            case R.id.callUsPanel:
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+Constants.callPhone));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(call);
                }
                break;
            case R.id.ratePanel:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.playstoreUrl));
                startActivity(i);
                break;
            case R.id.logoutPanel:
                MyPref.setLogin(getActivity(), 0);
                getActivity().finishAffinity();
                Intent intent = new Intent(getActivity(), Otp.class);
                startActivity(intent);
                break;
        }
    }
}
